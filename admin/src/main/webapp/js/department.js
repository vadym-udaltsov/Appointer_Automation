$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var typeSelect = $("#depTypeSelect");
    var depNameSelect = $("#depNameSelect");
    var timeZoneSelect = $("#timeZoneSelect");

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    loadDepartmentData(url, typeSelect, depNameSelect, timeZoneSelect);

    $("#update").click(function() {
    var updateButton = document.getElementById('update');
        if (updateButton.classList.contains('disabled')) {
            return false;
        }
        var department = new Object();
        var id = $("#depNameSelect").val();
        var name = $("#depNameSelect option:selected").text().trim();

        department.id = id;
        department.n = name;
        department.c = email;
        department.tp = $("#depTypeSelect").val();
        department.zone = $("#timeZoneSelect").val();
        department.sw = $("#startWork").val();
        department.ew = $("#finishWork").val();
        var checkboxes = document.getElementsByName("dayCheck");
        const days = [];
        for (var i = 0; i < checkboxes.length; i++) {
            if(checkboxes[i].checked) {
                days.push(checkboxes[i].value);
            }
        }
        department.nwd = days;
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        $("#updateModal").modal("hide");
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
        department.type = $("#depTypeSelect").val();
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        $("#createModal").modal("hide");
        return false;
     });
});

function loadDepartmentData(url, typeSelect, depNameSelect, timeZoneSelect) {
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
             $.each(data.availableZones, function () {
                var timeZone = $("<option value='" + this.id + "'></option>").text(this.title);
                timeZoneSelect.append(timeZone);
            });
            $.each(data.customerDepartments, function () {
                var name = $("<option value='" + this.id + "'></option>").text(this.n);
                depNameSelect.append(name);
            });
            document.getElementById("depNameSelect").value = data.customerDepartments[0].id;
            document.getElementById("timeZoneSelect").value = data.customerDepartments[0].zone;
            document.getElementById("depTypeSelect").value = data.customerDepartments[0].tp;
            document.getElementById("startWork").value = data.customerDepartments[0].sw;
            document.getElementById("finishWork").value = data.customerDepartments[0].ew;
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








