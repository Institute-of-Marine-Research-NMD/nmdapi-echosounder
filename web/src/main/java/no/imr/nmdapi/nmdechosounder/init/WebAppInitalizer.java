package no.imr.nmdapi.nmdechosounder.init;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import no.imr.framework.logging.logback.initalize.InitalizeLogbackHandler;
import no.imr.framework.logging.slf4j.exceptions.LoggerInitalizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;
/**
 * Initalize application with dispatcher servlet.
 *
 * @author kjetilf
 */
public class WebAppInitalizer extends AbstractDispatcherServletInitializer {

    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebAppInitalizer.class);

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.scan("no.imr.nmdapi.nmdechosounder.controller");
        ctx.setConfigLocation("classpath:imrAspectsContext.xml");
        return ctx;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        XmlWebApplicationContext cxt = new XmlWebApplicationContext();
        String[] locations = {"classpath:/applicationContext.xml", "classpath:imrAspectsContext.xml"};
        cxt.setConfigLocations(locations);
        return cxt;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        Dynamic mdcFilter = servletContext.addFilter("MDCInsertingServletFilter", ch.qos.logback.classic.helpers.MDCInsertingServletFilter.class);
        mdcFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

        Dynamic securityFilter = servletContext.addFilter(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, DelegatingFilterProxy.class);
        securityFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

        Dynamic encodingFilter = servletContext.addFilter("encodingFilter", org.springframework.web.filter.CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

        try {
            InitalizeLogbackHandler.getInstance().initalize(System.getProperty("catalina.base") + "/conf/nmdapi_echosounder_logback_v1.xml", true);
        } catch (LoggerInitalizationException ex) {
            LOGGER.error("Logging initializaton failed.", ex);
        }
        LOGGER.info("Entering application.");
    }

}
