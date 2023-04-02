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
                error.textContent = "Request is not authorized"
                error.style.color = "red"
            } else {
//                $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/dashboard.html');
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