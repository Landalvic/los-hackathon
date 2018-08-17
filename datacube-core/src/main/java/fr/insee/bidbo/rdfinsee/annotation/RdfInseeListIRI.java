package fr.insee.bidbo.rdfinsee.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RdfInseeListIRI {

    String value();

    boolean isNullable() default false;

    boolean reference() default false;

}