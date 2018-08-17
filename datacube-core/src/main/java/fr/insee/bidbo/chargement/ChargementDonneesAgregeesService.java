package fr.insee.bidbo.chargement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.chargement.model.ComponentModalite;
import fr.insee.bidbo.chargement.model.ValidationChoixGestionnaire;
import fr.insee.bidbo.chargement.model.ValidationChoixGestionnaire.ChoixGestionnaire;
import fr.insee.bidbo.chargement.model.ValidationChoixGestionnaire.ChoixParDefaut;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.exception.MelodiException;
import fr.insee.bidbo.model.DataSet;
import fr.insee.bidbo.model.Observation;
import fr.insee.bidbo.model.Slice;
import fr.insee.bidbo.model.rmes.Attribut;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.model.rmes.DataCubeComponent;
import fr.insee.bidbo.model.rmes.Dimension;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.service.DataSetService;
import fr.insee.bidbo.service.ObservationService;
import fr.insee.bidbo.service.SliceService;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.utils.CsvUtils;
import fr.insee.bidbo.utils.CsvUtils.LigneFichier;
import fr.insee.bidbo.vocabulary.QB;
import fr.insee.bidbo.vocabulary.str.SdmxDimensionStr;

@Service
public class ChargementDonneesAgregeesService extends ChargementCommun {

    private static final Logger logger = LoggerFactory.getLogger(ChargementDonneesAgregeesService.class);

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private DataCubeService dataCubeService;

    @Autowired
    private SliceService sliceService;

    public void chargerFichier(ValidationChoixGestionnaire validation) {
	chargerFichier(validation, null);
    }

