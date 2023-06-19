$(window).on('load', function() {
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
        var checkedSelect = this.value;
        localStorage.setItem('lastSelectedOption', checkedSelect);
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
    } else {
        checkbox.checked = false;
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

    var updateButton = $("<button type='button' class='sub-button service_updateOpenBtn lng-updateBtn' data-toggle='modal' data-target='#service_UpdateModal'>")
      .text("Update")
      .val(JSON.stringify(item));
    var deleteButton = $("<button type='button' class='sub-button service_deleteOpenBtn lng-deleteBtn' data-toggle='modal' data-target='#service_DeleteModal'>")
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

    var updateButton = $("<button type='button' class='sub-button specialist_updateOpenBtn lng-updateBtn' data-toggle='modal' data-target='#specialist_UpdateModal'>")
      .text("Update")
      .val(JSON.stringify(item));
    var deleteButton = $("<button type='button' class='sub-button specialist_deleteOpenBtn lng-deleteBtn' data-toggle='modal' data-target='#specialist_DeleteModal'>")
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

    var deleteButton = $("<button type='button' class='sub-button admin_deleteOpenBtn lng-deleteBtn' data-toggle='modal' data-target='#admin_DeleteModal'>")
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
    success: function (data) {
      choose_depNameSelect.empty();
      update_timeZoneSelect.empty();
      typeSelect.empty();

      console.log(data);
      var lastSelectedOption = JSON.parse(localStorage.getItem('lastSelectedOption'));

      $.each(data.customerDepartments, function (i, department) {
        var name = $("<option value='" + JSON.stringify(department) + "'></option>").text(this.n);

        if (lastSelectedOption && this.n === lastSelectedOption.n) {
          name.prop("selected", true);
        }

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
    error: function (data) {
      if (data.status === 0) {
         window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
      }
    }
  });
}

function loadDepUpdateData(url, typeSelect, choose_depNameSelect, update_timeZoneSelect, dataLoaded) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data) {
      var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
      loadCommonData(selectedDepartmentData);
    },
    error: function (data) {
        if (data.status === 0) {
            window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
        }
    }
  });
}

function loadDataFromSelectedDep(url, typeSelect, choose_depNameSelect, update_timeZoneSelect, dataLoaded) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data) {
      var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
      loadCommonData(selectedDepartmentData);
    },
    error: function (data) {
        if (data.status === 0) {
            window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
        }
    }
  });
}

function activateButton() {
  $('#department_link').addClass('btn-active');
}

document.getElementById('close_updateDepBtn').addEventListener('click', function () {
    var updateDepBtn = document.getElementById('update_DepBtn');
    updateDepBtn.disabled = false;
});

function validateInput(input) {
  input.value = input.value.replace(/\D/g, '');
}

function validateUpdateDepInput(input, inputType) {
  var value = parseInt(input.value, 10);

  if (value < 1) {
    input.value = 1;
  } else if (value > 24) {
    input.value = 24;
  }

  var startWork = parseInt(document.getElementById('startWork').value, 10);
  var finishWork = parseInt(document.getElementById('finishWork').value, 10);
  var updateDepBtn = document.getElementById('update_DepBtn');

  if (isNaN(startWork) || isNaN(finishWork) || startWork > finishWork) {
    updateDepBtn.disabled = true;
  } else if(startWork == '' || finishWork == '') {
    updateDepBtn.disabled = true;
  } else {
    updateDepBtn.disabled = false;
  }
}







