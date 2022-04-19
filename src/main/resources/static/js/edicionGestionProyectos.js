/**
	Cambia la visibilidad de los elementos de edición en la vista de gestión de proyecto
 */
function edicionGestionProyecto() {
	//Establece el valor hidden del elemento en el valor contrario en que se encuentre
	$(".botonModoEdicion").prop('hidden', function(i, v) {
		return !v;
	});
	$(".botonEdicion").prop('hidden', function(i, v) {
		return !v;
	});
	//Establece el valor disabled del elemento en el valor contrario en que se encuentre
	$(".inputEdicionProyecto")
		.prop('disabled', (i, v) => !v)
		.toggleClass("bg-light")
		.toggleClass("border-0");
}
