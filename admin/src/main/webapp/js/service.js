$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');
    var customerData = JSON.parse(window.localStorage.getItem('customerData'));
    var select = $("#depNameSelect");
    loadDepartments(select, customerData);

    $("#departmentRef").click(function() {
        $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/department.html');
        return false;
    });

    $("#specialistRef").click(function() {
        $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/specialist.html');
        return false;
    });

    $("#adminRef").click(function() {
        $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/admin.html');
        return false;
    });

    $("#servCreateBtn").click(function() {
        var request = new Object();
        var service = new Object();
        service.name = $("#servName").val();
        service.duration = $("#servDuration").val();
        service.price = $("#servPrice").val();
        request.customer = email;
        request.department = $("#depNameSelect").val();
        request.service = service;
        executePost(JSON.stringify(request), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/service');
        return false;
    });
});

function loadDepartments(select, data) {
    $.each(data.customerDepartments, function () {
        var opt = $("<option value='" + this.n + "'></option>").text(this.n);
        select.append(opt);
    });
}

