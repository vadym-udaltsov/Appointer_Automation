$(window).ready(function () {

    $("#verify").click(function() {
           var email = $("#verifyEmailValue").val();
           console.log(email)
           var params = new Object();
           var body = new Object();
           var additionalParams = new Object();
           executeGet('https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/email-verify?email=' + email)
           return false;
    });

    $("#register").click(function() {
           $(location).attr('href', 'https://appointer.auth.eu-central-1.amazoncognito.com/signup?client_id=' + userPoolClientId +'&response_type=code&scope=email+openid+phone&redirect_uri=https%3A%2F%2Fappointer-ui.s3.eu-central-1.amazonaws.com%2Fhtml%2Flogin.html');
           return false;
    });

    $("#log").click(function() {
        var customer = new Object();
        customer.email = $("#email").val();
        customer.password = $("#pass").val();
        executePost(JSON.stringify(customer), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/auth')
        return false;
    });
});

function executeGet(url) {
    $.ajax({
        type: 'get',
        url: url,
        contentType: "application/json",
        success: function (data) {
            alert('Please, check your email and follow the confirmation link.')
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
                error.textContent = "Login or password is wrong"
                error.style.color = "red"
            } else {
                localStorage.setItem('token', data.body);
                $(location).attr('href', 'https://appointer-ui.s3.eu-central-1.amazonaws.com/html/dashboard.html');
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