    public void chargerFichier(ValidationChoixGestionnaire validation, OutputStream output) {
	Date now = new Date();

	File fichier = FileUtils.getFile(validation.getLienFichier());
	DataCube dataCube = dataCubeService.trouverDatacubeAvecModalites(validation.getDatacube());
	dataCube.setConceptsMesures(dataCubeService.trouverConceptsMesures(dataCube.getIri()));

	try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
	    Iterator<LigneFichier> iterateur = CsvUtils.readToMap(reader, '#');
	    if (output == null) {
		chargerViaBase(validation, now, dataCube, iterateur);
	    } else {
		chargerViaFichierTelechargeable(validation, now, dataCube, iterateur, output);
	    }
	} catch (Exception e) {
	    logger.error("Batch en erreur", e);
	}
	logger.info("Fichier enregistré !");

    }

    private void chargerViaFichierTelechargeable(ValidationChoixGestionnaire validation, Date now, DataCube dataCube,
	    Iterator<LigneFichier> iterateur, OutputStream output) throws FileNotFoundException, IOException {
	List<Statement> ajout = new LinkedList<>();
	DataSet dataset = chargerDataSet(iterateur.next(), validation, dataCube, ajout);
	Rio.write(ajout, output, RDFFormat.TURTLE);
	while (iterateur.hasNext()) {
	    ajout = new LinkedList<>();
	    chargerObservation(iterateur.next(), validation, dataset, ajout, dataCube);
	    Rio.write(ajout, output, RDFFormat.TURTLE);
	}
    }

    private void chargerViaBase(ValidationChoixGestionnaire validation, Date now, DataCube dataCube,
	    Iterator<LigneFichier> iterateur) throws InterruptedException {
	ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	Semaphore semaphore = new Semaphore(4);
	List<LigneFichier> liste = new ArrayList<>(10000);

	LigneFichier ligneEsane = iterateur.next();
	List<Statement> ajout = new LinkedList<>();
	DataSet dataset = chargerDataSet(ligneEsane, validation, dataCube, ajout);
	rdfConnection.ajouterTriplets(BaseRDF.INTERNE, ajout);
	do {
	    liste.add(ligneEsane);
	    if (liste.size() >= 10000) {
		List<LigneFichier> nouvelleReference = liste;
		semaphore.acquire();
		executor.submit(() -> {
		    chargerPartition(nouvelleReference, validation, now, dataset, dataCube);
		    semaphore.release();
		});
		liste = new ArrayList<>(10000);
	    }
	    ligneEsane = iterateur.next();
	} while (iterateur.hasNext());
	executor.shutdown();
	chargerPartition(liste, validation, now, dataset, dataCube);
	executor.awaitTermination(5, TimeUnit.HOURS);
    }

    private void chargerPartition(List<LigneFichier> lignes, ValidationChoixGestionnaire validation, Date now,
	    DataSet dataset, DataCube dataCube) {
	Iterator<LigneFichier> iterateur = lignes.iterator();
	List<Statement> ajout = new LinkedList<>();
	List<Statement> suppression = new LinkedList<>();
	while (iterateur.hasNext()) {
	    chargerObservation(iterateur.next(), validation, dataset, ajout, dataCube);
	}
	rdfConnection.enleverTriplets(BaseRDF.INTERNE, suppression);
	rdfConnection.ajouterTriplets(BaseRDF.INTERNE, ajout);
	logger.info("10000 lignes enregistrées");
    }

    private DataSet chargerDataSet(LigneFichier ligne, ValidationChoixGestionnaire validation, DataCube dataCube,
	    List<Statement> ajout) {
	List<Statement> ajoutTemp = new ArrayList<>();
	try {
	    DataSet dataset = new DataSet();
	    dataset.setIri(RDFConnection.IRI_BASE_PROJET + "/dataset/" + dataCube.getCode());
	    dataset.setLibelleFr(dataCube.getLibelleFr());
	    dataset.setLibelleEn(dataCube.getLibelleEn());
	    dataset.setIriCube(validation.getDatacube());
	    chargerAttributs(ligne, validation, dataCube.getAttributs(), dataset.getIri(), QB.DATA_SET_CLASS,
		    ajoutTemp);
	    dataSetService.add(dataset, ajout);
	    ajout.addAll(ajoutTemp);
	    return dataset;
	} catch (MelodiException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private Observation chargerObservation(LigneFichier ligne, ValidationChoixGestionnaire validation, DataSet dataSet,
	    List<Statement> ajout, DataCube dataCube) {
	List<Statement> ajoutTemp = new ArrayList<>();
	try {
	    Observation obs = new Observation();
	    obs.setIri(dataSet.getIri() + "/observation/" + ligne.getMapFirst("numenregistrement"));
	    obs.setIriDataset(dataSet.getIri());
	    String choixConcept = findChoix(ligne, validation, "concept");
	    List<ConceptMesure> concepts = dataCube
		    .getConceptsMesures()
		    .stream()
		    .filter(c -> StringUtils.equalsIgnoreCase(c.getCode(), choixConcept))
		    .collect(Collectors.toList());
	    if (concepts.size() <= 0) {
		throw new MelodiException("Le concept " + choixConcept + " n'existe pas");
	    }
	    ConceptMesure concept = concepts.get(0);
	    obs.setIriMeasureType(concept.getIri());
	    obs.setTimePeriod(findChoix(ligne, validation, SdmxDimensionStr.TIME_PERIOD));
	    chargerAttributs(ligne, validation, dataCube.getAttributs(), obs.getIri(), QB.OBSERVATION_CLASS, ajoutTemp);
	    if (dataCube.getIriSliceKey() == null) {
		List<ComponentModalite> liste = chargerDimensions(ligne, validation, dataCube.getDimensions());
		chargerComponentsModalites(liste, obs.getIri(), QB.OBSERVATION_CLASS, ajoutTemp);
	    } else {
		Slice slice = new Slice();
		slice.setIriSliceKey(dataCube.getIriSliceKey());
		slice.setIriDataSet(dataSet.getIri());
		slice.setIriMeasureType(concept.getIri());
		List<ComponentModalite> liste = chargerDimensions(ligne, validation, dataCube.getDimensions());
		Collections.sort(liste);
		StringBuilder idBuilder = new StringBuilder(concept.getCode());
		for (ComponentModalite modalite : liste) {
		    idBuilder.append("-" + modalite.getModalite().getCode());
		}
		slice.setIri(
			RDFConnection.IRI_BASE_PROJET + "/" + dataCube.getCode() + "/slice-" + idBuilder.toString());
		slice.setCode(idBuilder.toString());
		sliceService.add(slice, ajoutTemp);
		chargerAttributs(ligne, validation, dataCube.getAttributs(), slice.getIri(), QB.SLICE_CLASS, ajoutTemp);
		chargerComponentsModalites(liste, slice.getIri(), QB.SLICE_CLASS, ajoutTemp);
		ajoutTemp.add(
			rdfConnection.createStatement(slice.getIri(), QB.OBSERVATION, obs.getIri(), QB.SLICE_CLASS));
	    }
	    obs.setValeur(Double.valueOf(findChoix(ligne, validation, "valeur")));
	    observationService.add(obs, ajoutTemp);
	    ajout.addAll(ajoutTemp);
	    return obs;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private String findChoix(LigneFichier ligne, ValidationChoixGestionnaire validation, String iriAttribut) {
	for (ChoixGestionnaire choix : validation.getChoixGestionnaire()) {
	    if (StringUtils.equals(choix.getChoix(), iriAttribut)) {
		return ligne.getMapFirst(choix.getNomColonne());
	    }
	}
	for (ChoixParDefaut choix : validation.getChoixParDefaut()) {
	    if (StringUtils.equals(choix.getIri(), iriAttribut)) {
		return choix.getChoix();
	    }
	}
	return null;
    }

    private void chargerAttributs(LigneFichier ligne, ValidationChoixGestionnaire validation, List<Attribut> attributs,
	    String iriParent, IRI iriClass, List<Statement> ajout) throws MelodiException {
	List<ComponentModalite> liste = new ArrayList<>();
	for (Attribut attribut : attributs) {
	    if (StringUtils.equals(attribut.getIriAttachment(), iriClass.stringValue())) {
		Modalite modalite = chargerComponent(ligne, validation, attribut);
		if (modalite != null) {
		    liste.add(new ComponentModalite(attribut, modalite));
		}
	    }
	}
	chargerComponentsModalites(liste, iriParent, iriClass, ajout);
    }

    private List<ComponentModalite> chargerDimensions(LigneFichier ligne, ValidationChoixGestionnaire validation,
	    List<Dimension> dimensions) throws MelodiException {
	List<ComponentModalite> liste = new ArrayList<>();
	for (Dimension dimension : dimensions) {
	    Modalite modalite = chargerComponent(ligne, validation, dimension);
	    if (modalite != null) {
		liste.add(new ComponentModalite(dimension, modalite));
	    }
	}
	return liste;
    }

    private Modalite chargerComponent(LigneFichier ligne, ValidationChoixGestionnaire validation,
	    DataCubeComponent component) throws MelodiException {
	String choix = findChoix(ligne, validation, component.getIri());
	if (StringUtils.isNotBlank(choix)) {
	    for (Modalite modalite : component.getModalites()) {
		if (StringUtils.equalsIgnoreCase(choix, modalite.getCode())) {
		    return modalite;
		}
	    }
	    throw new MelodiException(
		    "La modalité " + choix + " n'existe pas pour la composante " + component.getLibelleFr());
	}
	return null;
    }

    private void chargerComponentsModalites(List<ComponentModalite> liste, String iriParent, IRI iriClass,
	    List<Statement> ajout) {
	for (ComponentModalite modalite : liste) {
	    ajout.add(rdfConnection.createStatement(iriParent, modalite.getComponent().getIri(),
		    modalite.getModalite().getIri(), iriClass));
	}
    }

}
