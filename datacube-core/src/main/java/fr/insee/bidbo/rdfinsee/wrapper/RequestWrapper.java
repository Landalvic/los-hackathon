package fr.insee.bidbo.rdfinsee.wrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RequestWrapper {

    private StringBuilder select;
    private StringBuilder where;
    private StringBuilder end;
    private List<FieldWrapper> fieldsWrapper = new ArrayList<>();
    private List<FieldWrapper> fieldsListWrapper = new ArrayList<>();
    private Field fieldFilter;
    private Object complement;

    public List<FieldWrapper> getFieldsListWrapper() {
	return fieldsListWrapper;
    }

    public void setFieldsListWrapper(List<FieldWrapper> fieldsListWrapper) {
	this.fieldsListWrapper = fieldsListWrapper;
    }

    public Object getComplement() {
	return complement;
    }

    public void setComplement(Object complement) {
	this.complement = complement;
    }

    public Field getFieldFilter() {
	return fieldFilter;
    }

    public void setFieldFilter(Field fieldFilter) {
	this.fieldFilter = fieldFilter;
    }

    public List<FieldWrapper> getFieldsWrapper() {
	return fieldsWrapper;
    }

    public void setFieldsWrapper(List<FieldWrapper> fieldsWrapper) {
	this.fieldsWrapper = fieldsWrapper;
    }

    public StringBuilder getSelect() {
	return select;
    }

    public void setSelect(StringBuilder select) {
	this.select = select;
    }

    public StringBuilder getWhere() {
	return where;
    }

    public void setWhere(StringBuilder where) {
	this.where = where;
    }

    public StringBuilder getEnd() {
	return end;
    }

    public void setEnd(StringBuilder end) {
	this.end = end;
    }

}
