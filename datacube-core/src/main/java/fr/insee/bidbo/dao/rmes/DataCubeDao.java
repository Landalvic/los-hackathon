package fr.insee.bidbo.dao.rmes;

import java.util.List;

import fr.insee.bidbo.model.rmes.Attribut;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.model.rmes.Dimension;
import fr.insee.bidbo.rdfinsee.GenericDao;

public interface DataCubeDao extends GenericDao<DataCube> {

    List<ConceptMesure> trouverConceptsMesures(String iri);

    List<Attribut> trouverAttributs(String iri);

    List<Dimension> trouverDimensions(String iri);

    List<String> trouverConcepts();

    List<DataCube> relatedToAConceptScheme(String iriConceptScheme);

    List<DataCube> relatedToAConcept(String iriConcept);

    List<DataCube> relatedToAConceptMesure(String iriConceptMesure);

    void supprimerEnCascade(DataCube datacube);

}
