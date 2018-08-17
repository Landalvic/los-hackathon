package fr.insee.bidbo.chargement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.insee.bidbo.chargement.model.ColonneWrapper;
import fr.insee.bidbo.chargement.model.InfosFichier;
import fr.insee.bidbo.chargement.model.ValidationChoixGestionnaire;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.service.ColonneService;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.utils.CsvUtils;

public abstract class ChargementCommun {

    @Autowired
    protected ColonneService colonneService;

    @Autowired
    protected DataCubeService dataCubeService;

    @Autowired
    protected RDFConnection rdfConnection;

    public InfosFichier infosFichier(File fichier) {
	InfosFichier infos = new InfosFichier();
	List<ColonneWrapper> colonnes = new ArrayList<>();

	String[] firstLine = CsvUtils.firstLine(fichier);
	for (String colonne : firstLine) {
	    ColonneWrapper wrapper = new ColonneWrapper(colonneService.trouverParAltLabel(colonne), colonne);
	    colonnes.add(wrapper);
	}
	infos.setColonnes(colonnes);
	infos.setLienFichier(fichier.getAbsolutePath());
	return infos;
    }

    public void enregistrerPreferences(ValidationChoixGestionnaire validation) {
	/*
	 * List<Triplet> ajout = new LinkedList<>(); for (ChoixGestionnaire
	 * choix : validation.getChoixGestionnaire()) { if (choix != null) {
	 * ajout.add(new Triplet(rdfConnection.createIRI("iri"), SKOS.ALT_LABEL,
	 * rdfConnection.createLiteral(choix.getNomColonne()), TYPE.COLONNE));
	 * Value value = rdfConnection.isIri(choix.getChoix()) ?
	 * rdfConnection.createIRI(choix.getChoix()) :
	 * rdfConnection.createLiteral(choix.getChoix()); ajout.add(new
	 * Triplet(rdfConnection.createIRI("iri"), SKOS.RELATED, value,
	 * TYPE.COLONNE)); } } if (ajout.size() > 0) {
	 * rdfConnection.ajouterTriplets(BaseRDF.INTERNE, ajout); }
	 */
    }

}
