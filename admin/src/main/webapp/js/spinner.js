document.addEventListener('DOMContentLoaded', function() {
  var spinnerContainer = document.getElementById('spinner_loading');
  spinnerContainer.style.display = 'flex';

  window.onload = function() {
    spinnerContainer.style.display = 'none';
  };
});