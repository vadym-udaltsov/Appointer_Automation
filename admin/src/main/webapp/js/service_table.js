$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });

    var email = localStorage.getItem('customer');
    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    loadServiceData(url);


    document.querySelector('#service_CreateBtn').addEventListener('click', function() {
        var service = new Object();
        service.name = $("#service_Create-servNameInput").val();
        service.duration = $("#service_Create-servDurationInput").val();
        service.price = $("#service_Create-servPriceInput").val();

        var newServiceBlock = document.createElement('div');
        newServiceBlock.classList.add('service');
        newServiceBlock.setAttribute('value', service);

        var nameDiv = document.createElement('div');
        nameDiv.classList.add('cell');
        nameDiv.textContent = service.name;

        var durationDiv = document.createElement('div');
        durationDiv.classList.add('cell');
        durationDiv.textContent = service.duration

        var priceDiv = document.createElement('div');
        priceDiv.classList.add('cell');
        priceDiv.textContent = service.price

        var actionsDiv = document.createElement('div');
        actionsDiv.classList.add('cell', 'actions');

        var updateButton = document.createElement('button');
        updateButton.setAttribute("type", "button");
        updateButton.className = "sub-button service_updateOpenBtn";
        updateButton.setAttribute("data-toggle", "modal");
        updateButton.setAttribute("data-target", "#service_UpdateModal");
        updateButton.setAttribute("value", JSON.stringify(service));
        updateButton.textContent = 'Update';

        var deleteButton = document.createElement('button');
        deleteButton.setAttribute("type", "button");
        deleteButton.className = "sub-button service_deleteOpenBtn";
        deleteButton.setAttribute("data-toggle", "modal");
        deleteButton.setAttribute("data-target", "#service_DeleteModal");
        deleteButton.setAttribute("value", JSON.stringify(service));
        deleteButton.textContent = 'Delete';

        actionsDiv.appendChild(updateButton);
        actionsDiv.appendChild(deleteButton);

        newServiceBlock.appendChild(nameDiv);
        newServiceBlock.appendChild(durationDiv);
        newServiceBlock.appendChild(priceDiv);
        newServiceBlock.appendChild(actionsDiv);

        var column = document.querySelector('.columns_titles');
        var lastService = column.parentElement.querySelector('.service:last-of-type');
        if (lastService) {
          lastService.insertAdjacentElement('afterend', newServiceBlock);
        } else {
          column.insertAdjacentElement('afterend', newServiceBlock);
        }

    });

    var deleteButtons = document.querySelectorAll('.delete');

    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            var column = this.closest('.service');
            column.remove();
        });
    });

    var column = document.querySelector('.columns_titles');

    column.addEventListener('click', function(event) {
        if (event.target.classList.contains('delete')) {
            var service = event.target.closest('.service');
            service.remove();
        }
    });
});

function loadServiceData(url) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            success: function(data, jqXHR) {
               var table = $("#servicesTable");
                $.each(data.customerDepartments, function(i, department) {
                    $.each(department.s, function(j, item) {
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

                        table.append(row);
                    });
                });
                resolve();
            },
            error: function(data, jqXHR) {
                reject("Ошибка при выполнении запроса");
            }
        });
    });
}
