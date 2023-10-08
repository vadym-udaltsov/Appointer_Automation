document.addEventListener('DOMContentLoaded', function() {
  var spinnerContainer = document.getElementById('spinner_loading');
  spinnerContainer.style.display = 'flex';
  document.getElementById('global_container_page').style.display = 'none';

  window.onload = function() {
    spinnerContainer.style.display = 'none';
    document.getElementById('global_container_page').style.display = 'block';
  };
});