package fr.insee.bidbo.rdfinsee.wrapper;

import java.lang.reflect.Field;
import java.util.List;

public class FieldWrapper {

    private Field field;
    private List<FieldWrapper> subList;

    public FieldWrapper(Field field) {
	super();
	this.field = field;
    }

    public Field getField() {
	return field;
    }

    public void setField(Field field) {
	this.field = field;
    }

    public List<FieldWrapper> getSubList() {
	return subList;
    }

    public void setSubList(List<FieldWrapper> subList) {
	this.subList = subList;
    }

}
