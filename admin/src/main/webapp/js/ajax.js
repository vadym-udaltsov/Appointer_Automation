function executePost(data, url) {
    $.ajax({
        type: 'post',
        url: url,
        contentType: "application/json",
        dataType: 'JSON',
        data: data,
        success: function (data) {
            if(localStorage.getItem('createDepartmentButtonClicked') === true) {
                $("#department_CreateModal").modal("hide");
                document.getElementById('notRegisteredContainer').style.display = 'none';
                document.getElementById('department_CreatePopup').disabled = true;
            } else {
                setTimeout(function() {
                     location.reload();
                }, 500);
            }
        },
        error: function (data) {
            $('div.modal.fade').modal('hide');
            showDataError(JSON.parse(data.responseText).body);
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
            setTimeout(function() {
                location.reload();
            }, 500);
        },
        error: function (data) {
            $('div.modal.fade').modal('hide');
            showDataError(JSON.parse(data.responseText).body);
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
            setTimeout(function() {
                location.reload();
            }, 500);
        },
        error: function (data) {
            $('div.modal.fade').modal('hide');
            showDataError(JSON.parse(data.responseText).body);
        }
    });
}

function showDataError(text) {
        var element = document.getElementById('notRegisteredContainer');
        element.style.display = 'block';
        element.textContent = text;
}