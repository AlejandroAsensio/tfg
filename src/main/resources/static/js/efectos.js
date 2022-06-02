$(document).ready(function() {
    $("div#finalizadosSwitch").on("change", function() {
        $(".proyectoFinalizado").each(function (i){
            $(this).prop("hidden", function(i, v) {
                return !v;
            });
        })
    })
})