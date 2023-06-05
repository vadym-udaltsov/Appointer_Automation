$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');
    var customerData = JSON.parse(window.localStorage.getItem('customerData'));
    var select = $("#specialist_depNameSelect");
    //loadDepartments(select, customerData);

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
        return false;
    });
});

/*function loadDepartments(select, data) {
    $.each(data.customerDepartments, function () {
        var opt = $("<option value='" + this.n + "'></option>").text(this.n);
        select.append(opt);
    });
}*/

