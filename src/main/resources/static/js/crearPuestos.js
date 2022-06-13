function crearMasPuestos(seguir) {
	var nombre = document.getElementById("recipient-name").value;
	var desc = document.getElementById("message-text").value;
	var campos = true;
	var habilidades = false;

	if ($("input[name^=idsHabilidadesRequire]:checked").length >= 0) {
		habilidades = true;
	}

	if (nombre == null || nombre == "" || desc == null || desc == "") {
		campos = false;
	}

	if (campos && habilidades) {
		if (seguir) {
			puestoForm.destino.value = seguir;
		} else {
			puestoForm.destino.value = seguir;
		}
			puestoForm.submit();
	} else {
		alert("Completa todos los campos, por favor.");
	}
}
