$(document).ready(function() {
    $(".proyectoFinalizado").prop("hidden", true);
    $("div#finalizadosSwitch").on("change", function() {
        $(".proyectoFinalizado").each(function (i){
            $(this).prop("hidden", function(i, v) {
                return !v;
            });
        })
    })
})