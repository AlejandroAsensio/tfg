function crearMasPuestos(seguir){
	if(seguir){
		puestoForm.destino.value=seguir;
	}
	else if(!seguir){
		puestoForm.destino.value=seguir;
	}
	puestoForm.submit();
}