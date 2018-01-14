$("#botao-placar").click(mostraPlacar);
$("#botao-sync").click(sincronizaPlacar);

carregaToolTips();

function carregaToolTips(){

	$("#botao-reiniciar").hover(function(){
		 $("#botao-reiniciar").tooltipster("open").tooltipster("content", "Clique para reiniciar"); 
		
	});
	$("#botao-reiniciar").mouseleave(function(){
		$("#botao-reiniciar").tooltipster("close");      
	});

	$("#botao-placar").hover(function(){
		 $("#botao-placar").tooltipster("open").tooltipster("content", "Mostrar/Ocultar placar"); 
		
	});
	$("#botao-placar").mouseleave(function(){
		$("#botao-placar").tooltipster("close");      
	});

	$("#botao-frase").hover(function(){
		 $("#botao-frase").tooltipster("open").tooltipster("content", "Buscar frase aleatória"); 
		
	});
	$("#botao-frase").mouseleave(function(){
		$("#botao-frase").tooltipster("close");      
	});

	$("#botao-frase-id").hover(function(){
		 $("#botao-frase-id").tooltipster("open").tooltipster("content", "Buscar frase especifica"); 
		
	});
	$("#botao-frase-id").mouseleave(function(){
		$("#botao-frase-id").tooltipster("close");      
	});

	$("#botao-sync").hover(function(){
		 $("#botao-sync").tooltipster("open").tooltipster("content", "Clique para salvar");
	});
	$("#botao-sync").mouseleave(function(){
		$("#botao-sync").tooltipster("close");      
	});

}


function mostraPlacar() {
	$(".placar").stop().slideToggle(600);
}


function inserePlacar() {
	
	var corpoTabela = $(".placar").find("tbody");
	var usuario = "Andrezozo";
	var numPalavras = $("#contador-palavras").text();

	var linha = novaLinha(null, usuario, numPalavras);
	
	linha.find(".botao-remover").click(function(event){
		
		event.preventDefault();

		//animação durante remoção do item
		var linha = $(this).parent().parent();
		
		linha.fadeOut(function(){
			linha.remove();				
		});

		/*$.ajax({
		    url: 'http://localhost:8080/PedidoVenda/recursos/frase/',
		    type: 'DELETE',
		    success: function(result) {
		        // Do something with the result
		    }
		});*/
		
		//ou usar um timer
		//seta o timeout para execução somente após o termino do fadeout
		//lembrar de usar o mesmo timeout no fadeout e no settimeout
		//setTimeout(function(){

			//remove o item do html após a animação
		//	linha.remove();						
		//},500);
		

	});

	corpoTabela.prepend(linha);

	$(".placar").slideDown(500);
	scrollPlacar();

}


function scrollPlacar(){

	var posicaoPlacar = $(".placar").offset().top;
	$("body").animate(
	{
		scrollTop:  posicaoPlacar + "px"
	},1000);
}



function novaLinha(id, usuario, numPalavras) {

	var linha = $("<tr>");
	var colunaId = $("<td>").text(id);
	var colunaUsuario = $("<td>").text(usuario);
	var colunaPalavras = $("<td>").text(numPalavras);
	var colunaRemover = $("<td>");
	
	var link = $("<a>").addClass("botao-remover").attr("href","#");
	var icone = $("<i>").addClass("small").addClass("material-icons").text("delete");

	link.append(icone);
	colunaRemover.append(link);
	linha.append(colunaId);
	linha.append(colunaUsuario);
	linha.append(colunaPalavras);
	linha.append(colunaRemover);
	
	return linha;
}

function carregarPlacar(){
	
	$("#spinner").show();
	$("#erro").hide();   
	$("#msg").hide();
	
    $.get("http://localhost:8080/PedidoVenda/recursos/frase/placar", function (data){
    	
    	console.log("dados: " + data);
    	
    	var corpoTabela = $(".placar").find("tbody");
    	
    	$(data).each(function(){
    			
	    		var linha = novaLinha(this.placar.id, this.placar.usuario, this.placar.pontos);    	
	    		
	    		linha.find(".botao-remover").click(function(event){
	    			
													event.preventDefault();										
													//animação durante remoção do item
													var linha = $(this).parent().parent();													
													var id  = linha.find("td:nth-child(1)").text();
													
													$.ajax({
													    url: 'http://localhost:8080/PedidoVenda/recursos/frase/'+id,
													    type: 'DELETE',
													    success: function(result) {													        
													        linha.fadeOut(function(){
																linha.remove();				
															});													    	
													    }
													});
	
	    		});
	    		
	    		corpoTabela.append(linha);
    		
    	})
    	
    	
    })
     .fail(function(){
    	 $("#erro").show();    	 
     })
     .always(function(){
    	 
    	 setTimeout(function(){
    		 $("#spinner").toggle();
    	 },200);
    	 
     });
}




function sincronizaPlacar(){
	
	var placar = [];
	var linhas = $("tbody>tr");	
	$(".tooltip").tooltipster("close");
	
	linhas.each(function(){
	
		var id  = $(this).find("td:nth-child(1)").text();
		var usuario  = $(this).find("td:nth-child(2)").text();
		var numPalavras  = $(this).find("td:nth-child(3)").text();
		
		if (id==null || id==""){			
			var score = {
					usuario: usuario,
					pontos : numPalavras
			};			
		}else{			
			var score = {
					usuario: usuario,
					pontos : numPalavras,
					id     : id
			};	
		}
		placar.push(score);
	})
	
	if (placar.length==0){
		$("#msg").text("Não existe placar para envio.");
		$("#msg").show();
		setTimeout(function(){
			$("#msg").hide();
		},2000)
		return false;
	}
	
	/*var dados = {
		placar : placar
	}*/
	
	$("#spinner").toggle();
	
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/PedidoVenda/recursos/frase",
        data: JSON.stringify(placar),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        processData: true,
        success: function (data, status, jqXHR) {
            alert("success..." + data);
        },
        error: function (xhr, textStatus, errorThrown) {
        	
        	if (xhr.responseText=="Ok"){
        		
        		//$("#msg").text("Placar enviado com sucesso.");
        		//$("#msg").show();       	
        		
        	        		
        	}else
        		{
        	/*
        	console.log("error1: " + xhr); 
        	console.log("error2: " + xhr.responseText);
	       	console.log("error3: " + xhr.status);
	       	console.log("error4: " + textStatus);
	       	console.log("error5: " + errorThrown);*/
        		}
        }
    });
	
    recarregarPlacar();
}


function recarregarPlacar(){
	
	var linhas = $("tbody>tr");	
	
	linhas.each(function(){
		$(this).remove();
	})
	
	setTimeout(function(){
       			$("#msg").hide();
       			carregarPlacar();       			
       			$("#botao-sync").tooltipster("open").tooltipster("content", "Placar salvo com sucesso!!");
        		},2000)

    setTimeout(function(){
       			$(".tooltip").tooltipster("close");       			
        		},3000)        		
	
	
}



















