package br.com.pedidovenda.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DocumentoFederalValidator implements ConstraintValidator<CPFCNPF, String> {

	@Override
	public void initialize(CPFCNPF constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean retorno = false;
		value = value.replace(".", "").replace("-", "").replace("/", "") .trim();
		System.out.println(value);
		if (value.length() == 11) {
			retorno = CNP.isValidCPF(value);
		} else {
			retorno = CNP.isValidCNPJ(value);
		}
		return retorno;
	}

}
