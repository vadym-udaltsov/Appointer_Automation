$(window).ready(function () {

    $('#register').prop('disabled', true);
    $("#verify").click(function() {
           var email = $("#verifyEmailValue").val();
           console.log(email)
           var params = new Object();
           var body = new Object();
           var additionalParams = new Object();
           executeGet('https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/email-verify?email=' + email)
           $('#spinner-container').html('<div class="spinner"></div>');
           return false;
    });

    $("#register").click(function() {
           $(location).attr('href', 'https://' + userPoolDomain + '.auth.eu-central-1.amazoncognito.com/signup?client_id=' + userPoolClientId +'&response_type=code&scope=email+openid+phone&redirect_uri=https%3A%2F%2F' + uiBucket + '.s3.eu-central-1.amazonaws.com%2Fhtml%2Flogin.html');
           return false;
    });

    $("#log").click(function() {
        var customer = new Object();
        customer.email = $("#email").val();
        customer.password = $("#pass").val();
        localStorage.setItem('customer', customer.email);
        console.log("put email to storage: " + customer.email);
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
            $('#register').prop('disabled', false);
        },
        error: function (data) {
            alert('Please, enter valid email.')
            $('#register').prop('disabled', true);
        },
        complete: function (data) {
            $('#spinner-container').empty();
        }
    });

}

function executePost(data, url) {
    $("#log").click(function() {
        $("#loadingOverlay").show();

        $(".sidenav").hide();
        $(".main").hide();
    });

    $.ajax({
        type: 'post',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data) {
            console.log(data)
            var error = document.getElementById("error")
            if (data.status === 401) {
                error.textContent = "Login or password is wrong"
                error.style.color = "red"
            } else {
                localStorage.setItem('token', data.body);
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

/*Alert*/

document.addEventListener("DOMContentLoaded", function() {
  var messageDiv = document.getElementById("messageDiv");

    messageDiv.style.display = "block";
    setTimeout(function() {
      messageDiv.style.display = "none";
    }, 4000);
});