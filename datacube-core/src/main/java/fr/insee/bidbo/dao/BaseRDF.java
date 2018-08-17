package fr.insee.bidbo.dao;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public enum BaseRDF {

    INTERNE,
    EXTERNE,
    RMES;

    private String urlServer;
    private String repository;

    private BaseRDF() {
    }

    private BaseRDF(String urlServer) {
	this.urlServer = urlServer;
	repository = null;
    }

    public String getUrlServer() {
	return urlServer;
    }

    public String getRepository() {
	return repository;
    }

    public boolean isHttp() {
	return repository != null;
    }

    @Component
    private static class Injecteur {
	@Value("${sesame.url.update.interne}")
	private String urlInterne;
	@Value("${sesame.repository.interne}")
	private String repositoryInterne;
	@Value("${sesame.url.update.externe}")
	private String urlExterne;
	@Value("${sesame.repository.externe}")
	private String repositoryExterne;
	@Value("${sesame.url.update.rmes}")
	private String urlRmes;
	@Value("${sesame.repository.rmes}")
	private String repositoryRmes;

	@PostConstruct
	public void postConstruct() {
	    INTERNE.urlServer = urlInterne;
	    INTERNE.repository = repositoryInterne;
	    EXTERNE.urlServer = urlExterne;
	    EXTERNE.repository = repositoryExterne;
	    RMES.urlServer = urlRmes;
	    RMES.repository = repositoryRmes;
	}
    }

}
