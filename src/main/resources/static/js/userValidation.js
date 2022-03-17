$(document).ready(function(){


    $('#plantilla').on("click",function(){
        var x=Math.floor(Math.random()*99);
        $('#idNombre').val("Dolores");
        $('#idApe1').val("Crotales");
        $('#idApe2').val("Fuertes");
        $('#idTlf').val("654607766");
        $('#idEmail').val("ejemplo"+x+"@gmail.com");
        $('#idPass').val("Contraseña");
        $('#idPassConfirm').val("Contraseña");
        $('#divpass').text("La pass es: Contraseña");
    })
        
    
    
    
   $('#checktlf').on("click",function(){
    var tlf = document.getElementById("idTlf").value;
    var exp=/^[67][0-9]{8}$/;
    if(exp.test(tlf)){
        document.getElementById("divtlf").innerHTML="";
    }
    else{
        document.getElementById("divtlf").innerHTML="El número de teléfono es incorrecto";
    }
   })
        
    
    
    $('#checkemail').on("click",function(){
        var email = document.getElementById("idEmail").value;
        var exp=/^[A-Za-z0-9]{1,}[@]{1}[a-z]{1,}[\.]{1}[a-z]{2,}$/;
        
        if(exp.test(email)){
            document.getElementById("divemail").innerHTML="";
        }
        else{
            document.getElementById("divemail").innerHTML="El correo electrónico es incorrecto";
        }
    })
        
    
    
    $('#checkpass').on("click",function(){
        var pass = $('#idPass').val();
        var exp=/^[a-zA-Z0-9ñÑ]{6,}$/;
        
    
        if(exp.test(pass) && pass.match(/[A-ZÑ]/)){
            $("#divpass").css("color","green");
            document.getElementById("divpass").innerHTML="Contraseña válida";
            if($('#idPassConfirm').val()!=pass){
                document.getElementById("divpassConfirm").innerHTML="Las contraseñas deben de ser iguales";
            }
            else{
                $("#divpassConfirm").css("color","green");
                document.getElementById("divpassConfirm").innerHTML="Contraseñas válidas";
            }
           
        }
        else{
            $("#divpass").css("color","red");
            document.getElementById("divpass").innerHTML="Requisitos incorrectos (Al menos 6 caracteres y una letra mayúscula)";
        }
    })
        
        
    
})
