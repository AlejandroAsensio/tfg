$(document).ready(function() {
    $('.grid').masonry({
        // options
        itemSelector: '.grid-item',
        columnWidth: 200
      });
    $(".proyectoFinalizado").prop("hidden", true);
    $("div#finalizadosSwitch").on("change", function() {
        $(".proyectoFinalizado").each(function (i){
            $(this).prop("hidden", function(i, v) {
                return !v;
            });
        })
    })
})