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
            if (data.status === 0) {
                window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        }
    });
}

function validatePhoneNumber(input) {
  input.value = input.value.replace(/[^0-9+]/g, '');
  const inputValue = input.value.trim();

  if (inputValue.length === 0 || inputValue.charAt(0) !== '+') {
     input.value = '+' + inputValue;
  }
}