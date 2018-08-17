package fr.insee.bidbo.service;

import java.util.List;

import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.model.DataSet;
import fr.insee.bidbo.rdfinsee.GenericService;

public interface DataSetService extends GenericService<DataSet> {

    List<DataSet> datasetFiltrees(BaseRDF base, AttachableComplement complement);

    List<DataSet> datasetFiltrees(BaseRDF base, AttachableComplement complement, int limit);

}
