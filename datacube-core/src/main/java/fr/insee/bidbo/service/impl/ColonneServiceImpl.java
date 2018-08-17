package fr.insee.bidbo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.ColonneDao;
import fr.insee.bidbo.model.Colonne;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.ColonneService;

@Service
public class ColonneServiceImpl extends AbstractGenericService<Colonne> implements ColonneService {

    @Autowired
    private ColonneDao colonneDao;

    @Override
    public Colonne trouverParAltLabel(String altLabel) {
	return colonneDao.trouverParAltLabel(altLabel);
    }

    @Override
    protected GenericDao<Colonne> getDao() {
	return colonneDao;
    }

}
