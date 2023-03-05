$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });

    $("#test").click(function() {
        executeGetRequest('https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/company')
        return false;
    });
});

function executeGetRequest(url) {
    $.ajax({
        type: 'get',
        url: url,
        success: function (data) {
            alert('Auth working')
            console.log(data)
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