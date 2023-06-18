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
        request.cn = JSON.parse(localStorage.getItem('selectedOption')).c;
        request.pn = $("#admin_CreatePhoneInput").val();
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/admins');
        setTimeout(function() {
            $("#admin_CreateModal").modal("hide");
            location.reload();
         }, 500);
        return false;
    });

    $("#delete-admin_DeleteBtn").click(function() {
        var request = new Object();
        request.dn = $('option:checked','#department_NameSelect').text();
        request.cn = JSON.parse($('option:checked','#department_NameSelect').val()).c;
        request.pn = $("#delete-admin_PhoneInput").val();
        executeDelete(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/admins');
        setTimeout(function() {
            $("#admin_DeleteModal").modal("hide");
            location.reload();
        }, 500);
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

                console.log(data);
                console.log(jqXHR);

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
           if (data.status === 0) {
         window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
      }
        }
    });
}

