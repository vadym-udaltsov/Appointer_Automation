$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var select = $("#typeSelect");
    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    loadTypes(url, select);

    $("#serviceRef").click(function() {
        $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/service.html');
        return false;
    });

     $("#create").click(function() {
        var department = new Object();
        department.departmentName = $("#depName").val();
        department.email = email;
        department.type = $("#typeSelect").val();
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        return false;
     });
});

function loadTypes(url, select) {
    $.ajax({
        url: url,
        type: 'get',
        dataType: 'json',

        success: function (data) {
            console.log(data);
            $.each(data.availableTypes, function () {
                console.log(this)
                var opt = $("<option value='" + this + "'></option>").text(this);
                select.append(opt);
            });
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
//                $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/dashboard.html');
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