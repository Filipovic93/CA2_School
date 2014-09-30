/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entity.Person;
import entity.RoleSchool;
import exceptions.NotFoundException;

/**
 *
 * @author Neno
 */
public interface IPersonFacade
{

    String getPersonsAsJSON();

    String getPersonAsJSON(int id) throws NotFoundException;

    Person addPerson(String json);

    RoleSchool addRole(String json, int id) throws NotFoundException;

    Person delete(int id) throws NotFoundException;

}
