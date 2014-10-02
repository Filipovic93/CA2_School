$(document).ready(function () {

    fetchAll();
    deletePerson();
    initPersons();
    initAddBtn();
    initCancelBtn();
    initSaveBtn();
    showRole();
    roleevent();

});

function showRole() {
    $("#btn_addrole").click(function () {
        $("#roleform").removeAttr("hidden");
        $("#roletxt").removeAttr("hidden");
        $("#btn_saverole").removeAttr("style"); 
    });
}


function roleevent() {
    $("#roleform").click(function(e){
       var selected = e.options[e.selectedIndex].text;
       if(selected === "Teacher")
       {
        $("#rolelabel").text("Degree");
        
    }
       if($("#roleform").val === "Student")
       {
           $("#rolelabel").text("Semester");
       }
       if($("#roleform").valueOf() === "AssistentTeacher")
       {
        $("#roletxt").hide();    
    }
    });
}


function initAddBtn() {
    $("#btn_add").click(function () {
        initDetails(true);
        fetchAll();
    });
}

function initSaveBtn() {
    $("#btn_save").click(function () {
        //First create post argument as a JavaScript object
        var newPerson = {"firstName": $("#fname").val(), "lastName": $("#lname").val(), "phone": $("#phone").val(), "email": $("#email").val()};
        $.ajax({
            url: "../person",
            data: JSON.stringify(newPerson), //Convert newPerson to JSON
            type: "post",
            dataType: 'json',
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText + ": " + textStatus);
            }
        }).done(function (newPerson) {
            $("#id").val(newPerson.id);
            initDetails(false);
            fetchAll();
        });
    });
}

function initCancelBtn() {
    $("#btn_cancel").click(function () {
        clearDetails();
        initDetails(false);
        fetchAll();
    });
}


function initPersons() {
    $("#persons").click(function (e) {
        var id = e.target.id;
        if (isNaN(id)) {
            return;
        }
        updateDetails(id);
    });
}

function deletePerson() {
    $("#delete").click(function () {
        $.ajax({
            url: "../person/" + $("#persons option:selected").attr("id"),
            type: "DELETE",
            dataType: 'json',
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText + ": " + textStatus);
            }
        }).done(function () {
            fetchAll();
        });
    });
}

function initDetails(init) {
    if (init) {
        $("#fname").removeAttr("disabled");
        $("#lname").removeAttr("disabled");
        $("#phone").removeAttr("disabled");
        $("#email").removeAttr("disabled");
        $("#role").removeAttr("disabled");
        $("#btn_save").removeAttr("disabled");
        $("#btn_cancel").removeAttr("disabled");
        $("#btn_add").attr("disabled", "disabled");
    }
    else {
        $("#fname").attr("disabled", "disabled");
        $("#lname").attr("disabled", "disabled");
        $("#phone").attr("disabled", "disabled");
        $("#email").attr("disabled", "disabled");
        $("#role").attr("disabled", "disabled");
        $("#btn_save").attr("disabled", "disabled");
        $("#btn_cancel").attr("disabled", "disabled");
        $("#btn_add").removeAttr("disabled");
    }
}

function clearDetails() {
    $("#id").val("");
    $("#fname").val("");
    $("#lname").val("");
    $("#phone").val("");
    $("#email").val("");
    $("#role").val("");
}
function updateDetails(id) {
    $.ajax({
        url: "../person/" + id,
        type: "GET",
        dataType: 'json',
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.getResonseText + ": " + textStatus);
        }
    }).done(function (person) {
        $("#id").val(person.id);
        $("#fname").val(person.firstName);
        $("#lname").val(person.lastName);
        $("#phone").val(person.phone);
        $("#email").val(person.email);
        $("#role").val("");
        var roleStr = "";
        person.roles.forEach(function (roles){
            if(roles.roleName === "Teacher"){
                 roleStr += roles.roleName + ", " + roles.degree + ". ";
            }else if (roles.roleName === "Student"){
                roleStr += roles.roleName + ", " + roles.semester + ". ";
            }
            else if (roles.roleName === "AssistentTeacher"){
                roleStr += roles.roleName;
            }
        });
        $("#role").val(roleStr);
    });
    $("#delete").removeAttr("disabled");
    $("#btn_addrole").removeAttr("disabled");
}

function fetchAll() {
    $.ajax({
        url: "../person",
        type: "GET",
        dataType: 'json',
        error: function (jqXHR, textStatus, errorThrown) {
            alert(textStatus);
        }
    }).done(function (persons) {
        var options = "";
        persons.forEach(function (person) {
            options += "<option id=" + person.id + ">" + person.firstName[0] + ", " + person.lastName + "</option>";
        });
        $("#persons").html(options);
        clearDetails();
    });
}