function drawPages(cur, count, url) {

    if (count == 1 || count == 0)
    {
        document.getElementById("pages").style.display = "none";
        return;
    }

    var minPage = document.createElement('a');
    minPage.className = "w3-button";
    minPage.innerHTML = "«";
    minPage.setAttribute("href", url + "?page=0")
    document.getElementById('pages').appendChild(minPage);

    var start = 0;
    if (cur >= 2 && count > 5)
    {
        if (count - cur - 1 < 2) start = count - 5;
        else start = cur - 2;
    }

    for (var i = start; i < (count > 5 ? start + 5 : count); i++) {
        var page = document.createElement('a');
        page.className = "w3-button";
        if (cur == i) page.className += " w3-green";
        page.innerHTML = i + 1;

        var link = url + "?page=" + i;

        page.setAttribute("href", link);
        document.getElementById('pages').appendChild(page);
    }

    var maxPage = document.createElement('a');
    maxPage.className = "w3-button";
    maxPage.innerHTML = "»";
    maxPage.setAttribute("href", url + "?page=" + (count - 1));
    document.getElementById('pages').appendChild(maxPage);

}