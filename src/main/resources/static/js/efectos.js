$(document).ready(function() {
    $("div#finalizadosSwitch").on("change", function() {
        $(".proyectoFinalizado").each(function (i){
            console.log("Hiden nº " + i)
            $(this).prop("hidden", function(i, v) {
                console.log("Toggle -" + v)
                return !v;
            });
        })
    })
})