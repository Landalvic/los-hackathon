package fr.insee.bidbo.service.rmes;

import java.util.List;

import fr.insee.bidbo.dao.Parametres;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.rdfinsee.GenericService;

public interface ModaliteService extends GenericService<Modalite> {

    List<Modalite> modalites(Parametres parametres);

    List<Modalite> modalitesByConceptScheme(String code);

}
