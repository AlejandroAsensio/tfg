var oktlf=false;
var okemail=false;
var okpass=false;
var okpassConfirm=false;
$(document).ready(function(){
        
    
    
    
   $('#idTlf').on("keyup",function(){
    var tlf = document.getElementById("idTlf").value;
    var exp=/^[67][0-9]{8}$/;
    
    if(exp.test(tlf)){
		$("#divtlf").css("color","green");
        document.getElementById("divtlf").innerHTML="";
       
        oktlf=true;
    }
    else{
		$("#divtlf").css("color","red");
        document.getElementById("divtlf").innerHTML="El formato del teléfono es incorrecto.";
        oktlf=false;
    }
   })
        
    
    
    $('#idEmail').on("keyup",function(){
        var email = document.getElementById("idEmail").value;
        var exp=/^[A-Za-z0-9\.\-\_]{1,}[@]{1}[a-z]{1,}[\.]{1}[a-z]{2,}$/;
        
        if(exp.test(email)){
			$("#divemail").css("color","green");
            document.getElementById("divemail").innerHTML="";
            okemail=true;
        }
        else{
			$("#divemail").css("color","red");
            document.getElementById("divemail").innerHTML="El formato del email es incorrecto.";
            okemail=false;
        }
    })
        
    
    
    $('#idPass').on("keyup",function(){
        var pass = $('#idPass').val();
        var passConfirm=$("#idPassConfirm").val();
        var exp=/^[a-zA-Z0-9ñÑ]{6,}$/;
		
		if(pass == passConfirm && pass!=""){
			$("#divpassConfirm").css("color","green");
            document.getElementById("divpass").innerHTML="";
                document.getElementById("divpassConfirm").innerHTML="Coinciden.";
		}
		else{
			$("#divpassConfirm").css("color","red");
                document.getElementById("divpassConfirm").innerHTML="Las contraseñas deben ser iguales.";
		}
    
        if(exp.test(pass) && pass.match(/[A-ZÑ]/)){
            $("#divpass").css("color","green");
            document.getElementById("divpass").innerHTML="Contraseña válida.";
            okpass=true;
           
        }
        else{
            $("#divpass").css("color","red");
            document.getElementById("divpass").innerHTML="*Al menos 6 caracteres y una letra mayúscula.";
            okpass=false;
            
        }
        
        
        
        
    })
    
    $('#idPassConfirm').on("keyup",function(){
	var pass = $('#idPass').val();
	 var passConfirm=$("#idPassConfirm").val();
	if(passConfirm==pass & passConfirm!=""){
				$("#divpassConfirm").css("color","green");
                document.getElementById("divpassConfirm").innerHTML="Coinciden.";
                okpassConfirm=true;
		
               
            }
            else{
                $("#divpassConfirm").css("color","red");
                document.getElementById("divpassConfirm").innerHTML="Las contraseñas deben ser iguales.";
                okpassConfirm=false;
            }
})
      
       
    
})
function validar(){
   
        if(oktlf && okemail && okpass && okpassConfirm){
        
            return true;
           
        }
        else{
          document.getElementById("divValidate").innerHTML="¡Revisa todos los campos!";
          window.location.href="#01"
            return false;
            
        }
    }
