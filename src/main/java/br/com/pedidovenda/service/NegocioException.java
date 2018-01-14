package br.com.pedidovenda.service;
//quando a classe NegocioException implementa RuntimeException entao não é obrigado a tratar com try/cacth ou throws
//quando a classe NegocioException implementa Exception entao é obrigado a tratar com try/cacth ou throws
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NegocioException(String msg) {
		super(msg);
	}
	
}
