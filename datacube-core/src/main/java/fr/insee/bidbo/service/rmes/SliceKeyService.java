package fr.insee.bidbo.service.rmes;

import java.util.List;

import fr.insee.bidbo.model.rmes.SliceKey;
import fr.insee.bidbo.rdfinsee.GenericService;

public interface SliceKeyService extends GenericService<SliceKey> {

    List<SliceKey> sliceKeyByDatacube(String iriDatacube);

}
