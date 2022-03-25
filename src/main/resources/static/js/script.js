$(document).ready(function() {
	$(".enlace-login").on("click", function() {
		$(this).toggleClass("oculto");
		$(".cerrar-login").toggleClass("oculto");
		$(".login-menu").toggle();
			
	})
	
	$(".cerrar-login").on("click", function() {
		$(this).toggleClass("oculto");
		$(".enlace-login").toggleClass("oculto");
		$(".login-menu").toggle();
			
	})
})
