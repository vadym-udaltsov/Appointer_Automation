$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var typeSelect = $("#update_depTypeSelect");
    var choose_depNameSelect = $("#department_NameSelect");

    var update_timeZoneSelect = $("#update_timeZoneSelect");

    choose_depNameSelect.append('<option value="Loading...">Loading...</option>');
    update_timeZoneSelect.append('<option value="Loading...">Loading...</option>');
    typeSelect.append('<option value="Loading...">Loading...</option>');

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    window.dataLoaded;
    loadDepartmentData(url, typeSelect, choose_depNameSelect, update_timeZoneSelect);

    const selectElement = document.getElementById('department_NameSelect');

      document.getElementById('department_NameSelect').addEventListener('change', function() {
        var checkedSelect = $('option:checked','#department_NameSelect');
        loadDataFromSelectedDep(url, typeSelect, checkedSelect, update_timeZoneSelect);
      });

    $("#department_UpdatePopup").click(function() {
        loadDepUpdateData(url, typeSelect, choose_depNameSelect, update_timeZoneSelect);
    });

    $("#update_DepBtn").click(function() {
    var updateButton = document.getElementById('update_DepBtn');
        if (updateButton.classList.contains('disabled')) {
            return false;
        }
        var department = new Object();
        var id = JSON.parse($("#department_NameSelect").val()).id;
        var name = $("#department_NameSelect option:selected").text().trim();

        department.id = id;
        department.n = name;
        department.c = email;
        department.tp = $("#update_depTypeSelect").val();
        department.zone = $("#update_timeZoneSelect").val();
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
        $("#department_UpdateModal").modal("hide");
        location.reload();
        return false;
    });

     $("#create_depBtn").click(function() {
     const createButton = document.getElementById('create_depBtn');
      if (createButton.classList.contains('disabled')) {
              return false;
          }
        var department = new Object();
        department.departmentName = $("#depName").val();
        department.email = email;
        department.type = $("#update_depTypeSelect").val();
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        $("#department_CreateModal").modal("hide");
        return false;
     });
});

function loadCommonData(selectedDepartmentData) {
  document.getElementById("update_selectedDepartment").value = selectedDepartmentData.n;
  document.getElementById("update_timeZoneSelect").value = selectedDepartmentData.zone;
  document.getElementById("update_depTypeSelect").value = selectedDepartmentData.tp;
  document.getElementById("startWork").value = selectedDepartmentData.sw;
  document.getElementById("finishWork").value = selectedDepartmentData.ew;

  var checkedDateFromData = selectedDepartmentData.nwd;
  var daysCheckboxes = document.querySelectorAll('input[name="dayCheck"]');
  daysCheckboxes.forEach(function (checkbox) {
    if (checkedDateFromData.includes(parseInt(checkbox.value))) {
      checkbox.checked = true;
    }
  });

  var serviceTable = $("#servicesTable");
  serviceTable.find('.service').remove();
  $.each(selectedDepartmentData.s, function (j, item) {
    var row = $("<div class='service'></div>");
    var nameCell = $("<div class='service_name cell'></div>").text(item.name);
    var durationCell = $("<div class='service_duration cell'></div>").text(item.duration);
    var priceCell = $("<div class='service_price cell'></div>").text(item.price);
    var actionsCell = $("<div class='cell actions'></div>");

    var updateButton = $("<button type='button' class='sub-button service_updateOpenBtn' data-toggle='modal' data-target='#service_UpdateModal'>")
      .text("Update")
      .val(JSON.stringify(item));
    var deleteButton = $("<button type='button' class='sub-button service_deleteOpenBtn' data-toggle='modal' data-target='#service_DeleteModal'>")
      .text("Delete")
      .val(JSON.stringify(item));

    actionsCell.append(updateButton, deleteButton);

    row.append(nameCell, durationCell, priceCell, actionsCell);

    serviceTable.append(row);
  });

  var specialistTable = $("#specialistTable");
  specialistTable.find('.specialist').remove();
  $.each(selectedDepartmentData.as, function (j, item) {
    var row = $("<div class='specialist'></div>");
    var nameCell = $("<div class='specialist_name specCol'></div>").text(item.name);
    var durationCell = $("<div class='specialist_phone specCol'></div>").text(item.pn);
    var actionsCell = $("<div class='specCol actions'></div>");

    var updateButton = $("<button type='button' class='sub-button specialist_updateOpenBtn' data-toggle='modal' data-target='#specialist_UpdateModal'>")
      .text("Update")
      .val(JSON.stringify(item));
    var deleteButton = $("<button type='button' class='sub-button specialist_deleteOpenBtn' data-toggle='modal' data-target='#specialist_DeleteModal'>")
      .text("Delete")
      .val(JSON.stringify(item));

    actionsCell.append(updateButton, deleteButton);

    row.append(nameCell, durationCell, actionsCell);

    specialistTable.append(row);
  });

  var adminTable = $("#adminTable");
  adminTable.find('.admin').remove();
  $.each(selectedDepartmentData.adm, function (j, item) {
    var row = $("<div class='admin'></div>");
    var phoneCell = $("<div class='admin_phone admCol centerPos'></div>").text(item);
    var actionsCell = $("<div class='admCol actions  centerPos'></div>");

    var deleteButton = $("<button type='button' class='sub-button admin_deleteOpenBtn' data-toggle='modal' data-target='#admin_DeleteModal'>")
      .text("Delete")
      .val(JSON.stringify(item));

    actionsCell.append(deleteButton);

    row.append(phoneCell, actionsCell);

    adminTable.append(row);
  });

  window.dataLoaded = true;
}

function loadDepartmentData(url, typeSelect, choose_depNameSelect, update_timeZoneSelect, dataLoaded) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data, jqXHR) {
      choose_depNameSelect.empty();
      update_timeZoneSelect.empty();
      typeSelect.empty();

      console.log(data);
      $.each(data.customerDepartments, function (i, department) {
        var name = $("<option value='" + JSON.stringify(department) + "'></option>").text(this.n);
        choose_depNameSelect.append(name);
      });

      $.each(data.availableTypes, function () {
        var type = $("<option value='" + this + "'></option>").text(this);
        typeSelect.append(type);
      });

      $.each(data.availableZones, function () {
        var timeZone = $("<option value='" + this.id + "'></option>").text(this.title);
        update_timeZoneSelect.append(timeZone);
      });

      var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
      loadCommonData(selectedDepartmentData);
    },
    error: function (data, jqXHR) {
      if (jqXHR !== "success") {
        // window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
      }
    }
  });
}

function loadDepUpdateData(url, typeSelect, choose_depNameSelect, update_timeZoneSelect, dataLoaded) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data, jqXHR) {
      var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
      loadCommonData(selectedDepartmentData);
    },
    error: function (data, jqXHR) {
      if (jqXHR !== "success") {
        // window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
      }
    }
  });
}

function loadDataFromSelectedDep(url, typeSelect, choose_depNameSelect, update_timeZoneSelect, dataLoaded) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data, jqXHR) {
      var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
      loadCommonData(selectedDepartmentData);
    },
    error: function (data, jqXHR) {
      if (jqXHR !== "success") {
        // window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
      }
    }
  });
}

function executeGetRequest(url) {
    $.ajax({
        type: 'get',
        url: url,
        success: function (data, jqXHR) {
            console.log(data);
            console.log(jqXHR);
            return data;
        },
        error: function (data, jqXHR) {
            if (jqXHR !== "success") {
               // window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
            }
        },
    });
}

function activateButton() {
  $('#department_link').addClass('btn-active');
}

function validateInput(input) {
  input.value = input.value.replace(/\D/g, '');
}







