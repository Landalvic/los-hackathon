package fr.insee.bidbo.ws.init;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.jaxrs.config.BeanConfig;

public class Bootstrap extends HttpServlet {

    private static final long serialVersionUID = -7751888565295704267L;

    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);

	BeanConfig beanConfig = new BeanConfig();
	beanConfig.setVersion("0.1");
	beanConfig.setSchemes(new String[] { "http" });
	beanConfig.setResourcePackage("fr.insee.bidbo.ws");
	beanConfig.setScan(true);
	beanConfig.setBasePath(config.getServletContext().getContextPath() + "/api");
    }
}
