package fr.insee.bidbo.dao;

import java.util.List;

import fr.insee.bidbo.model.DataSet;
import fr.insee.bidbo.rdfinsee.GenericDao;

public interface DataSetDao extends GenericDao<DataSet> {

    List<DataSet> datasetFiltrees(BaseRDF base, AttachableComplement complement);

    List<DataSet> datasetFiltrees(BaseRDF base, AttachableComplement complement, int limit);

}
