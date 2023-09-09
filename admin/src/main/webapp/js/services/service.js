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
            loadServiceData(url, JSON.parse(buttonValue), document.getElementById("update-service_servNameInput"))
        }
    });

    tableContainer.addEventListener('click', (event) => {
        if (event.target.matches('.sub-button.service_deleteOpenBtn')) {
            const buttonValue = event.target.value;
            loadServiceData(url, JSON.parse(buttonValue), document.getElementById("delete-service_NameInput"))
        }
    });

    $("#service_CreateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#service_Create-servNameInput").val();
        service.duration = $("#service_Create-servDurationInput").val();
        //service.price = $("#service_Create-servPriceInput").val();
        request.customer = email;
        request.department = $('option:checked','#department_NameSelect').text();
        request.service = service;
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        return false;
    });

    $("#update-service_UpdateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#update-service_newNameInput").val();
        service.duration = $("#update-service_newDurationInput").val();
        //service.price = $("#update-service_newPriceInput").val();
        request.departmentId = JSON.parse($('option:checked','#department_NameSelect').val()).id;
        request.serviceName = document.getElementById("update-service_servNameInput").value;
        request.service = service;
        executePut(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        return false;
    });

    $("#delete-service_DeleteBtn").click(function() {
        var request = new Object();
        request.departmentId = JSON.parse($('option:checked','#department_NameSelect').val()).id;
        request.serviceName = $("#delete-service_NameInput").val();
        executeDelete(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        return false;
    });
});

function loadServiceData(url, value, nameSelect) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function(data) {
                nameSelect.value = value.name;
                document.getElementById("update-service_newNameInput").value = value.name;
                document.getElementById("update-service_newDurationInput").value = value.duration;
                //document.getElementById("update-service_newPriceInput").value = value.price;
                resolve();
            },
            error: function(data) {
                if (data.status === 0) {
                    window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
                }
            reject("Ошибка при выполнении запроса");
            }
        });
    });
}

