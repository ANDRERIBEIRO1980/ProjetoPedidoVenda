$("#botao-frase").click(fraseAleatoria);
$("#botao-frase-id").click(buscaFraseId);

function fraseAleatoria() {
	
	$("#spinner").toggle();
	$("#erro").hide();   
	$("#msg").hide();
	
    $.get("http://localhost:8080/PedidoVenda/recursos/frase", trocaFraseAleatoria).fail(function(){
    	 $("#erro").show();    	 
     })
     .always(function(){
    	 
    	 setTimeout(function(){
    		 $("#spinner").toggle();
    	 },200);
    	 
     });
}

function trocaFraseAleatoria(data){
	
	$("#erro").hide();	
	
	var frase = $(".frase");
	var numeroAleatorio = Math.floor(Math.random() * data.length);
	
	var conteudoFrase = data[numeroAleatorio].frase.frase; 
	frase.text(conteudoFrase);
	
	$("#tamanho-frase").text(conteudoFrase.split(/\S+/).length-1);
	$("#tempo-digitacao").text(data[numeroAleatorio].frase.tempo);	
	
	
}

function trocaFrasePorId(data){
	
	var frase = $(".frase");
	var conteudoFrase = data.frase.frase; 
	frase.text(conteudoFrase);
	console.log("Retorno: "+ conteudoFrase);
	$("#tamanho-frase").text(conteudoFrase.split(/\S+/).length-1);
	$("#tempo-digitacao").text(data.frase.tempo);	
	
	
}

function buscaFraseId(){
	
	if ($("#frase-id").val()==""){
		$("#msg").text("Informe o Id da frase.");
		$("#msg").show();
		 setTimeout(function(){
			 $("#msg").hide();
			 $("#erro").hide();
		 },2000);
		 
		 return false;
	
	}
	
	var fraseId = $("#frase-id").val();
	$("#spinner").show();
	$("#erro").hide();
	$("#msg").hide();
	
	 $.get("http://localhost:8080/PedidoVenda/recursos/frase/" + fraseId, trocaFrasePorId)
	 .fail(function(jqXHR, textStatus, errorThrown){
		 
		 console.log("Erro5: " + errorThrown);
    	 $("#erro").show();
    	 
     })
     .always(function(xhr, textStatus){
    	 
    	 /*
    	 console.log("always1: " + xhr);
    	 console.log("always2: " + xhr.status);
    	 console.log("always3: " + textStatus);
    	 console.log("resp: " + xhr.responseText);*/
    	 
    	 if (xhr.status=="404"){
    		 $("#msg").text(xhr.responseText);
    		 $("#msg").show();
    		 $("#erro").hide();
    		 
    		 setTimeout(function(){
    			 $("#msg").hide();
    			 $("#erro").hide();
    		 },2000);
    	 }
    	 
    	 setTimeout(function(){
    		 $("#spinner").hide();
    	 },200);
    	 
     });
	
}
