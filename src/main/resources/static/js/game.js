var slideIndex = 1;
var pageIndex = 1;

window.onload = function()
{
    showDivs(slideIndex);
    showPage(pageIndex);
}

function currentDiv(n) {
    showDivs(slideIndex = n);
}

function showDivs(n) {
    var i;
    var x = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("demo");
    if (n > x.length) {slideIndex = 1}
    if (n < 1) {slideIndex = x.length}
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" w3-opacity-off", "");
    }
    x[slideIndex-1].style.display = "block";
    dots[slideIndex-1].className += " w3-opacity-off";
}

function plusDivs(n) {
    showPage(pageIndex += n);
}

function showPage(n) {
    var dots = document.getElementsByClassName("demo");
    if (dots.length < 3) return;

    if (n > dots.length - 2) {pageIndex = dots.length - 2}
    if (n < 1) {pageIndex = 1}

    for (i = 0; i < dots.length; i++) {
        dots[i].style.display = "none";
    }

    for (i = pageIndex - 1; i < dots.length && i < pageIndex + 2; i++) {
        dots[i].style.display = "inline-block";
    }
}