package fr.insee.bidbo.model;

import java.util.List;

public interface Attachable {

    List<CodeVariableModalite> getAttributs();

    void setAttributs(List<CodeVariableModalite> attributs);

    List<CodeVariableModalite> getDimensions();

    void setDimensions(List<CodeVariableModalite> dimensions);

}
