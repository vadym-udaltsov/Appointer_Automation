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
            if (data.status === 401) {
                error.textContent = "Request is not authorized";
                error.style.color = "red";
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html';
            } else {
//                $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/dashboard.html');
            }
        },
        error: function (data) {
            console.log("Operation failed")
        },
        complete: function (data) {
            console.log(data);
            console.log(data.status);
        }
    });
}