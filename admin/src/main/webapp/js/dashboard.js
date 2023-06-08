$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });

    var email = localStorage.getItem('customer');
    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    loadCustomerData(url);

    $("#depCreateBtn").click(function() {
        var department = new Object();
        department.departmentName = $("#depName").val();
        department.email = email;
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        return false;
    });

    $("#servCreateBtn").click(function() {
        var service = new Object();
        service.name = $("#specialist_servName").val();
        service.duration = $("#specialist_servDuration").val();
        service.price = $("#specialist_servPrice").val();
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
        },
        error: function (data) {
            if (data.status === 401) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
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
            if (data.status === 401) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        },
        complete: function (data) {
            console.log(data)
        }
    });
}