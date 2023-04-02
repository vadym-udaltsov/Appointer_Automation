$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });

    var email = localStorage.getItem('customer');
    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    loadCustomerData(url);

    $("#serviceRef").click(function() {
        $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/service.html');
        return false;
    });

    $("#departmentRef").click(function() {
        $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/department.html');
        return false;
    });

    $("#depCreateBtn").click(function() {
        var department = new Object();
        department.departmentName = $("#depName").val();
        department.email = email;
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        return false;
    });

    $("#servCreateBtn").click(function() {
        var service = new Object();
        service.name = $("#servName").val();
        service.duration = $("#servDuration").val();
        service.price = $("#servPrice").val();
        executeGetRequest('https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/company');
        return false;
    });
});

function loadCustomerData(url) {
    $.ajax({
        url: url,
        type: 'get',
        dataType: 'json',
        success: function (data) {
            localStorage.setItem('customerData', JSON.stringify(data));
        }
    });
}

function executePost(data, url) {
    $.ajax({
        type: 'post',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data) {
            console.log(data)
            var error = document.getElementById("error")
            if (data.statusCode === 401) {
                error.textContent = "Request is not authorized"
                error.style.color = "red"
            } else {
                $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/dashboard.html');
            }
        },
        error: function (data) {
            $(location).attr('href', '');
        },
        complete: function (data) {
            console.log(data);
        }
    });
}

function executeGetRequest(url) {
    $.ajax({
        type: 'get',
        url: url,
        success: function (data) {
            console.log(data)
            return data;
        },
        error: function (data) {
            alert('Auth not working')
            console.log(data)
        },
        complete: function (data) {
            console.log(data)
        }
    });
}