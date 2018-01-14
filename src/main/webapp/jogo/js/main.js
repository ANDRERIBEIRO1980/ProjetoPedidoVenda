
var tempoInicial = $("#tempo-digitacao").text();
var campoDigitacao = $(".campo-digitacao");

$(function(){

	atualizaTamanhoFrase();
	inicializaContadores();
	inicializaCronometro();
	inicializaMarcadores();
	carregarPlacar();
	$("#botao-reiniciar").click(reiniciaJogo);
	
	$(".tooltip").tooltipster({trigger: "custom"});
	
	

});

function atualizaTamanhoFrase() {
	
	var frase = $(".frase").text();
	var numPalavras = frase.split(" ").length;
	var tamanhoFrase = $("#tamanho-frase");
	tamanhoFrase.text(numPalavras);

}

function inicializaContadores(){
	
	    campoDigitacao.on("input", function(){

		var conteudo = campoDigitacao.val();
		var qtdePalavras = conteudo.split(/\S+/).length-1;
		var qtdeCaracteres = conteudo.length;    
	    
		$("#contador-palavras").text(qtdePalavras);
		$("#contador-caracteres").text(qtdeCaracteres);		

	})
}

function inicializaMarcadores() {

	
	campoDigitacao.on("input", function(){

		var frase = $(".frase").text();
		var digitado = campoDigitacao.val();
		var comparavel = frase.substr(0, digitado.length);

		if (digitado==comparavel){
			campoDigitacao.removeClass("borda-vermelha");
			campoDigitacao.addClass("borda-verde");
		} else{
			campoDigitacao.removeClass("borda-verde");
			campoDigitacao.addClass("borda-vermelha");
		}
	})
}

function inicializaCronometro(){

	campoDigitacao.one("focus", function(){
	
	var cronometroID = setInterval(function(){

		$("#tempo-digitacao").text($("#tempo-digitacao").text()-1);
		$("#botao-reiniciar").attr("disabled",true);
		if ($("#tempo-digitacao").text()==0){
			clearInterval(cronometroID);				
			finalizaJogo();
		}

	},1000);
})
}

function finalizaJogo() {
	campoDigitacao.attr("disabled", true);
	$("#botao-reiniciar").attr("disabled",false);
	//campoDigitacao.css("background-color", "lightgray");
	campoDigitacao.toggleClass("campo-desativado");	
	inserePlacar() ;
}

function reiniciaJogo(){
	
	campoDigitacao.attr("disabled", false);
	campoDigitacao.val("");
	$("#contador-palavras").text("0");
	$("#contador-caracteres").text("0");
	$("#tempo-digitacao").text(tempoInicial);
	inicializaCronometro();
	$("#botao-reiniciar").attr("disabled",true);	
	campoDigitacao.toggleClass("campo-desativado");
	campoDigitacao.removeClass("borda-vermelha");
	campoDigitacao.removeClass("borda-verde");

}

