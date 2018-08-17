package fr.insee.bidbo.dao;

import java.util.List;
import java.util.Map;

public class Parametres {

    private Map<String, List<String>> modalites;
    private String source;

    public Map<String, List<String>> getModalites() {
	return modalites;
    }

    public void setModalites(Map<String, List<String>> modalites) {
	this.modalites = modalites;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

}
