$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');
    var customerData = JSON.parse(window.localStorage.getItem('customerData'));
    var select = $("#specialist_depNameSelect");

    select.append('<option value="Loading...">Loading...</option>');
    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    loadDepartments(url, select, customerData);

    $("#specialist_create").click(function() {
                loadDepartments(url, select, customerData);
    });

    $("#specialist_servCreateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#specialist_servName").val();
        service.duration = $("#specialist_servDuration").val();
        service.price = $("#specialist_servPrice").val();
        request.customer = email;
        request.department = $("#specialist_depNameSelect").val();
        request.service = service;
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        $("#specialistModal").modal("hide");
        return false;
    });
});

function loadDepartments(url, select, data) {
    $.ajax({
        url: url,
        type: 'get',
        dataType: 'json',
        success: function (data) {
            select.empty();
            $.each(data.customerDepartments, function () {
                    var opt = $("<option value='" + this.n + "'></option>").text(this.n);
                    select.append(opt);
            });
        },
        error: function (data) {
            if (data.status === 0) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

