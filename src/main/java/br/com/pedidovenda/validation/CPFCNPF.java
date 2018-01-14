package br.com.pedidovenda.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DocumentoFederalValidator.class})//valida CPF/CNPJ através dessa classe
public @interface CPFCNPF {

	//captura a mensagem que está na chave do arquivo src/main/resources/ValidationMessages.properties 
    String message() default "{br.com.pedidovenda.constraints.CpfCnpj.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}