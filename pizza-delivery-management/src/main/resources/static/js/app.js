
// Small progressive enhancement: smoothly scroll to the menu and dismiss feedback after a short delay.

document.querySelectorAll('a[href="#menu"]').forEach(link => {
  link.addEventListener('click', event => {
    const menu = document.querySelector('#menu');

    if (menu) {
      event.preventDefault();
      menu.scrollIntoView({
        behavior: 'smooth'
      });
    }
  });
});
