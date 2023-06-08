var logoutButton = document.getElementById("logout_button");
logoutButton.addEventListener("click", function() {
  window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html';
});