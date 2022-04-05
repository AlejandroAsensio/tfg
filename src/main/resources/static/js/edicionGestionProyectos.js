function mostrarEdicionCamposGestionProyecto() {
    $(".hiddenInputProyecto").toggleClass("hiddenHinputProyecto").toggleClass("visibleHinputProyecto").toggle();
    $(".visibleInputProyecto").toggleClass("visibleHinputProyecto").toggleClass("hiddenHinputProyecto").toggle();
    $("div#jumboGeneralInfo").toggleClass("warningEdicionProyecto");
}

function cancelarEdicionCamposGestionProyecto() {
    $(".hiddenInputProyecto").toggleClass("hiddenHinputProyecto").toggleClass("visibleHinputProyecto").toggle();
    $(".visibleInputProyecto").toggleClass("visibleHinputProyecto").toggleClass("hiddenHinputProyecto").toggle();
    $("div#jumboGeneralInfo").toggleClass("warningEdicionProyecto");
}