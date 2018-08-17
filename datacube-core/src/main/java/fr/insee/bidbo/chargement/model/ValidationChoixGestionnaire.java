package fr.insee.bidbo.chargement.model;

import java.util.List;

public class ValidationChoixGestionnaire {

    private String lienFichier;
    private String datacube;
    private List<ChoixGestionnaire> choixGestionnaire;
    private List<ChoixParDefaut> choixParDefaut;

    public String getDatacube() {
	return datacube;
    }

    public void setDatacube(String datacube) {
	this.datacube = datacube;
    }

    public String getLienFichier() {
	return lienFichier;
    }

    public void setLienFichier(String lienFichier) {
	this.lienFichier = lienFichier;
    }

    public List<ChoixGestionnaire> getChoixGestionnaire() {
	return choixGestionnaire;
    }

    public void setChoixGestionnaire(List<ChoixGestionnaire> choixGestionnaire) {
	this.choixGestionnaire = choixGestionnaire;
    }

    public List<ChoixParDefaut> getChoixParDefaut() {
	return choixParDefaut;
    }

    public void setChoixParDefaut(List<ChoixParDefaut> choixParDefaut) {
	this.choixParDefaut = choixParDefaut;
    }

    public static class ChoixGestionnaire {

	private String nomColonne;
	private String choix;

	public ChoixGestionnaire() {
	    super();
	}

	public ChoixGestionnaire(String nomColonne, String choix) {
	    super();
	    this.nomColonne = nomColonne;
	    this.choix = choix;
	}

	public String getNomColonne() {
	    return nomColonne;
	}

	public void setNomColonne(String nomColonne) {
	    this.nomColonne = nomColonne;
	}

	public String getChoix() {
	    return choix;
	}

	public void setChoix(String choix) {
	    this.choix = choix;
	}

    }

    public static class ChoixParDefaut {

	private String iri;
	private String choix;

	public ChoixParDefaut() {
	    super();
	}

	public ChoixParDefaut(String iri, String choix) {
	    super();
	    this.iri = iri;
	    this.choix = choix;
	}

	public String getIri() {
	    return iri;
	}

	public void setIri(String iri) {
	    this.iri = iri;
	}

	public String getChoix() {
	    return choix;
	}

	public void setChoix(String choix) {
	    this.choix = choix;
	}

    }

}
