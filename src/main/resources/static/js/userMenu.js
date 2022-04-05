$(document).ready(function() {
    $(".userMenuHeader").on("mouseenter", function() {
        $(this).css({
            'background-color' : '#343a40',
            'color' : '#20c997' 
        });

    }).on("mouseleave", function() {
        $(this).css({
            'background-color' : '#20c997',
            'color' : 'rgb(248, 248, 251)' 
        });
    })
})