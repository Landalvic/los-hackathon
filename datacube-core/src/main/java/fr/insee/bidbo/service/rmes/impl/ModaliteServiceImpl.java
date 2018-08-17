package fr.insee.bidbo.service.rmes.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.Parametres;
import fr.insee.bidbo.dao.rmes.ModaliteDao;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.rmes.ModaliteService;

@Service
public class ModaliteServiceImpl extends AbstractGenericService<Modalite> implements ModaliteService {

    @Autowired
    private ModaliteDao modaliteDao;

    @Override
    protected GenericDao<Modalite> getDao() {
	return modaliteDao;
    }

    @Override
    public List<Modalite> modalites(Parametres parametres) {
	return modaliteDao.modalites(parametres);
    }

    @Override
    public List<Modalite> modalitesByConceptScheme(String code) {
	return modaliteDao.modalitesByConceptScheme(code);
    }
}
