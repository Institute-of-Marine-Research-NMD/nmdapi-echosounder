package no.imr.nmdapi.nmdechosounder.service.config;

import no.imr.nmdapi.nmdechosounder.service.NMDEchosounderService;
import no.imr.nmdapi.nmdechosounder.service.NMDEchosounderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This contains all configuration for the reference services.
 *
 * @author kjetilf
 */
@Configuration
public class NMDCruiseServiceConfig {

    /**
     * Creates the service implementation.
     *
     * @return  A reference service implementation.
     */
    @Bean(name="nmdCruiseService")
    public NMDEchosounderService getNMDCruiseService() {
        return new NMDEchosounderServiceImpl();
    }

}
