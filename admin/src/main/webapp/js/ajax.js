function executeRequest(data, url, method) {
  $('div.modal.fade').modal('hide');
  $.ajax({
    type: method,
    url: url,
    contentType: "application/json",
    dataType: 'JSON',
    data: data,
    success: function (data) {
      if (localStorage.getItem('createButtonClicked') == true) {
        $("#department_CreateModal").modal("hide");
        document.getElementById('notRegisteredContainer').style.display = 'none';
        document.getElementById('department_CreatePopup').disabled = true;
        localStorage.setItem('createBtnClicked', 'true');
      } else {
          location.reload();
      }
    },
    error: function (data) {
      $('div.modal.fade').modal('hide');
      if (data.responseText == undefined) {
        showDataError("Unsuccessful operation");
      } else {
        showDataError(JSON.parse(data.responseText).body);
      }
    }
  });
}

function executePost(data, url) {
  executeRequest(data, url, 'post');
}

function executePut(data, url) {
  executeRequest(data, url, 'put');
}

function executeDelete(data, url) {
  executeRequest(data, url, 'delete');
}

function showDataError(text) {
    var element = document.getElementById('notRegisteredContainer');
    element.style.display = 'block';
    element.textContent = text;

    setTimeout(function() {
        element.classList.add('fadeOut');
        element.style.display = 'none';
    }, 10000);
}