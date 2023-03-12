$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');
    executeGetRequest('https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/customer?email=' + email);

    $("#servCreateBtn").click(function() {
        var service = new Object();
        service.name = $("#servName").val();
        service.duration = $("#servDuration").val();
        service.price = $("#servPrice").val();
        executeGetRequest('https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/company');
        return false;
    });
});

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