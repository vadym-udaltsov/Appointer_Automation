$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var select = $("#specialist_depNameSelect");
    var updateNameSelect = $("#service_updateName-dropdown");
    var deleteNameSelect = $("#delete_updateName-dropdown");
    var updateDepSelect = $("#update_depNameSelect");
    var deleteDepSelect = $("#delete_depNameSelect");

    select.append('<option value="Loading...">Loading...</option>');
    updateNameSelect.append('<option value="Loading...">Loading...</option>');
    updateDepSelect.append('<option value="Loading...">Loading...</option>');
    deleteDepSelect.append('<option value="Loading...">Loading...</option>');
    deleteNameSelect.append('<option value="Loading...">Loading...</option>');

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    var pastName = "";

    $("#service_create").click(function() {
        loadDepartments(url, select, updateNameSelect);
    });

    $("#service_update").click(function() {
        loadDepartments(url, updateDepSelect, updateNameSelect);
        pastName =  $('option:checked', '#service_updateName-dropdown').text();
        populateData(pastName);
    });

    $("#service_delete").click(function() {
        loadDepartments(url, deleteDepSelect, deleteNameSelect);
    });

    $('#service_updateName-dropdown').change(function() {
      pastName =  $('option:checked', '#service_updateName-dropdown').text();
      populateData(pastName);
    });

    $("#service_CreateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#specialist_servName").val();
        service.duration = $("#specialist_servDuration").val();
        service.price = $("#specialist_servPrice").val();
        request.customer = email;
        request.department = $("#specialist_depNameSelect").text();
        request.service = service;
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        $("#specialistModal").modal("hide");
        return false;
    });

    $("#update_servCreateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#service_updateName-input").val();
        service.duration = $("#service_updateDuration-input").val();
        service.price = $("#service_updatePrice-input").val();
        request.departmentId = $("#update_depNameSelect").val();
        request.serviceName = pastName;
        request.service = service;
        executePut(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        $("#serviceUpdateModal").modal("hide");
        return false;
    });

    $("#delete_serviceBtn").click(function() {
        var request = new Object();
        request.departmentId = $("#delete_depNameSelect").val();
        request.serviceName = $('option:checked', "#delete_updateName-dropdown").text();
        executeDelete(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        $("#serviceDeleteModal").modal("hide");
        return false;
    });
});

 /*Loading Service Data for Update */
function populateData(selectedName) {
    var data = JSON.parse($('#service_updateName-dropdown').val());
    var selectedObject = null;
    if (data.name === selectedName) {
        selectedObject = data;
    }
    if (selectedObject !== null) {
        $("#service_updateName-input").val(selectedObject.name);
        $('#service_updateDuration-input').val(selectedObject.duration);
        $('#service_updatePrice-input').val(selectedObject.price);
    }
}

function loadDepartments(url, select, updateNameSelect) {
    $.ajax({
        url: url,
        type: 'get',
        dataType: 'json',
        success: function (data, jqXHR) {
            select.empty();
            updateNameSelect.empty();
            if (jqXHR !== "success") {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
            $.each(data.customerDepartments, function () {
                    var opt = $("<option value='" + this.id + "'></option>").text(this.n);
                    select.append(opt);
            });
           $.each(data.customerDepartments, function(i, department) {
                $.each(department.s, function(j, item) {
                    var name = $("<option>").val(JSON.stringify(item)).text(item.name);
                    updateNameSelect.append(name);
               });
           });
           var firstServiceOfFirstDepartment = data.customerDepartments[0].s[0];
           document.getElementById("service_updateName-input").value = firstServiceOfFirstDepartment.name;
           document.getElementById("service_updateDuration-input").value = firstServiceOfFirstDepartment.duration;
           document.getElementById("service_updatePrice-input").value = firstServiceOfFirstDepartment.price;
            console.log(data);
            console.log(jqXHR);
        },
        error: function (data, jqXHR) {
            if (jqXHR !== "success") {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

function executePut(data, url) {
    $.ajax({
        type: 'put',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data, jqXHR) {
            if (jqXHR !== "success") {
                var error = document.getElementById("error")
                error.textContent = "Request is not authorized";
                error.style.color = "red";
//                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            } else {
                console.log(data);
                console.log(jqXHR);
            }
        },
        error: function (data, jqXHR) {
            if (jqXHR !== "success") {
//               window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

function executeDelete(data, url) {
    $.ajax({
        type: 'delete',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data, jqXHR) {
            if (jqXHR !== "success") {
                var error = document.getElementById("error")
                error.textContent = "Request is not authorized";
                error.style.color = "red";
//                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            } else {
                console.log(data);
                console.log(jqXHR);
            }
        },
        error: function (data, jqXHR) {
            if (jqXHR !== "success") {
//               window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

