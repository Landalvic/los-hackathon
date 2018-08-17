package fr.insee.bidbo.chargement.model;

import java.util.List;

public class InfosFichier {

    private List<ColonneWrapper> colonnes;
    private String lienFichier;

    public String getLienFichier() {
	return lienFichier;
    }

    public void setLienFichier(String lienFichier) {
	this.lienFichier = lienFichier;
    }

    public List<ColonneWrapper> getColonnes() {
	return colonnes;
    }

    public void setColonnes(List<ColonneWrapper> colonnes) {
	this.colonnes = colonnes;
    }

}
