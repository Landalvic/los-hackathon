package fr.insee.bidbo.model.rmes;

import javax.xml.bind.annotation.XmlRootElement;

import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeObjectIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.rdfinsee.vocabulary.SKOSStr;

@RdfInseeType(SKOSStr.CONCEPT)
@XmlRootElement(name = "modalite")
public class Modalite {

    @RdfInseeIRI
    private String iri;
    @RdfInseeValue(SKOSStr.NOTATION)
    private String code;
    @RdfInseeObjectIRI(SKOSStr.IN_SCHEME)
    private String iriConceptScheme;
    @RdfInseeValue(value = SKOSStr.PREF_LABEL, lang = Langue.FR)
    private String libelleFr;
    @RdfInseeValue(value = SKOSStr.PREF_LABEL, lang = Langue.EN, isNullable = true)
    private String libelleEn;

    public String getIri() {
	return iri;
    }

    public void setIri(String iri) {
	this.iri = iri;
    }

    public String getLibelleFr() {
	return libelleFr;
    }

    public void setLibelleFr(String libelleFr) {
	this.libelleFr = libelleFr;
    }

    public String getLibelleEn() {
	return libelleEn;
    }

    public void setLibelleEn(String libelleEn) {
	this.libelleEn = libelleEn;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getIriConceptScheme() {
	return iriConceptScheme;
    }

    public void setIriConceptScheme(String iriConceptScheme) {
	this.iriConceptScheme = iriConceptScheme;
    }

    @Override
    public String toString() {
	return "Modalite [iri=" + iri + ", code=" + code + ", iriConceptScheme=" + iriConceptScheme + ", libelleFr="
		+ libelleFr + ", libelleEn=" + libelleEn + "]";
    }

}
