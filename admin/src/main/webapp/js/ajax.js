function executePost(data, url) {
    $.ajax({
        type: 'post',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data) {
            console.log(data);
        },
        error: function (data) {
        console.log("Body from error: " +  data.responseText)
        console.log("Code from error: " +  data.status)
            if (data.status === 0) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

function executePut(data, url) {
    $.ajax({
        type: 'put',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data) {
            console.log(data);
        },
        error: function (data) {
         console.log("Body from error: " +  data.responseText)
         console.log("Code from error: " +  data.status)
            if (data.status === 0) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

function executeDelete(data, url) {
    $.ajax({
        type: 'delete',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data) {
            console.log(data);
        },
        error: function (data) {
            if (data.status === 0) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}