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
}