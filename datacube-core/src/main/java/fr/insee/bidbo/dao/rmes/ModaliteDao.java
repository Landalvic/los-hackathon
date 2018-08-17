package fr.insee.bidbo.dao.rmes;

import java.util.List;

import fr.insee.bidbo.dao.Parametres;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.rdfinsee.GenericDao;

public interface ModaliteDao extends GenericDao<Modalite> {

    List<Modalite> modalites(Parametres parametres);

    List<Modalite> modalitesByConceptScheme(String code);

}
