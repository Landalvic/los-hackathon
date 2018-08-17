package fr.insee.bidbo.model.rmes;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeObjectIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.rdfinsee.vocabulary.DCTERMSStr;
import fr.insee.bidbo.rdfinsee.vocabulary.RDFSStr;
import fr.insee.bidbo.vocabulary.str.QBStr;

@RdfInseeType(QBStr.DATA_STRUCTURE_DEFINITION)
@JsonInclude(Include.NON_NULL)
@XmlRootElement(name = "datacube")
public class DataCube {

    @RdfInseeIRI
    private String iri;
    @RdfInseeValue(value = RDFSStr.LABEL, lang = Langue.FR)
    private String libelleFr;
    @RdfInseeValue(value = RDFSStr.LABEL, lang = Langue.EN, isNullable = true)
    private String libelleEn;
    @RdfInseeValue(DCTERMSStr.IDENTIFIER)
    private String code;
    @RdfInseeObjectIRI(value = QBStr.SLICE_KEY, isNullable = true)
    private String iriSliceKey;
    private List<Attribut> attributs;
    private List<Dimension> dimensions;
    private List<ConceptMesure> conceptsMesures;
    private List<SliceKey> slicesKeys;

    public List<SliceKey> getSlicesKeys() {
	return slicesKeys;
    }

    public void setSlicesKeys(List<SliceKey> slicesKeys) {
	this.slicesKeys = slicesKeys;
    }

    public String getIriSliceKey() {
	return iriSliceKey;
    }

    public void setIriSliceKey(String iriSliceKey) {
	this.iriSliceKey = iriSliceKey;
    }

    public String getLibelleFr() {
	return libelleFr;
    }

    public String getLibelleEn() {
	return libelleEn;
    }

    public void setLibelleEn(String libelleEn) {
	this.libelleEn = libelleEn;
    }

    public void setLibelleFr(String libelleFr) {
	this.libelleFr = libelleFr;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    @XmlElementWrapper(name = "attributs")
    @XmlElement(name = "attribut")
    public List<Attribut> getAttributs() {
	return attributs;
    }

    public void setAttributs(List<Attribut> attributs) {
	this.attributs = attributs;
    }

    @XmlElementWrapper(name = "dimensions")
    @XmlElement(name = "dimension")
    public List<Dimension> getDimensions() {
	return dimensions;
    }

    public void setDimensions(List<Dimension> dimensions) {
	this.dimensions = dimensions;
    }

    @XmlElementWrapper(name = "mesures")
    @XmlElement(name = "mesure")
    public List<ConceptMesure> getConceptsMesures() {
	return conceptsMesures;
    }

    public void setConceptsMesures(List<ConceptMesure> conceptsMesures) {
	this.conceptsMesures = conceptsMesures;
    }

    public DataCube() {
	super();
    }

    public String getIri() {
	return iri;
    }

    public void setIri(String iri) {
	this.iri = iri;
    }

    @Override
    public String toString() {
	return "DataCube [iri=" + iri + ", libelleFr=" + libelleFr + ", libelleEn=" + libelleEn + ", code=" + code
		+ ", iriSliceKey=" + iriSliceKey + ", attributs=" + attributs + ", dimensions=" + dimensions
		+ ", conceptsMesures=" + conceptsMesures + "]";
    }

}
