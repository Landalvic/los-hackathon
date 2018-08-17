package fr.insee.bidbo.service;

import java.util.List;

import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.model.Slice;
import fr.insee.bidbo.rdfinsee.GenericService;

public interface SliceService extends GenericService<Slice> {

    List<Slice> seriesFiltrees(BaseRDF base, AttachableComplement complement);

    List<Slice> seriesFiltrees(BaseRDF base, AttachableComplement complement, int limit);

}
