$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');
    alert('Service page');

    $("#serviceRef").click(function() {
        $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/service.html');
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