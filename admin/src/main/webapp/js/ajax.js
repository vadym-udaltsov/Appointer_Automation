function executeRequest(data, url, method) {
  $('div.modal.fade').modal('hide');
  $.ajax({
    type: method,
    url: url,
    contentType: "application/json",
    dataType: 'JSON',
    data: data,
    success: function (data) {
      if (localStorage.getItem('createDepartmentButtonClicked') == true) {
        if(document.getElementById('spinner_loading').style.display == 'flex') {
            document.getElementById('spinner_loading').style.display = 'none';
            document.getElementById('global_container_page').style.display = 'block';
        }
        $("#department_CreateModal").modal("hide");
        document.getElementById('notRegisteredContainer').style.display = 'none';
        document.getElementById('department_CreatePopup').disabled = true;
        localStorage.setItem('createBtnClicked', 'true');
      } else {
          location.reload();
      }
    },
    error: function (data) {
        if(document.getElementById('spinner_loading').style.display == 'flex') {
            document.getElementById('spinner_loading').style.display = 'none';
            document.getElementById('global_container_page').style.display = 'block';
        }
      $('div.modal.fade').modal('hide');
      if (data.responseText == undefined || data.responseText == "") {
        showDataError("Unsuccessful operation");
        location.reload();
      } else {
        showDataError(JSON.parse(data.responseText).body);
      }
    }
  });
}

function executePost(data, url) {
  document.getElementById('spinner_loading').style.display = 'flex';
  document.getElementById('global_container_page').style.display = 'none';
  executeRequest(data, url, 'post');
}

function executePut(data, url) {
  executeRequest(data, url, 'put');
}

function updateToken(data, url) {
  document.getElementById('spinner_loading').style.display = 'flex';
  document.getElementById('global_container_page').style.display = 'block';
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