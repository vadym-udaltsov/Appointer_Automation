document.addEventListener("DOMContentLoaded", function() {
  const closeBtn = document.querySelector(".close_notRegisteredMessage");
  const alertBox = document.querySelector(".notRegisteredMessage");

  closeBtn.addEventListener("click", function() {
    alertBox.style.display = "none";
  });
});