package fr.insee.bidbo.ws.init;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;

@Provider
public class JsonMoxyConfigurationContextResolver implements ContextResolver<MoxyJsonConfig> {

    private final MoxyJsonConfig config;

    public JsonMoxyConfigurationContextResolver() {
	config = new MoxyJsonConfig().property(JAXBContextProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
    }

    @Override
    public MoxyJsonConfig getContext(Class<?> objectType) {
	return config;
    }
}