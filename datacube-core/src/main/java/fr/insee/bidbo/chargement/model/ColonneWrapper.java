package fr.insee.bidbo.chargement.model;

import fr.insee.bidbo.model.Colonne;

public class ColonneWrapper {

    private Colonne colonne;
    private String nomColonne;

    public ColonneWrapper() {
	super();
    }

    public ColonneWrapper(Colonne colonne, String nomColonne) {
	super();
	this.colonne = colonne;
	this.nomColonne = nomColonne;
    }

    public Colonne getColonne() {
	return colonne;
    }

    public void setColonne(Colonne colonne) {
	this.colonne = colonne;
    }

    public String getNomColonne() {
	return nomColonne;
    }

    public void setNomColonne(String nomColonne) {
	this.nomColonne = nomColonne;
    }

}
