$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });

    var email = localStorage.getItem('customer');
    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;

    document.querySelector('#specialist_CreateBtn').addEventListener('click', function() {
        var specialist = new Object();
        specialist.name = $("#specialist_CreateNameInput").val();
        specialist.pn = $("#specialist_CreatePhoneInput").val();

        var newSpecialistBlock = document.createElement('div');
        newServiceBlock.classList.add('specialist');
        newServiceBlock.setAttribute('value', specialist);

        var nameDiv = document.createElement('div');
        nameDiv.classList.add('specCol');
        nameDiv.textContent = specialist.name;

        var durationDiv = document.createElement('div');
        durationDiv.classList.add('specCol');
        durationDiv.textContent = specialist.phone

        var actionsDiv = document.createElement('div');
        actionsDiv.classList.add('specCol', 'actions');

        var updateButton = document.createElement('button');
        updateButton.setAttribute("type", "button");
        updateButton.className = "sub-button specialist_updateOpenBtn";
        updateButton.setAttribute("data-toggle", "modal");
        updateButton.setAttribute("data-target", "#specialist_UpdateModal");
        updateButton.setAttribute("value", JSON.stringify(specialist));
        updateButton.textContent = 'Update';

        var deleteButton = document.createElement('button');
        deleteButton.setAttribute("type", "button");
        deleteButton.className = "sub-button specialist_deleteOpenBtn";
        deleteButton.setAttribute("data-toggle", "modal");
        deleteButton.setAttribute("data-target", "#specialist_DeleteModal");
        deleteButton.setAttribute("value", JSON.stringify(specialist));
        deleteButton.textContent = 'Delete';

        actionsDiv.appendChild(updateButton);
        actionsDiv.appendChild(deleteButton);

        newSpecialistBlock.appendChild(nameDiv);
        newSpecialistBlock.appendChild(durationDiv);
        newSpecialistBlock.appendChild(actionsDiv);

        var column = document.querySelector('.columns_titles_specialist');
        var lastService = column.parentElement.querySelector('.specialist:last-of-type');
        if (lastService) {
          lastService.insertAdjacentElement('afterend', newSpecialistBlock);
        } else {
          column.insertAdjacentElement('afterend', newSpecialistBlock);
        }

    });

    var deleteButtons = document.querySelectorAll('.delete');

    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            var column = this.closest('.specialist');
            column.remove();
        });
    });

    var column = document.querySelector('.columns_titles_specialist');

    column.addEventListener('click', function(event) {
        if (event.target.classList.contains('delete')) {
            var service = event.target.closest('.specialist');
            service.remove();
        }
    });

    async function applyStyleAfterDataLoad() {
        while (window.dataLoaded !== true) {
            await new Promise(resolve => setTimeout(resolve, 200)); // Ждем 100 миллисекунд перед следующей проверкой
        }
            var tableElements = document.querySelectorAll('.specialist');
            var elementCount = tableElements.length;
            if (elementCount <= 4) {
                tableElements.forEach(function(element) {
                    element.style.borderBottom = '1px solid black';
                });
            } else {
                for (var i = 0; i < elementCount - 1; i++) {
                    tableElements[i].style.borderBottom = '1px solid black';
                }
           }
    }
    applyStyleAfterDataLoad();
});
