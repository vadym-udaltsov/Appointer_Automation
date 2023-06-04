$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var typeSelect = $("#depTypeSelect");
    var depNameSelect = $("#depNameSelect");
    var timeZoneSelect = $("#timeZoneSelect");

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    loadDepartmentData(url, typeSelect, depNameSelect);

    $("#serviceRef").click(function() {
        $(location).attr('href', 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/service.html');
        return false;
    });

    $("#update").click(function() {
    const updateButton = document.getElementById('update');
          if (updateButton.classList.contains('disabled')) {
                  return false;
              }
        var department = new Object();
        department.departmentName = $("#depName").val() == "" ? "Default" : $("#depName").val();
        department.type = $("#typeSelect").val();
        department.timeZone = $("#timeZoneSelect").val();
        department.startWork = $("#startWork").val();
        department.finishWork = $("#finishWork").val();

        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        return false;
    });

     $("#create").click(function() {
     const createButton = document.getElementById('create');
      if (createButton.classList.contains('disabled')) {
              return false;
          }
        var department = new Object();
        department.departmentName = $("#depName").val();
        department.email = email;
        department.type = $("#typeSelect").val();
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        return false;
     });
});

function loadDepartmentData(url, typeSelect, depNameSelect) {
    $.ajax({
        url: url,
        type: 'get',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            $.each(data.availableTypes, function () {
                var type = $("<option value='" + this + "'></option>").text(this);
                typeSelect.append(type);
            });
            $.each(data.customerDepartments, function () {
                var name = $("<option value='" + this.id + "'></option>").text(this.id);
                depNameSelect.append(name);
            });
            $("#depNameUpdate").defaultValue = "jijij";
        }
    });
}
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

function executeGetRequest(url) {
    $.ajax({
        type: 'get',
        url: url,
        success: function (data) {
            console.log(data)
            return data;
        },
        error: function (data) {
            alert('Auth not working')
            console.log(data)
        },
        complete: function (data) {
            console.log(data)
        }
    });
}

function activateButton() {
  $('#department_link').addClass('btn-active');
}








