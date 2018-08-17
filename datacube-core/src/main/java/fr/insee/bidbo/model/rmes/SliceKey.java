package fr.insee.bidbo.model.rmes;

import java.util.List;

import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeListIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.rdfinsee.vocabulary.RDFSStr;
import fr.insee.bidbo.vocabulary.str.QBStr;

@RdfInseeType(QBStr.SLICE_KEY_CLASS)
public class SliceKey {

    @RdfInseeIRI
    private String iri;
    @RdfInseeValue(value = RDFSStr.LABEL, lang = Langue.FR)
    private String libelleFr;
    @RdfInseeValue(value = RDFSStr.LABEL, lang = Langue.EN, isNullable = true)
    private String libelleEn;
    @RdfInseeListIRI(QBStr.COMPONENT_PROPERTY)
    private List<String> dimensions;

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

    public List<String> getDimensions() {
	return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
	this.dimensions = dimensions;
    }

    @Override
    public String toString() {
	return "SliceKey [iri=" + iri + ", libelleFr=" + libelleFr + ", libelleEn=" + libelleEn + ", dimensions="
		+ dimensions + "]";
    }

}
