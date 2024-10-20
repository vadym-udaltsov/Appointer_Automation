$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;

    var tableContainer = document.getElementById('specialistTable');
    tableContainer.addEventListener('click', (event) => {
        if (event.target.matches('.sub-button.specialist_updateOpenBtn')) {
            const buttonValue = event.target.value;
            loadSpecialistData(url, JSON.parse(buttonValue), document.getElementById("update-specialist_servNameInput"))
        }
    });

    tableContainer.addEventListener('click', (event) => {
        if (event.target.matches('.sub-button.specialist_deleteOpenBtn')) {
            const buttonValue = event.target.value;
            loadSpecialistData(url, JSON.parse(buttonValue), document.getElementById("delete-specialist_NameInput"))
        }
    });

    $("#specialist_CreateBtn").click(function() {
        var request = new Object();
        var specialist = new Object();
        specialist.name = $("#specialist_CreateNameInput").val();
        specialist.pn = $("#specialist_CreatePhoneInput").val();
        request.departmentId = JSON.parse($('option:checked','#department_NameSelect').val()).id;
        request.specialist = specialist;
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/specialist');
        return false;
    });

    $("#update-specialist_UpdateBtn").click(function() {
        var request = new Object();
        var specialist = new Object();
        specialist.name = $("#update-specialist_newNameInput").val();
        specialist.pn = $("#update-specialist_newPhoneInput").val();
        request.departmentId = JSON.parse($('option:checked','#department_NameSelect').val()).id;
        request.specialistName = document.getElementById("update-specialist_servNameInput").value;
        request.specialist = specialist;
        executePut(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/specialist');
        return false;
    });

    $("#delete-specialist_DeleteBtn").click(function() {
        var request = new Object();
        request.departmentId = JSON.parse($('option:checked','#department_NameSelect').val()).id;
        request.specialistName = $("#delete-specialist_NameInput").val();
        executeDelete(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/specialist');
        return false;
    });
});

function loadSpecialistData(url, value, nameSelect) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function(data) {
                nameSelect.value = value.name;
                document.getElementById("update-specialist_newNameInput").value = value.name;
                document.getElementById("update-specialist_newPhoneInput").value = value.pn;
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

