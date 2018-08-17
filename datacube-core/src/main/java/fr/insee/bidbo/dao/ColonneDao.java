package fr.insee.bidbo.dao;

import fr.insee.bidbo.model.Colonne;
import fr.insee.bidbo.rdfinsee.GenericDao;

public interface ColonneDao extends GenericDao<Colonne> {

    Colonne trouverParAltLabel(String altLabel);

}
