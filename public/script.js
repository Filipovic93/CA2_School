$(document).ready(function () {

    fetchAll();
    deletePerson();
    initPersons();
    initAddBtn();
    initCancelBtn();
    initSaveBtn();
    showRole();
    roleevent();
    addRoleToPerson();

});

function showRole() {
    $("#btn_addrole").click(function () {
        $("#roleform").removeAttr("hidden");
        $('#roleform').removeClass("hidden");
    });
}


function addRoleToPerson() {
    $('#btn_saverole').click(function () {
        var newRole;
        var roleName = $('#dropdown option:selected').text();
        if (roleName === "Teacher") {
            newRole = {"degree": $('#roletxt').val(), "roleName": roleName};
        } else if (roleName === "Student") {
            newRole = {"semester": $('#roletxt').val(), "roleName": roleName};
        } else if (roleName === "AssistentTeacher") {
            newRole = {"roleName": roleName};
        }

        $.ajax({
            url: "../role/" + $("#persons option:selected").attr("id"),
            data: JSON.stringify(newRole), //Convert newRole to JSON
            type: "post",
            dataType: 'json',
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText + ": " + textStatus);
            }
        }).done(function (newRole) {
            $("#role").val(newRole.roleName);
            initDetails(false);
            fetchAll();
            $('#roletxt').val("");
            $("#roleform").addClass("hidden");
        });
    });
}

function roleevent() {
    $("#dropdown").change(function () {
        var selected = $("#dropdown option:selected").text();
        if (selected === "Teacher") {
            $("#roletxt").show();
            $("#roletxt").attr("placeholder", "Enter degree");
        }
        else if (selected === "Student") {
            $("#roletxt").show();
            $("#roletxt").attr("placeholder", "Enter semester");
        }
        else if (selected === "AssistentTeacher") {
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
        person.roles.forEach(function (roles) {
            if (roles.roleName === "Teacher") {
                roleStr += roles.roleName + ", " + roles.degree + ". ";
            } else if (roles.roleName === "Student") {
                roleStr += roles.roleName + ", " + roles.semester + ". ";
            }
            else if (roles.roleName === "AssistentTeacher") {
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