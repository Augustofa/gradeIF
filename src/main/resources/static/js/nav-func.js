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
    if (sidebar.style.width === "250px") {
        sidebar.style.width = "0";
        main.style.marginLeft = "0";
    } else {
        sidebar.style.width = "250px";
        main.style.marginLeft = "250px";
    }
}