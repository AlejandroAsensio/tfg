function crearMasPuestos(seguir){
	var nombre = document.getElementById("recipient-name").value;
	var desc = document.getElementById("message-text").value;
	var ok = true;
	var okH = false;
	
	
	if ($('input[name^=idsHabilidadesRequire]:checked').length <= 0) {
       
    }else{
        okH = true;
    }
	
	if(nombre == null || nombre == "" || desc == null || desc == ""){
		ok = false;
	
		
	}
	
	if(ok && okH){
	if(seguir){
		puestoForm.destino.value=seguir;
	}
	else if(!seguir){
		puestoForm.destino.value=seguir;
	}
	
	puestoForm.submit();	
	}
	else{
		alert("Campos vacios");
	}
	
}