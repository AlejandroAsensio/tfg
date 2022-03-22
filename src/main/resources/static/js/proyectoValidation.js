var fechaHoy=new Date();
var fechaIni="";
var fechaFin= "";
var okFecha=false;
var cosa="";
var cosa2="";
$(document).ready(function(){
    $('#id-fIni').datepicker({dateFormat : 'yy-mm-dd',minDate:-365});
    $('#id-fFin').datepicker({dateFormat : 'yy-mm-dd'});
   
})
function fechaValidationFin(){

cosa= document.getElementById("id-fIni").value;
cosa2= document.getElementById("id-fFin").value;

fechaIni=new Date(cosa);
fechaFin=new Date(cosa2);

if(fechaFin.getTime()<=fechaHoy.getTime() || fechaFin.getTime()<=fechaIni.getTime() ){
  
    document.getElementById("divFecha").innerHTML="La fecha de finalización no puede ser inferior a la fecha de hoy.";
    okFecha=false;
    }
    else{
		
        okFecha=true;
        document.getElementById("divFecha").innerHTML="";
    }
}
function validar(){
   
    if(okFecha){
    
        return true;
       
    }
    else{
        window.location.href="#idNombre"
        return false;
        
    }
}

