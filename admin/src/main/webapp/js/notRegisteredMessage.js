document.addEventListener("DOMContentLoaded", function() {
  const closeBtn = document.querySelector(".close_notRegisteredMessage");
  const alertBox = document.querySelector(".notRegisteredMessage");

  closeBtn.addEventListener("click", function() {
    alertBox.style.display = "none";
  });

    const closeMessageBtn = document.querySelector(".close_waitMessage");
    const alertWaiterBox = document.querySelector(".waitMessage");

    closeMessageBtn.addEventListener("click", function() {
        alertWaiterBox.style.display = "none";
    });
});