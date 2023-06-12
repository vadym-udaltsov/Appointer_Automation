$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var select = $("#service_Create-depNameSelect");
    var updateNameSelect = $("#update-service_servNameDropdown");
    var deleteNameSelect = $("#delete-service_NameSelect");
    var updateDepSelect = $("#update-service_depNameSelect");
    var deleteDepSelect = $("#delete-service_depNameSelect");

    select.append('<option value="Loading...">Loading...</option>');
    updateNameSelect.append('<option value="Loading...">Loading...</option>');
    updateDepSelect.append('<option value="Loading...">Loading...</option>');
    deleteDepSelect.append('<option value="Loading...">Loading...</option>');
    deleteNameSelect.append('<option value="Loading...">Loading...</option>');

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    var pastName = "";

    $("#service_createOpenBtn").click(function() {
        loadDepartments(url, select, updateNameSelect);
    });

    $("#service_updateOpenBtn").click(function() {
        loadDepartments(url, updateDepSelect, updateNameSelect)
            .then(function() {
                pastName = JSON.parse($('#update-service_servNameDropdown').val()).name;
                populateData(pastName);
            })
            .catch(function(error) {
                console.error(error);
            });
    });

    $("#service_deleteOpenBtn").click(function() {
        loadDepartments(url, deleteDepSelect, deleteNameSelect);
    });

    $('#update-service_servNameDropdown').change(function() {
      pastName =  $('option:checked', '#update-service_servNameDropdown').text();
      populateData(pastName);
    });

    $("#service_CreateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#service_Create-servNameInput").val();
        service.duration = $("#service_Create-servDurationInput").val();
        service.price = $("#service_Create-servPriceInput").val();
        request.customer = email;
        request.department = $("#service_Create-depNameSelect").text();
        request.service = service;
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        $("#service_CreateModal").modal("hide");
        return false;
    });

    $("#update-service_UpdateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#update-service_newNameInput").val();
        service.duration = $("#update-service_newDurationInput").val();
        service.price = $("#update-service_newPriceInput").val();
        request.departmentId = $("#update-service_depNameSelect").val();
        request.serviceName = pastName;
        request.service = service;
        executePut(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        $("#service_UpdateModal").modal("hide");
        return false;
    });

    $("#delete-service_DeleteBtn").click(function() {
        var request = new Object();
        request.departmentId = $("#delete-service_depNameSelect").val();
        request.serviceName = $('option:checked', "#delete-service_NameSelect").text();
        executeDelete(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        $("#service_DeleteModal").modal("hide");
        return false;
    });
});

 /*Loading Service Data for Update */
function populateData(selectedName) {
    var data = JSON.parse($('#update-service_servNameDropdown').val());
    var selectedObject = null;
    if (data.name === selectedName) {
        selectedObject = data;
    }
    if (selectedObject !== null) {
        $("#update-service_newNameInput").val(selectedObject.name);
        $('#update-service_newDurationInput').val(selectedObject.duration);
        $('#update-service_newPriceInput').val(selectedObject.price);
    }
}

function loadDepartments(url, select, updateNameSelect) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function(data, jqXHR) {
                select.empty();
                updateNameSelect.empty();
                if (jqXHR !== "success") {
                    window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
                }
                $.each(data.customerDepartments, function() {
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
                document.getElementById("update-service_newNameInput").value = firstServiceOfFirstDepartment.name;
                document.getElementById("update-service_newDurationInput").value = firstServiceOfFirstDepartment.duration;
                document.getElementById("update-service_newPriceInput").value = firstServiceOfFirstDepartment.price;

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

