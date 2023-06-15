$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var updateNameInput = $("#update-service_servNameInput");
    var deleteNameInput = $("#delete-service_NameInput");

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    var pastName = "";

    var tableContainer = document.getElementById('servicesTable');
    tableContainer.addEventListener('click', (event) => {
        if (event.target.matches('.sub-button.service_updateOpenBtn')) {
            const buttonValue = event.target.value;
            loadDepartments(url, JSON.parse(buttonValue), document.getElementById("update-service_servNameInput"))
        }
    });

    tableContainer.addEventListener('click', (event) => {
        if (event.target.matches('.sub-button.service_deleteOpenBtn')) {
            const buttonValue = event.target.value;
            loadDepartments(url, JSON.parse(buttonValue), document.getElementById("delete-service_NameInput"))
        }
    });

    $("#service_CreateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#service_Create-servNameInput").val();
        service.duration = $("#service_Create-servDurationInput").val();
        service.price = $("#service_Create-servPriceInput").val();
        request.customer = email;
        request.department = $('option:checked','#department_NameSelect').text();
        request.service = service;
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        setTimeout(function() {
            $("#service_CreateModal").modal("hide");
            location.reload();
         }, 500);
        return false;
    });

    $("#update-service_UpdateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#update-service_newNameInput").val();
        service.duration = $("#update-service_newDurationInput").val();
        service.price = $("#update-service_newPriceInput").val();
        request.departmentId = JSON.parse($("#department_NameSelect").val())[0].id;
        request.serviceName = document.getElementById("update-service_servNameInput").value;
        request.service = service;
        executePut(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        setTimeout(function() {
            $("#service_UpdateModal").modal("hide");
            location.reload();
        }, 500);
        return false;
    });

    $("#delete-service_DeleteBtn").click(function() {
        var request = new Object();
        request.departmentId = JSON.parse($("#department_NameSelect").val())[0].id;
        request.serviceName = $("#delete-service_NameInput").val();
        executeDelete(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        setTimeout(function() {
            $("#service_DeleteModal").modal("hide");
            location.reload();
        }, 500);
        return false;
    });
});

function loadDepartments(url, value, nameSelect) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function(data, jqXHR) {
                if (jqXHR !== "success") {
                    window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
                }

                nameSelect.value = value.name;
                document.getElementById("update-service_newNameInput").value = value.name;
                document.getElementById("update-service_newDurationInput").value = value.duration;
                document.getElementById("update-service_newPriceInput").value = value.price;

                console.log(data);
                console.log(jqXHR);

                resolve();
            },
            error: function(data, jqXHR) {
                if (jqXHR !== "success") {
                    window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
                }
                reject("Ошибка при выполнении запроса");
            }
        });
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
                console.log(jqXHR.status);
            }
        },
        error: function (data, jqXHR) {
            if (jqXHR !== "success") {
//               window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

