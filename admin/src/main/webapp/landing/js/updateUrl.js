function showOverlayAndSpinner() {
  const overlay = document.getElementById('overlay');
  overlay.style.display = 'block';
}

function hideOverlayAndSpinner() {
  const overlay = document.getElementById('overlay');
  setTimeout(() => {
    overlay.style.display = 'none';
  }, 1000);
}

function updateLinks() {
  showOverlayAndSpinner();

  const linksWithNeedUpdate = document.querySelectorAll('[href*="needUpdate"], [src*="needUpdate"]');

  linksWithNeedUpdate.forEach(link => {
    if (link.tagName === 'IMG' || link.tagName === 'A') {
      const originalHref = link.getAttribute('href');
      const originalSrc = link.getAttribute('src');

      if (originalHref && originalHref.includes('needUpdate')) {
        const updatedHref = originalHref.replace('needUpdate', `https://appointer-ui-${accountId}.s3.eu-central-1.amazonaws.com`);
        link.setAttribute('href', updatedHref);
      }

      if (originalSrc && originalSrc.includes('needUpdate')) {
        const updatedSrc = originalSrc.replace('needUpdate', `https://appointer-ui-${accountId}.s3.eu-central-1.amazonaws.com`);
        link.setAttribute('src', updatedSrc);
      }
    }
  });

  const videos = document.querySelectorAll('video');

  videos.forEach(video => {
    const source = video.querySelector('source');

    if (source) {
      const originalSrc = source.getAttribute('src');

      if (originalSrc && originalSrc.includes('needUpdate')) {
        const updatedSrc = originalSrc.replace('needUpdate', `https://appointer-ui-${accountId}.s3.eu-central-1.amazonaws.com`);
        source.setAttribute('src', updatedSrc);
      }
    }
  });

  videos.forEach(video => {
    video.load();
  });
  hideOverlayAndSpinner();
}

window.addEventListener('DOMContentLoaded', () => {
  updateLinks();
});

window.onload = function () {
  updateLinks();
};