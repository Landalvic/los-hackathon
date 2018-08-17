package fr.insee.bidbo.dao;

import java.util.List;
import java.util.Map;

import fr.insee.bidbo.model.rmes.DataCube;

public class AttachableComplement {

    private DataCube datacube;
    private Map<String, List<String>> modalites;
    private String timePeriod;

    public AttachableComplement(DataCube datacube, Map<String, List<String>> modalites, String timePeriod) {
	super();
	this.datacube = datacube;
	this.modalites = modalites;
	this.timePeriod = timePeriod;
    }

    public String getTimePeriod() {
	return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
	this.timePeriod = timePeriod;
    }

    public DataCube getDatacube() {
	return datacube;
    }

    public void setDatacube(DataCube datacube) {
	this.datacube = datacube;
    }

    public Map<String, List<String>> getModalites() {
	return modalites;
    }

    public void setModalites(Map<String, List<String>> modalites) {
	this.modalites = modalites;
    }

}
