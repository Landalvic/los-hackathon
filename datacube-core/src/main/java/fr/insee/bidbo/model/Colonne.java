package fr.insee.bidbo.model;

import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeObjectIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.rdfinsee.vocabulary.SKOSStr;
import fr.insee.bidbo.vocabulary.str.TYPEStr;

@RdfInseeType(TYPEStr.COLONNE)
public class Colonne {

    @RdfInseeIRI
    private String iri;
    @RdfInseeValue(SKOSStr.PREF_LABEL)
    private String libelle;
    @RdfInseeValue(SKOSStr.NOTATION)
    private String code;
    @RdfInseeObjectIRI(value = SKOSStr.RELATED, isNullable = true)
    private String iriVariable;

    public Colonne() {
	super();
    }

    public Colonne(String iri, String libelle, String code, String iriVariable) {
	super();
	this.iri = iri;
	this.libelle = libelle;
	this.code = code;
	this.iriVariable = iriVariable;
    }

    public String getIriVariable() {
	return iriVariable;
    }

    public void setIriVariable(String iriVariable) {
	this.iriVariable = iriVariable;
    }

    public String getIri() {
	return iri;
    }

    public void setIri(String iri) {
	this.iri = iri;
    }

    public String getLibelle() {
	return libelle;
    }

    public void setLibelle(String libelle) {
	this.libelle = libelle;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    @Override
    public String toString() {
	return "Colonne [iri=" + iri + ", libelle=" + libelle + ", iriVariable=" + iriVariable + "]";
    }

}
