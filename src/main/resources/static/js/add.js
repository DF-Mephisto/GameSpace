window.onload = function()
{
    var inputs = document.querySelectorAll('.inputfile');

    Array.prototype.forEach.call(inputs, function(input){
        var label	 = input.nextElementSibling;
        var labelVal = label.innerHTML;

        input.addEventListener('change', function(e){
            var fileName = '';
            fileName = e.target.value.split( '\\' ).pop();

            if( fileName )
                label.querySelector( 'span' ).innerHTML = fileName;
            else
                label.innerHTML = labelVal;
        });
    });

    addScrInput();
}

var count = 0;
function addScrInput()
{
    var input=document.createElement('input');
    input.type="file";
    input.className="inputfile";
    input.setAttribute("name", "scrf");
    input.setAttribute("id", "scr" + count);

    var label=document.createElement('label');
    label.className="w3-btn w3-blue-grey";
    label.setAttribute("for", "scr" + count);
    label.style.marginTop="5px";

    var span=document.createElement('span');
    span.innerHTML="Choose a file";

    label.appendChild(span);

    var br=document.createElement('br');

    document.getElementById('screens').appendChild(input);
    document.getElementById('screens').appendChild(label);
    document.getElementById('screens').appendChild(br);

    input.addEventListener('change', function(e){
        var fileName = '';
        fileName = e.target.value.split( '\\' ).pop();

        if( fileName) {
            label.querySelector('span').innerHTML = fileName;

            if (parseInt(e.target.getAttribute("id").substr(3), 10) == count - 1)
            addScrInput();
        }
    });

    count++;
}