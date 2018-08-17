package fr.insee.bidbo.dao;

import java.util.List;

import fr.insee.bidbo.model.Slice;
import fr.insee.bidbo.rdfinsee.GenericDao;

public interface SliceDao extends GenericDao<Slice> {

    List<Slice> seriesFiltrees(BaseRDF base, AttachableComplement complement);

    List<Slice> seriesFiltrees(BaseRDF base, AttachableComplement complement, int limit);

}
