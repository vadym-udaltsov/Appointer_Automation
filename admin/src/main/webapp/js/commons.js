function executePost(data, url) {
    $.ajax({
        type: 'post',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data, jqXHR) {
            if (jqXHR !== "success") {
                var error = document.getElementById("error")
                error.textContent = "Request is not authorized";
                error.style.color = "red";
//                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            } else {
                console.log(data);
                console.log(jqXHR);
            }
        },
        error: function (data, jqXHR) {
            if (jqXHR !== "success") {
 //               window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

function validatePhoneNumber(input) {
  input.value = input.value.replace(/[^0-9+]/g, '');
}