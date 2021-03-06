package no.imr.nmdapi.nmdechosounder.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import no.imr.framework.logging.slf4j.aspects.stereotype.ArgumentLogging;
import no.imr.framework.logging.slf4j.aspects.stereotype.PerformanceLogging;
import no.imr.nmdapi.exceptions.BadRequestException;
import no.imr.nmdapi.generic.nmdechosounder.domain.luf20.EchosounderDatasetType;
import no.imr.nmdapi.nmdechosounder.service.NMDEchosounderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author sjurl
 */
@Controller
public class EchosounderController {

    /**
     * Url part that defines it as echosounder.
     */
    public static final String ECHOSOUNDER_URL = "/echosounder";

    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EchosounderController.class);

    /**
     * Service layer object for nmd biotic queries.
     */
    @Autowired
    private NMDEchosounderService nmdEchosounderService;

    /**
     * Get echosounder data for mission.
     *
     * @param mission
     * @return Response object.
     */
    @PerformanceLogging
    @ArgumentLogging
    @RequestMapping(value = "/{missiontype}/{year}/{platform}/{delivery}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object findByMission(@PathVariable(value = "missiontype") String missiontype, @PathVariable(value = "year") String year, @PathVariable(value = "platform") String platform, @PathVariable(value = "delivery") String delivery) {
        LOGGER.info("Start EchosounderController.findByMission");
        return nmdEchosounderService.getData(missiontype, year, platform, delivery);
    }

    /**
     * Delete echosounder data for mission.
     *
     * @param mission
     */
    @PerformanceLogging
    @ArgumentLogging
    @RequestMapping(value = "/{missiontype}/{year}/{platform}/{delivery}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteByMission(@PathVariable(value = "missiontype") String missiontype, @PathVariable(value = "year") String year, @PathVariable(value = "platform") String platform, @PathVariable(value = "delivery") String delivery) {
        LOGGER.info("Start EchosounderController.deleteByMission");
        nmdEchosounderService.deleteData(missiontype, year, platform, delivery);
    }

    /**
     * Update echosounder data for mission.
     *
     * @param mission
     * @param echosounderDatasetType
     */
    @PerformanceLogging
    @ArgumentLogging
    @RequestMapping(value = "/{missiontype}/{year}/{platform}/{delivery}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void insertByMission(@PathVariable(value = "missiontype") String missiontype, @PathVariable(value = "year") String year, @PathVariable(value = "platform") String platform, @PathVariable(value = "delivery") String delivery, @RequestBody EchosounderDatasetType echosounderDatasetType) {
        LOGGER.info("Start EchosounderController.insertByMission");
        nmdEchosounderService.insertData(missiontype, year, platform, delivery, echosounderDatasetType);
    }

     /**
     * insert echosounder data for mission.
     *
     * @param mission
     * @param echosounderDatasetType
     */
    @PerformanceLogging
    @ArgumentLogging
    @RequestMapping(value = "/{missiontype}/{year}/{platform}/{delivery}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateByMission(@PathVariable(value = "missiontype") String missiontype, @PathVariable(value = "year") String year, @PathVariable(value = "platform") String platform, @PathVariable(value = "delivery") String delivery, @RequestBody EchosounderDatasetType echosounderDatasetType) {
        LOGGER.info("Start EchosounderController.updateByMission");
        nmdEchosounderService.updateData(missiontype, year, platform, delivery, echosounderDatasetType);
    }


    /**
     * Does the mission have data
     *
     * @param missiontype
     * @param year
     * @param platform
     * @param delivery
     * @return
     */
    @PerformanceLogging
    @ArgumentLogging
    @RequestMapping(value = "/{missiontype}/{year}/{platform}/{delivery}", method = RequestMethod.HEAD)
    @ResponseBody
    public void  hasData(HttpServletResponse httpServletResponse,@PathVariable(value = "missiontype") String missiontype, @PathVariable(value = "year") String year, @PathVariable(value = "platform") String platform, @PathVariable(value = "delivery") String delivery) {
        LOGGER.info("Start EchosounderController.hasData");
        if (nmdEchosounderService.hasData(missiontype, year, platform, delivery)){
           httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        } else {
         httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

        /**
     * Get data by id or cruise number.
     *
     * @param cruisenr
     * @return Response object.
     */
    @PerformanceLogging
    @ArgumentLogging
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object find(@RequestParam(value = "cruisenr", required = true) String cruisenr, @RequestParam(value = "shipname", required = true) String shipname, HttpServletRequest request) throws MalformedURLException, URISyntaxException {
        LOGGER.info("Start BioticController.find");
        if (cruisenr != null) {
            URI uri = (new URI(request.getRequestURL().toString())).resolve(".");
            return nmdEchosounderService.getDataByCruiseNr(cruisenr, shipname, uri.toString());
        } else {
            throw new BadRequestException("Cruisenr parameters must be set.");
        }
    }

    /**
     * Get data by id or cruise number.
     *
     * @param httpServletResponse
     * @param cruisenr
     */
    @PerformanceLogging
    @ArgumentLogging
    @RequestMapping(value = "/find", method = RequestMethod.HEAD)
    @ResponseBody
    public void find(HttpServletResponse httpServletResponse, @RequestParam(value = "cruisenr", required = false) String cruisenr, @RequestParam(value = "shipname", required = true) String shipname) {
        LOGGER.info("Start BioticController.find");
        if (nmdEchosounderService.hasDataByCruiseNr(cruisenr, shipname)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Get namepsace for data.
     *
     * @param missiontype
     * @param year
     * @param platform
     * @param delivery
     * @return
     */
    @PerformanceLogging
    @ArgumentLogging
    @RequestMapping(value = "/{missiontype}/{year}/{platform}/{delivery}", method = RequestMethod.GET, params = {"type=info"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object getInfo(@PathVariable(value = "missiontype") String missiontype, @PathVariable(value = "year") String year, @PathVariable(value = "platform") String platform, @PathVariable(value = "delivery") String delivery) {
        LOGGER.info("Start EchosounderController.getInfo");
        return nmdEchosounderService.getInfo(missiontype, year, platform, delivery);
    }

}
