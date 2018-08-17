package fr.insee.bidbo.model;

public class CodeVariableModalite {

    private String codeVariable;
    private String codeModalite;

    public CodeVariableModalite() {
	super();
    }

    public CodeVariableModalite(String codeVariable, String codeModalite) {
	super();
	this.codeVariable = codeVariable;
	this.codeModalite = codeModalite;
    }

    public String getCodeVariable() {
	return codeVariable;
    }

    public void setCodeVariable(String codeVariable) {
	this.codeVariable = codeVariable;
    }

    public String getCodeModalite() {
	return codeModalite;
    }

    public void setCodeModalite(String codeModalite) {
	this.codeModalite = codeModalite;
    }

}
