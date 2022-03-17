function tlfValidation(){
    var tlf = document.getElementById("idTlf").value;
    var exp=/^[67][0-9]{8}$/;
    if(exp.test(tlf)){
        document.getElementById("divtlf").innerHTML="";
    }
    else{
        document.getElementById("divtlf").innerHTML="El número de teléfono es incorrecto";
    }
}

function emailValidation(){
    var email = document.getElementById("idEmail").value;
    var exp=/^[A-Za-z0-9]{1,}[@]{1}[a-z]{1,}[\.]{1}[a-z]{2,}$/;
    if(exp.test(email)){
        document.getElementById("divemail").innerHTML="";
    }
    else{
        document.getElementById("divemail").innerHTML="El correo electrónico es incorrecto";
    }
}

function passValidation(){
    var pass = document.getElementById("idPass").value;
    var exp=/^[a-zA-Z0-9]{6,}$/;
    if(exp.test(pass)){
        document.getElementById("divpass").innerHTML="";
    }
    else{
        document.getElementById("divpass").innerHTML="La contraseña no cumple los requisitos mínimos (mas de 6 caracteres)";
    }
}