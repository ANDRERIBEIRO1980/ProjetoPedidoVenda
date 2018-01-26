package br.com.pedidovenda.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "([a-zA-Z]{2}\\d{4,18})?") //valida através desse Pattern de expressao regular, ? significa se informar valida, se nao informar nao valida
public @interface SKU {

	@OverridesAttribute(constraint = Pattern.class, name = "message")//obrigatorio para atribuir a mensagem abaixo na mensagem do pattern
	//captura a mensagem que está na chave do arquivo src/main/resources/ValidationMessages.properties 
	String message() default "{br.com.pedidovenda.constraints.SKU.message}";
	
	//ou poderia exibir a mensagem fixa
	//String message() default "mensagem de validacao";


	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
}	