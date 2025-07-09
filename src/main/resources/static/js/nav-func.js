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


// Adiciona espaçamento entre botões nas tabelas em caso de wrap
function updateButtonSpacing() {
    document.querySelectorAll('.td-actions').forEach(td => {
        const buttons = Array.from(td.querySelectorAll('.btn'));
        if (buttons.length < 2) return;

        const firstTop = buttons[0].getBoundingClientRect().top;
        let wrapped = false;
        for (let i = 1; i < buttons.length; i++) {
            if (buttons[i].getBoundingClientRect().top > firstTop + 1) {
                wrapped = true;
                break;
            }
        }

        buttons.forEach((btn, idx) => {
            btn.style.marginBottom = (wrapped && idx < buttons.length - 1) ? '0.3rem' : '';
        });
    });
}

document.addEventListener('DOMContentLoaded', updateButtonSpacing);
