package fr.insee.bidbo.service.rmes.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.dao.rmes.DataCubeDao;
import fr.insee.bidbo.model.rmes.Attribut;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.model.rmes.Dimension;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.rdfinsee.vocabulary.DCTERMSStr;
import fr.insee.bidbo.rdfinsee.vocabulary.SKOSStr;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.service.rmes.ModaliteService;
import fr.insee.bidbo.service.rmes.SliceKeyService;

@Service
public class DataCubeServiceImpl extends AbstractGenericService<DataCube> implements DataCubeService {

    @Autowired
    private DataCubeDao dataCubeDao;

    @Autowired
    private ModaliteService modaliteService;

    @Autowired
    private SliceKeyService sliceKeyService;

    @Override
    protected GenericDao<DataCube> getDao() {
	return dataCubeDao;
    }

    @Override
    public List<DataCube> relatedToAConceptMesure(String iriConceptMesure) {
	return dataCubeDao.relatedToAConceptMesure(iriConceptMesure);
    }

    @Override
    public List<DataCube> relatedToAConcept(String iriConcept) {
	return dataCubeDao.relatedToAConcept(iriConcept);
    }

    @Override
    public List<DataCube> relatedToAConceptScheme(String iriConceptScheme) {
	return dataCubeDao.relatedToAConceptScheme(iriConceptScheme);
    }

    @Override
    public List<ConceptMesure> trouverConceptsMesures(String iri) {
	return dataCubeDao.trouverConceptsMesures(iri);
    }

    @Override
    public List<Attribut> trouverAttributs(String iri) {
	return dataCubeDao.trouverAttributs(iri);
    }

    @Override
    public List<Dimension> trouverDimensions(String iri) {
	return dataCubeDao.trouverDimensions(iri);
    }

    @Override
    public DataCube trouverDatacubeParCodeAvecModalites(String code) {
	DataCube datacube = findByPredicat(BaseRDF.RMES, DCTERMSStr.IDENTIFIER, code);
	if (datacube != null) {
	    datacube.setAttributs(trouverAttributsAvecModalites(datacube.getIri()));
	    datacube.setDimensions(trouverDimensionsAvecModalites(datacube.getIri()));
	}
	return datacube;
    }

    @Override
    public DataCube trouverDatacubeParCodeSansModalites(String code) {
	DataCube datacube = findByPredicat(BaseRDF.RMES, DCTERMSStr.IDENTIFIER, code);
	if (datacube != null) {
	    datacube.setAttributs(dataCubeDao.trouverAttributs(datacube.getIri()));
	    datacube.setDimensions(dataCubeDao.trouverDimensions(datacube.getIri()));
	    datacube.setConceptsMesures(dataCubeDao.trouverConceptsMesures(datacube.getIri()));
	    datacube.setSlicesKeys(sliceKeyService.sliceKeyByDatacube(datacube.getIri()));
	}
	return datacube;
    }

    @Override
    public DataCube trouverDatacubeAvecModalites(String iri) {
	DataCube datacube = null;
	if (RDFConnection.isIri(iri)) {
	    datacube = findByIri(BaseRDF.RMES, iri);
	    datacube.setAttributs(trouverAttributsAvecModalites(iri));
	    datacube.setDimensions(trouverDimensionsAvecModalites(iri));
	    return datacube;
	}
	return datacube;
    }

    @Override
    public List<Dimension> trouverDimensionsAvecModalites(String iri) {
	List<Dimension> dimensions = dataCubeDao.trouverDimensions(iri);
	for (Dimension dimension : dimensions) {
	    dimension.setModalites(
		    modaliteService.filterByPredicat(BaseRDF.RMES, SKOSStr.IN_SCHEME, dimension.getIriConceptScheme()));
	}
	return dimensions;
    }

    @Override
    public List<Attribut> trouverAttributsAvecModalites(String iri) {
	List<Attribut> attributs = dataCubeDao.trouverAttributs(iri);
	for (Attribut attribut : attributs) {
	    attribut.setModalites(
		    modaliteService.filterByPredicat(BaseRDF.RMES, SKOSStr.IN_SCHEME, attribut.getIriConceptScheme()));
	}
	return attributs;
    }

    @Override
    public List<String> trouverConcepts() {
	return dataCubeDao.trouverConcepts();
    }

    @Override
    public void supprimerEnCascade(DataCube datacube) {
	dataCubeDao.supprimerEnCascade(datacube);
    }

}
