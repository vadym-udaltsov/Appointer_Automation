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
            if (data.status === 0) {
                error.textContent = "Request is not authorized";
                error.style.color = "red";
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            } else {
//                $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/dashboard.html');
            }
        },
        error: function (data) {
            if (data.status === 0) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        },
        complete: function (data) {
            if (data.status === 0) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
            console.log(data);
            console.log(data.status);
        }
    });
}