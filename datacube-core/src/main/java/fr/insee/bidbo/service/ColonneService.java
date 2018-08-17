package fr.insee.bidbo.service;

import fr.insee.bidbo.model.Colonne;
import fr.insee.bidbo.rdfinsee.GenericService;

public interface ColonneService extends GenericService<Colonne> {

    Colonne trouverParAltLabel(String altLabel);

}
