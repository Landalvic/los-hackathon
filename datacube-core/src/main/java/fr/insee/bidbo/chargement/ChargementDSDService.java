package fr.insee.bidbo.chargement;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.model.rmes.Attribut;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.model.rmes.DataCubeComponent;
import fr.insee.bidbo.model.rmes.Dimension;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.vocabulary.Insee;
import fr.insee.bidbo.vocabulary.QB;
import fr.insee.bidbo.vocabulary.str.SdmxDimensionStr;

@Service
public class ChargementDSDService {

    private static final Logger logger = LoggerFactory.getLogger(ChargementDSDService.class);

    @Autowired
    private RDFConnection rdfConnection;

    @Autowired
    private DataCubeService dataCubeService;

    public List<Statement> chargerDSD(DataCube datacube, boolean telechargerTurtle) {
	if (StringUtils.isBlank(datacube.getIri())) {
	    datacube.setIri(Insee.NAMESPACE_RMES_DATACUBE + datacube.getCode());
	}

	List<Statement> triplets = new LinkedList<>();

	dataCubeService.add(datacube, triplets);

	for (ConceptMesure concept : datacube.getConceptsMesures()) {
	    BNode bn = rdfConnection.createBlankNode();
	    triplets.add(
		    rdfConnection.createStatement(datacube.getIri(), QB.COMPONENT, bn, QB.DATA_STRUCTURE_DEFINITION));
	    triplets.add(rdfConnection.createStatement(bn, RDF.TYPE, QB.COMPONENT_SPECIFICATION,
		    QB.DATA_STRUCTURE_DEFINITION));
	    triplets.add(rdfConnection.createStatement(bn, QB.MEASURE, concept.getIri(), QB.DATA_STRUCTURE_DEFINITION));
	}

	for (Attribut attribut : datacube.getAttributs()) {
	    if (!RDFConnection.isIri(attribut.getIri())) {
		creerComponent(attribut, triplets, false);
	    }
	    creerLienComponent(attribut, triplets, datacube, QB.ATTRIBUTE);
	}

	Dimension timePeriod = new Dimension();
	timePeriod.setIri(SdmxDimensionStr.TIME_PERIOD);
	timePeriod.setOrdre(1);
	creerLienDimension(timePeriod, triplets, datacube);
	for (Dimension dimension : datacube.getDimensions()) {
	    if (!RDFConnection.isIri(dimension.getIri())) {
		creerComponent(dimension, triplets, true);
	    }
	    creerLienDimension(dimension, triplets, datacube);
	}

	if (!telechargerTurtle) {
	    rdfConnection.ajouterTriplets(BaseRDF.RMES, triplets);
	}
	logger.info("Triplets enregistrés");
	return triplets;
    }

    private void creerLienDimension(Dimension dimension, List<Statement> triplets, DataCube datacube) {
	BNode bn = creerLienComponent(dimension, triplets, datacube, QB.DIMENSION);
	if (dimension.getOrdre() != null) {
	    triplets.add(rdfConnection.createStatement(bn, QB.ORDER, rdfConnection.createLiteral(dimension.getOrdre()),
		    QB.DATA_STRUCTURE_DEFINITION));
	}
    }

    private BNode creerLienComponent(DataCubeComponent component, List<Statement> triplets, DataCube datacube,
	    IRI type) {
	BNode bn = rdfConnection.createBlankNode();
	triplets.add(rdfConnection.createStatement(datacube.getIri(), QB.COMPONENT, bn, QB.DATA_STRUCTURE_DEFINITION));
	triplets.add(
		rdfConnection.createStatement(bn, RDF.TYPE, QB.COMPONENT_SPECIFICATION, QB.DATA_STRUCTURE_DEFINITION));
	triplets.add(rdfConnection.createStatement(bn, type, component.getIri(), QB.DATA_STRUCTURE_DEFINITION));
	if (component.getIriAttachment() != null) {
	    triplets.add(rdfConnection.createStatement(bn, QB.COMPONENT_ATTACHMENT, component.getIriAttachment(),
		    QB.DATA_STRUCTURE_DEFINITION));
	}
	return bn;
    }

    private void creerComponent(DataCubeComponent component, List<Statement> triplets, boolean dimension) {
	String namespaceComponent = dimension ? Insee.NAMESPACE_RMES_DIMENSION : Insee.NAMESPACE_RMES_ATTRIBUT;
	component.setIri(namespaceComponent + component.getIri());
	triplets.add(rdfConnection.createStatement(component.getIri(), RDF.TYPE, QB.CODED_PROPERTY,
		QB.DATA_STRUCTURE_DEFINITION));
	IRI property = dimension ? QB.DIMENSION_PROPERTY : QB.ATTRIBUTE_PROPERTY;
	triplets.add(
		rdfConnection.createStatement(component.getIri(), RDF.TYPE, property, QB.DATA_STRUCTURE_DEFINITION));
	triplets.add(rdfConnection.createStatement(component.getIri(), RDFS.LABEL,
		rdfConnection.createLiteral(component.getLibelleFr(), Langue.FR), QB.DATA_STRUCTURE_DEFINITION));
	triplets.add(rdfConnection.createStatement(component.getIri(), RDFS.LABEL,
		rdfConnection.createLiteral(component.getLibelleEn(), Langue.EN), QB.DATA_STRUCTURE_DEFINITION));
	triplets.add(rdfConnection.createStatement(component.getIri(), RDFS.RANGE, component.getIriRange(),
		QB.DATA_STRUCTURE_DEFINITION));
	triplets.add(rdfConnection.createStatement(component.getIri(), QB.CONCEPT, component.getIriConcept(),
		QB.DATA_STRUCTURE_DEFINITION));
	triplets.add(rdfConnection.createStatement(component.getIri(), QB.CODE_LIST, component.getIriConceptScheme(),
		QB.DATA_STRUCTURE_DEFINITION));
    }

}
