document.addEventListener("DOMContentLoaded", function () {
    var sidebar = document.getElementById("sidebar");
    var main = document.getElementById("main");

    if (window.innerWidth >= 992) {
        sidebar.style.width = "250px";
        main.style.marginLeft = "250px";
    } else {
        sidebar.style.width = "0";
        main.style.marginLeft = "0";
    }

    sidebar.style.transition = "none";
    main.style.transition = "none";

    setTimeout(function () {
        sidebar.style.transition = "0.5s";
        main.style.transition = "0.5s";
    }, 100);
});

function toggleNav() {
    var sidebar = document.getElementById("sidebar");
    var main = document.getElementById("main");

    if (window.innerWidth >= 992) {
        if (sidebar.style.width === "250px" || sidebar.style.width === "") {
            sidebar.style.width = "0";
            main.style.marginLeft = "0";
        } else {
            sidebar.style.width = "250px";
            main.style.marginLeft = "250px";
        }
    } else {
        if (sidebar.style.width === "250px" || sidebar.style.width === "") {
            sidebar.style.width = "0";
            sidebar.classList.remove("active");
        } else {
            sidebar.style.width = "250px";
            sidebar.classList.add("active");
        }
    }
}

document.addEventListener('click', function(event) {
    var sidebar = document.getElementById("sidebar");
    var navbarToggle = document.getElementById("navbar-toggle-button");

    if (window.innerWidth < 992 &&
        sidebar.classList.contains("active") &&
        !sidebar.contains(event.target) &&
        event.target !== navbarToggle &&
        !navbarToggle.contains(event.target)) {
        sidebar.style.width = "0";
        sidebar.classList.remove("active");
    }
});