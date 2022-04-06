/**
	Cambia la visibilidad de los botones generales de edición en la vista de gestión de proyecto
 */
function edicionGestionProyecto() {
	$(".botonEdicionProyecto").toggleClass("hiddenInputProyecto");
}

/**
	Habilita los campos editables en el formulario de Proyecto U en la vista de gestión de proyectos
	y oculta los que mostraban la información
 */
function mostrarEdicionCamposGestionProyecto() {
    $(".hiddenInputProyecto").toggleClass("hiddenHinputProyecto").toggleClass("visibleHinputProyecto").toggle();
    $(".visibleInputProyecto").toggleClass("visibleHinputProyecto").toggleClass("hiddenHinputProyecto").toggle();
    $("div#jumboGeneralInfo").toggleClass("warningEdicionProyecto");
}

/**
	Deshabilita los campos editables en el formulario de Proyecto U en la vista de gestión de proyectos
	y habilita los que muestran la información
 */
function cancelarEdicionCamposGestionProyecto() {
    $(".hiddenInputProyecto").toggleClass("hiddenHinputProyecto").toggleClass("visibleHinputProyecto").toggle();
    $(".visibleInputProyecto").toggleClass("visibleHinputProyecto").toggleClass("hiddenHinputProyecto").toggle();
    $("div#jumboGeneralInfo").toggleClass("warningEdicionProyecto");
}


function mostrarEdicionEquipoGestionProyecto() {
	
}