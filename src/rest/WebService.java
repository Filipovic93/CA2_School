package rest;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import entity.Person;
import entity.RoleSchool;
import exceptions.NotFoundException;
import facades.PersonFacade;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Vuk
 */
public class WebService {

    private final HttpServer server;

    private PersonFacade facade;
    private String contentFolder = "public/";

    public WebService(int port) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(port), 0);
        server.createContext("/person", createPersonHandler());
        server.createContext("/files", createFileHandler());
        server.createContext("/", createStartpageHandler());
        server.createContext("/role", createRoleHandler());
        facade = PersonFacade.getFacade();

    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    private HttpHandler createPersonHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                String response = "";
                int status = 200;
                String method = he.getRequestMethod().toUpperCase();
                switch (method) {
                    case "GET":
                        try {
                            String path = he.getRequestURI().getPath();
                            int lastIndex = path.lastIndexOf("/");
                            if (lastIndex > 0) {  //person/id
                                String idStr = path.substring(lastIndex + 1);
                                int id = Integer.parseInt(idStr);
                                response = facade.getPersonAsJSON(id);
                            } else { // person
                                response = facade.getPersonsAsJSON();
                            }
                        } catch (NumberFormatException nfe) {
                            response = "Id is not a number";
                            status = 404;
                        } catch (NotFoundException nfe) {
                            response = nfe.getMessage();
                            status = 404;
                        }
                        break;
                    case "POST":
                        try {
                            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                            BufferedReader br = new BufferedReader(isr);
                            String jsonQuery = br.readLine();
                            if (jsonQuery.contains("<") || jsonQuery.contains(">")) {

                                throw new IllegalArgumentException("Illegal characters in input");
                            }
                            Person p = facade.addPerson(jsonQuery);
                            if (p.getPhone().length() > 50 || p.getFirstName().length() > 50 || p.getLastName().length() > 70) {

                                throw new IllegalArgumentException("Input contains to many characters");
                            }
                            response = new Gson().toJson(p);
                        } catch (IllegalArgumentException iae) {
                            status = 400;
                            response = iae.getMessage();
                        } catch (IOException e) {
                            status = 500;
                            response = "Internal Server Problem";
                        }
                        break;
                    case "PUT":
                        break;
                    case "DELETE":
                        try {
                            String path = he.getRequestURI().getPath();
                            int lastIndex = path.lastIndexOf("/");
                            if (lastIndex > 0) {  //person/id
                                int id = Integer.parseInt(path.substring(lastIndex + 1));
                                Person pDeleted = facade.delete(id);
                                response = new Gson().toJson(pDeleted);
                            } else {
                                status = 400;
                                response = "<h1>Bad Request</h1>No id supplied with request";
                            }
                        } catch (NotFoundException nfe) {
                            status = 404;
                            response = nfe.getMessage();
                        } catch (NumberFormatException nfe) {
                            response = "Id is not a number";
                            status = 404;
                        }
                        break;
                }
                he.getResponseHeaders().add("Content-Type", "application/json");
                he.sendResponseHeaders(status, 0);
                try (OutputStream os = he.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        };
    }

    private HttpHandler createFileHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {

                Pattern pattern = Pattern.compile("/files/(.+)$");
                Matcher matcher
                        = pattern.matcher(he.getRequestURI().getPath());
                if (!matcher.matches()) {
                    error(he, 404, "No file defined");
                    return;
                }

                String fileName = contentFolder + matcher.group(1);

                File file = new File(fileName);
                String extension = fileName.substring(fileName.lastIndexOf("."));

                if (!file.exists()) {
                    error(he, 404, "File not found");
                    return;
                }
                he.getResponseHeaders().set("Content-Type", getMime(extension));
                he.sendResponseHeaders(200, 0);
                FileInputStream in = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                try (OutputStream responseBody = he.getResponseBody()) {
                    while (true) {
                        int bytesRead = in.read(buffer, 0, 4096);
                        if (bytesRead <= 0) {
                            break;
                        }
                        responseBody.write(buffer, 0, bytesRead);
                    }
                }
            }

            private String getMime(String extension) {
                String mime = "";
                switch (extension) {
                    case ".pdf":
                        mime = "application/pdf";
                        break;
                    case ".png":
                        mime = "image/png";
                        break;
                    case ".css":
                        mime = "text/css";
                        break;
                    case ".js":
                        mime = "text/javascript";
                        break;
                    case ".html":
                        mime = "text/html";
                        break;
                    case ".jar":
                        mime = "application/java-archive";
                        break;
                }
                return mime;
            }

        };
    }

    private HttpHandler createStartpageHandler() {

        return new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                String path = contentFolder + "index.html";
                File file = new File(path);
                byte[] bytesToSend = new byte[(int) file.length()];
                try {
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                    bis.read(bytesToSend, 0, bytesToSend.length);
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
                String contentType = "text/html";
                Headers h = he.getResponseHeaders();
                h.add("Content-Type", contentType);
                he.sendResponseHeaders(200, bytesToSend.length);
                try (OutputStream os = he.getResponseBody()) {
                    os.write(bytesToSend, 0, bytesToSend.length);

                }
            }
        };
    }

    private void error(HttpExchange he, int statusCode, String message) throws IOException {
        send(he, statusCode, message);
    }

    private void send(HttpExchange he, int statusCode, String message) throws IOException {
        he.sendResponseHeaders(statusCode, 0);
        try (OutputStream responseBody = he.getResponseBody()) {
            responseBody.write(message.getBytes());
        }
    }

    private HttpHandler createRoleHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                String response = "";
                int status = 200;
                String method = he.getRequestMethod().toUpperCase();
                switch (method) {
                    case "GET":
                        break;
                    case "POST":
                        try {
                            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                            BufferedReader br = new BufferedReader(isr);
                            String jsonQuery = br.readLine();
                            if (jsonQuery.contains("<") || jsonQuery.contains(">")) {

                                throw new IllegalArgumentException("Illegal characters in input");
                            }
                            String path = he.getRequestURI().getPath();
                            int lastIndex = path.lastIndexOf("/");
                            if (lastIndex > 0) {  //role/id
                                String idStr = path.substring(lastIndex + 1);
                                int id = Integer.parseInt(idStr);
                                RoleSchool role = facade.addRole(jsonQuery, id);
                                status = 200;
                                response = new Gson().toJson(role);
                            }
                        } catch (IllegalArgumentException iae) {
                            status = 400;
                            response = iae.getMessage();
                        } catch (IOException e) {
                            status = 500;
                            response = "Internal Server Problem";
                        } catch (NotFoundException ex) {
                            status = 404;
                            response = ex.getMessage();
                        }
                        break;
                    case "PUT":
                        break;
                    case "DELETE":
                        break;
                }
                he.getResponseHeaders().add("Content-Type", "application/json");
                he.sendResponseHeaders(status, 0);
                try (OutputStream os = he.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        };
    }
}
