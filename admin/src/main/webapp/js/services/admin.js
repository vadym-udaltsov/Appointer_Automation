$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;

    var tableContainer = document.getElementById('adminTable');

    tableContainer.addEventListener('click', (event) => {
        if (event.target.matches('.sub-button.admin_deleteOpenBtn')) {
            const buttonValue = event.target.value;
            loadAdminData(url, JSON.parse(buttonValue), document.getElementById("delete-admin_PhoneInput"))
        }
    });

    $("#admin_CreateBtn").click(function() {
        var request = new Object();
        request.dn = $('option:checked','#department_NameSelect').text();
        request.cn = JSON.parse($('option:checked','#department_NameSelect').val()).c;
        request.pn = $("#admin_CreatePhoneInput").val();
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/admins');
        return false;
    });

    $("#delete-admin_DeleteBtn").click(function() {
        var request = new Object();
        request.dn = $('option:checked','#department_NameSelect').text();
        request.cn = JSON.parse($('option:checked','#department_NameSelect').val()).c;
        request.pn = $("#delete-admin_PhoneInput").val();
        executeDelete(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/admins');
        return false;
    });
});

function loadAdminData(url, value, nameSelect) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function(data, jqXHR) {
                if (jqXHR !== "success") {
                    window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
                }
                nameSelect.value = value;
                resolve();
            },
            error: function(data, jqXHR) {
                if (data.status === 0) {
                    window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
                }
                reject("Ошибка при выполнении запроса");
            }
        });
    });
}

