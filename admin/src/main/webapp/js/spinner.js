document.addEventListener('DOMContentLoaded', function() {
  var spinnerContainer = document.getElementById('spinner_loading');

  spinnerContainer.style.display = 'flex';

  window.addEventListener('load', function() {
    spinnerContainer.style.display = 'none';
  });
});