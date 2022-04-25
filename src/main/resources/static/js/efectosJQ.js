$(document).ready(function() {
	if (window.innerWidth >= 540) {
		$(".textoBotonProyectos").hide();
	}
	
	$("li.botonProyectos").hover(
		function() {
			$(".textoBotonProyectos").show(400);
		},
		function() {
			$(".textoBotonProyectos").hide(400);
		}
	)
})