package fr.insee.bidbo.rdfinsee.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.insee.bidbo.rdfinsee.enumeration.Langue;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RdfInseeValue {

    String value();

    boolean isNullable() default false;

    Langue lang() default Langue.NULL;

    boolean reference() default false;

}
