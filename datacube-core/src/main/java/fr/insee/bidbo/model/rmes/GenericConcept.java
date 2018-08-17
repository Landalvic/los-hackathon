package fr.insee.bidbo.model.rmes;

import java.util.List;

public interface GenericConcept {

    String getIri();

    void setIri(String iri);

    String getCode();

    void setCode(String code);

    String getLibelleFr();

    void setLibelleFr(String libelleFr);

    String getLibelleEn();

    void setLibelleEn(String libelleEn);

    List<DataCube> getDatacubes();

    void setDatacubes(List<DataCube> datacubes);

    boolean isMesure();

    void setMesure(boolean mesure);

}
