var spinnerContainer = document.getElementById('spinner_loading');
var loginContainer = document.getElementById('login_container');
spinnerContainer.style.display = 'none';

$(window).ready(function () {
    $('#register').prop('disabled', true);

    $("#verify").click(function() {
           var email = $("#verifyEmailValue").val();
           console.log(email)
           var params = new Object();
           var body = new Object();
           var additionalParams = new Object();
           verifyEmail('https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/email-verify?email=' + email)
           $('#spinner-container').html('<div class="spinner"></div>');
           setTimeout(function() {
                document.getElementsByClassName('spinner')[0].style.display = 'none'
           }, 8000);
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
        login(JSON.stringify(customer), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/auth')
        return false;
    });
});

function verifyEmail(url) {
    $.ajax({
        type: 'get',
        url: url,
        contentType: "application/json",
        success: function (data) {
            alert('Please, check your email and follow the confirmation link.')
            document.getElementById('register').disabled = false;
        },
        error: function (data) {
            alert('Please, enter valid email.')
            document.getElementById('register').disabled = true;
        }
    });
}

function login(data, url) {
    loginContainer.style.display = 'none';
    spinnerContainer.style.display = 'flex';
    $.ajax({
        type: 'post',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data) {
            console.log(data)
            spinnerContainer.style.display = 'none';
            localStorage.setItem('token', data.body);
            $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/dashboard.html');
        },
        error: function (data) {
            spinnerContainer.style.display = 'none';
            loginContainer.style.display = 'block';
            if (data.responseText == undefined) {
                showDataError("Unsuccessful operation");
            } else {
                spinnerContainer.style.display = 'none';
                var errorPassword = document.getElementById("notValidPassword");
                errorPassword.style.display = 'block';
            }
        }
    });
}

/*Alert*/
document.addEventListener("DOMContentLoaded", function() {
  var messageDiv = document.getElementById("messageDiv");
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams.get("buttonClicked")) {
    urlParams.delete("buttonClicked");
    const newUrl = window.location.pathname + (urlParams.toString() ? "?" + urlParams.toString() : "");
    window.history.replaceState({}, "", newUrl);
    messageDiv.style.display = "block";
    setTimeout(function() {
      messageDiv.style.display = "none";
    }, 4000);
  } else {
    messageDiv.style.display = "none";
  }
});