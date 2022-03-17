var oktlf=false;
var okemail=false;
var okpass=false;
$(document).ready(function(){


    $('#plantilla').on("click",function(){
        var x=Math.floor(Math.random()*99);
        $('#idNombre').val("Dolores"+x);
        $('#idApe1').val("Crotales");
        $('#idApe2').val("Fuertes");
        $('#idTlf').val("654607766");
        $('#idEmail').val("dolores"+x+"@gmail.com");
        $('#idPass').val("Contraseña");
        $('#idPassConfirm').val("Contraseña");
        $('#divpass').text("La pass es: Contraseña");
    })
        
    
    
    
   $('#idTlf').on("keyup",function(){
    var tlf = document.getElementById("idTlf").value;
    var exp=/^[67][0-9]{8}$/;
    
    if(exp.test(tlf)){
		$("#divtlf").css("color","green");
        document.getElementById("divtlf").innerHTML="El teléfono es válido";
        oktlf=true;
    }
    else{
		$("#divtlf").css("color","red");
        document.getElementById("divtlf").innerHTML="El número de teléfono es incorrecto";
        oktlf=false;
    }
   })
        
    
    
    $('#idEmail').on("keyup",function(){
        var email = document.getElementById("idEmail").value;
        var exp=/^[A-Za-z0-9]{1,}[@]{1}[a-z]{1,}[\.]{1}[a-z]{2,}$/;
        
        if(exp.test(email)){
			$("#divemail").css("color","green");
            document.getElementById("divemail").innerHTML="El correo es válido";
            okemail=true;
        }
        else{
			$("#divemail").css("color","red");
            document.getElementById("divemail").innerHTML="El correo electrónico es incorrecto";
            okemail=false;
        }
    })
        
    
    
    $('#idPass').on("keyup",function(){
        var pass = $('#idPass').val();
        var exp=/^[a-zA-Z0-9ñÑ]{6,}$/;
        
    
        if(exp.test(pass) && pass.match(/[A-ZÑ]/)){
            $("#divpass").css("color","green");
            document.getElementById("divpass").innerHTML="Contraseña válida";
            okpass=true;
           
        }
        else{
            $("#divpass").css("color","red");
            document.getElementById("divpass").innerHTML="Requisitos incorrectos (Al menos 6 caracteres y una letra mayúscula)";
            okpass=false;
        }
    })
    
    $('#idPassConfirm').on("keyup",function(){
	var pass = $('#idPass').val();
	if($('#idPassConfirm').val()!=pass){
                $("#divpassConfirm").css("color","red");
                document.getElementById("divpassConfirm").innerHTML="Las contraseñas deben de ser iguales";
            }
            else{
                $("#divpassConfirm").css("color","green");
                document.getElementById("divpassConfirm").innerHTML="Confirmar contraseña correcto";
                ok=true;
            }
})
      
       
    
})
function validar(){
   
        if(oktlf && okemail && okpass){
        
            return true;
           
        }
        else{
          document.getElementById("divValidate").innerHTML="Revisa los datos";
            return false;
            
        }
    }
