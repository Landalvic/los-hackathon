package fr.insee.bidbo.chargement;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.model.rmes.ConceptScheme;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.rdfinsee.vocabulary.SKOSStr;
import fr.insee.bidbo.service.rmes.ConceptSchemeService;
import fr.insee.bidbo.service.rmes.ModaliteService;
import fr.insee.bidbo.vocabulary.Insee;

@Service
public class ChargementCodeListeService {

    private static final Logger logger = LoggerFactory.getLogger(ChargementCodeListeService.class);

    @Autowired
    private RDFConnection rdfConnection;

    @Autowired
    private ModaliteService modaliteService;

    @Autowired
    private ConceptSchemeService conceptSchemeService;

    public List<Statement> chargerCodeListe(List<String[]> lignes, boolean telechargerTurtle) {

	List<Statement> triplets = new LinkedList<>();

	String[] premiereLigne = lignes.remove(0);
	String nomConceptFr = premiereLigne[0];
	String nomConceptEn = premiereLigne[1];
	String codeConcept = premiereLigne[2];

	String urlIri = enregistrerConcept(triplets, codeConcept, nomConceptFr, nomConceptEn);

	ConceptScheme cs = new ConceptScheme();
	cs.setIri(Insee.NAMESPACE_RMES_CODES + codeConcept.toLowerCase() + "/cs");
	cs.setIriClass(urlIri);
	cs.setCode(codeConcept);
	cs.setLibelleFr(nomConceptFr);
	if (!StringUtils.isBlank(nomConceptEn)) {
	    cs.setLibelleEn(nomConceptEn);
	}
	conceptSchemeService.add(cs, triplets);

	triplets.add(rdfConnection.createStatement(urlIri, RDFS.SEEALSO, cs.getIri(), Insee.NAMESPACE_RMES_CODES));

	for (String[] ligne : lignes) {
	    Modalite modalite = new Modalite();
	    modalite.setIri(Insee.NAMESPACE_RMES_CODES + ligne[2]);
	    modalite.setIriConceptScheme(cs.getIri());
	    modalite.setCode(ligne[2]);
	    modalite.setLibelleFr(ligne[0]);
	    modalite.setLibelleEn(ligne[1]);
	    modaliteService.add(modalite, triplets);
	    triplets.add(
		    rdfConnection.createStatement(modalite.getIri(), RDF.TYPE, urlIri, Insee.NAMESPACE_RMES_CODES));
	}

	if (!telechargerTurtle) {
	    rdfConnection.ajouterTriplets(BaseRDF.RMES, triplets);
	}
	logger.info("Triplets enregistrés");
	return triplets;
    }

    private String enregistrerConcept(List<Statement> triplets, String code, String libelleFr, String libelleEn) {
	String urlIri = Insee.NAMESPACE_RMES_CODES + code.toLowerCase() + "/" + code;
	triplets.add(rdfConnection.createStatement(urlIri, RDF.TYPE, RDFS.CLASS, Insee.NAMESPACE_RMES_CODES));
	triplets.add(rdfConnection.createStatement(urlIri, RDF.TYPE, OWL.CLASS, Insee.NAMESPACE_RMES_CODES));
	triplets.add(rdfConnection.createStatement(urlIri, RDFS.SUBCLASSOF, SKOS.CONCEPT, Insee.NAMESPACE_RMES_CODES));
	triplets.add(rdfConnection.createStatement(urlIri, SKOS.NOTATION, rdfConnection.createLiteral(code),
		Insee.NAMESPACE_RMES_CODES));
	triplets.add(rdfConnection.createStatement(urlIri, SKOS.PREF_LABEL,
		rdfConnection.createLiteral(libelleFr, Langue.FR), Insee.NAMESPACE_RMES_CODES));
	if (!StringUtils.isBlank(libelleEn)) {
	    triplets.add(rdfConnection.createStatement(urlIri, SKOS.PREF_LABEL,
		    rdfConnection.createLiteral(libelleEn, Langue.EN), Insee.NAMESPACE_RMES_CODES));
	}
	return urlIri;
    }

    public List<Statement> telecharger(String code) {

	List<Statement> triplets = new LinkedList<>();
	ConceptScheme cs = conceptSchemeService.findByPredicat(BaseRDF.RMES, SKOSStr.NOTATION, code);
	cs.setModalites(modaliteService.modalitesByConceptScheme(code));

	String urlIri = enregistrerConcept(triplets, cs.getCode(), cs.getLibelleFr(), cs.getLibelleEn());

	conceptSchemeService.add(cs, triplets);

	triplets.add(rdfConnection.createStatement(urlIri, RDFS.SEEALSO, cs.getIri(), Insee.NAMESPACE_RMES_CODES));

	for (Modalite modalite : cs.getModalites()) {
	    modaliteService.add(modalite, triplets);
	    triplets.add(
		    rdfConnection.createStatement(modalite.getIri(), RDF.TYPE, urlIri, Insee.NAMESPACE_RMES_CODES));
	}
	return triplets;
    }

}
