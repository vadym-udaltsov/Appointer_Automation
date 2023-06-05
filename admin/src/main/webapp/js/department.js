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
    const updateButton = document.getElementById('update');
        if (updateButton.classList.contains('disabled')) {
            return false;
        }
        var department = new Object();
        var id = $("#depNameSelect").val();
        var name = $("#depNameSelect").text();

        department.id = id;
        department.n = name;
        department.c = email;
        department.tp = $("#typeSelect").val();
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
            $.each(data.customerDepartments, function () {
                var name = $("<option value='" + this.id + "'></option>").text(this.n);
                depNameSelect.append(name);
            });
            var selectedZone = data.customerDepartments[0].zone;
            if (selectedZone != "") {
                var selectedZoneOption = $("<option value='" + selectedZone + "selected disabled hidden'></option>").text("Select zone");
                timeZoneSelect.append()
            }
            document.getElementById("timeZoneSelect").value = data.customerDepartments[0].n;
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








