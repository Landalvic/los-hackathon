package fr.insee.bidbo.service.rmes;

import java.util.List;

import fr.insee.bidbo.model.rmes.Attribut;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.model.rmes.Dimension;
import fr.insee.bidbo.rdfinsee.GenericService;

public interface DataCubeService extends GenericService<DataCube> {

    List<ConceptMesure> trouverConceptsMesures(String iri);

    List<Attribut> trouverAttributs(String iri);

    List<Dimension> trouverDimensions(String iri);

    List<Dimension> trouverDimensionsAvecModalites(String iri);

    DataCube trouverDatacubeAvecModalites(String iri);

    List<Attribut> trouverAttributsAvecModalites(String iri);

    DataCube trouverDatacubeParCodeAvecModalites(String code);

    List<String> trouverConcepts();

    List<DataCube> relatedToAConceptScheme(String iriConceptScheme);

    DataCube trouverDatacubeParCodeSansModalites(String code);

    List<DataCube> relatedToAConceptMesure(String iriConceptMesure);

    List<DataCube> relatedToAConcept(String iriConcept);

    void supprimerEnCascade(DataCube datacube);

}
