package fr.insee.bidbo.dao.rmes;

import java.util.List;

import fr.insee.bidbo.model.rmes.SliceKey;
import fr.insee.bidbo.rdfinsee.GenericDao;

public interface SliceKeyDao extends GenericDao<SliceKey> {

    List<SliceKey> sliceKeyByDatacube(String iriDatacube);

}
