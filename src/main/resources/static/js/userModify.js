var oktlf=true;
var okemail=true;
var okpass=true;
var okpassConfirm=true;
$(document).ready(function(){

    
   $('#idTlf').on("keyup",function(){
    var tlf = document.getElementById("idTlf").value;
    var exp=/^[67][0-9]{8}$/;
    
    if(exp.test(tlf)){
	
       
        oktlf=true;
    }
    else{

        oktlf=false;
    }
   })
        
    
    
    $('#idEmail').on("keyup",function(){
        var email = document.getElementById("idEmail").value;
        var exp=/^[A-Za-z0-9\.\-\_]{1,}[@]{1}[a-z]{1,}[\.]{1}[a-z]{2,}$/;
        
        if(exp.test(email)){

            okemail=true;
        }
        else{
	
            okemail=false;
        }
    })
        
    $('#idPassA').on("keyup",function(){
		okpass=false;
 		okpassConfirm=false;
 		 var pass = $('#idPass').val();
        var passConfirm=$("#idPassConfirm").val();
        var exp=/^[a-zA-Z0-9ñÑ]{6,}$/;
		
	
    	
        if(exp.test(pass) && pass.match(/[A-ZÑ]/)){
          
            okpass=true;
           
        }
        else{
            
            okpass=false;
            
        }
        
      
		if(passConfirm == null || passConfirm == ""){
		okpassConfirm = false;
		}
        
		
	});
    
    $('#idPass').on("keyup",function(){
        var pass = $('#idPass').val();
        var passConfirm=$("#idPassConfirm").val();
        var exp=/^[a-zA-Z0-9ñÑ]{6,}$/;
		
	
    	
        if(exp.test(pass) && pass.match(/[A-ZÑ]/)){
          
            okpass=true;
           
        }
        else{
            
            okpass=false;
            
        }
        
        if(pass == null | pass == ""){
		okpass = true;
		}
		
		if(passConfirm == null | passConfirm == ""){
		okpassConfirm = false;
		}
        
        
    })
    
    $('#idPassConfirm').on("keyup",function(){
	var pass = $('#idPass').val();
	 var passConfirm=$("#idPassConfirm").val();
	if(passConfirm==pass & passConfirm!=""){
				
                okpassConfirm=true;
		
               
            }
            else{
               
                okpassConfirm=false;
            }
})
      
       
    
})
function validar(){
   
        if(oktlf && okemail && okpass && okpassConfirm){
        
            return true;
           
        }
        else{
         alert("Campos incorrectos");
            return false;
            
        }
    }
