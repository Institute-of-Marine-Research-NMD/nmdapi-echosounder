package no.imr.nmdapi.nmdechosounder.service;

import no.imr.nmdapi.dao.file.NMDDatasetDao;
import no.imr.nmdapi.generic.nmdechosounder.domain.luf20.EchosounderDatasetType;
import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NMDEchosounder service layer implementation.
 *
 * @author kjetilf
 */
public class NMDEchosounderServiceImpl implements NMDEchosounderService {

    /**
     * Data type.
     */
    private static final String TYPE = "echosounder";

    /**
     * Dataset name.
     */
    private static final String DATASET_NAME = "data";

    @Autowired
    private NMDDatasetDao nmdDatasetDao;

    @Autowired
    private Configuration configuration;

    @Override
    public Object getData(final String missiontype, final String year, final String platform, final String delivery) {
        return nmdDatasetDao.get(TYPE, DATASET_NAME, EchosounderDatasetType.class.getPackage().getName(), missiontype, year, platform, delivery);
    }

    @Override
    public void deleteData(final String missiontype, final String year, final String platform, final String delivery) {
        nmdDatasetDao.delete(TYPE, DATASET_NAME, true, missiontype, year, platform, delivery);
    }

   @Override
    public void insertData(final String missiontype, final String year, final String platform, final String delivery, final EchosounderDatasetType dataset) {
        String readRole = configuration.getString("default.readrole");
        String writeRole = configuration.getString("default.writerole");
        String owner = configuration.getString("default.owner");
        nmdDatasetDao.insert(writeRole, readRole, owner, TYPE, DATASET_NAME, dataset, true, missiontype, year, platform, delivery);
    }


    @Override
    public void updateData(final String missiontype, final String year, final String platform, final String delivery, final EchosounderDatasetType dataset) {
        nmdDatasetDao.update(TYPE, DATASET_NAME, dataset, missiontype, year, platform, delivery);
    }

    @Override
    public boolean hasData(String missiontype, String year, String platform, String delivery) {
        return nmdDatasetDao.hasData(TYPE, DATASET_NAME, missiontype, year, platform, delivery);
    }

    @Override
    public Object getDataByCruiseNr(final String cruisenr) {
        return nmdDatasetDao.getByCruisenr(TYPE, DATASET_NAME, EchosounderDatasetType.class.getPackage().getName(), cruisenr);
    }

    @Override
    public boolean hasDataByCruiseNr(final String cruisenr) {
        return nmdDatasetDao.hasDataByCruisenr(TYPE, DATASET_NAME, EchosounderDatasetType.class.getPackage().getName(), cruisenr);
    }


}
