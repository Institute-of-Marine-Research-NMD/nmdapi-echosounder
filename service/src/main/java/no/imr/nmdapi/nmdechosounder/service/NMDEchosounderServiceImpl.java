package no.imr.nmdapi.nmdechosounder.service;

import no.imr.nmd.commons.dataset.jaxb.DataTypeEnum;
import no.imr.nmdapi.dao.file.NMDDatasetDao;
import no.imr.nmdapi.generic.nmdechosounder.domain.luf20.EchosounderDatasetType;
import no.imr.nmdapi.generic.response.v1.OptionKeyValueListType;
import no.imr.nmdapi.generic.response.v1.OptionKeyValueType;
import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NMDEchosounder service layer implementation.
 *
 * @author kjetilf
 */
public class NMDEchosounderServiceImpl implements NMDEchosounderService {

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
        return nmdDatasetDao.get(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, missiontype, year, platform, delivery);
    }

    @Override
    public void deleteData(final String missiontype, final String year, final String platform, final String delivery) {
        nmdDatasetDao.delete(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, true, missiontype, year, platform, delivery);
    }

   @Override
    public void insertData(final String missiontype, final String year, final String platform, final String delivery, final EchosounderDatasetType dataset) {
        String readRole = configuration.getString("default.readrole");
        String writeRole = configuration.getString("default.writerole");
        String owner = configuration.getString("default.owner");
        nmdDatasetDao.insert(writeRole, readRole, owner, DataTypeEnum.ECHOSOUNDER, DATASET_NAME, dataset, true, missiontype, year, platform, delivery);
    }


    @Override
    public void updateData(final String missiontype, final String year, final String platform, final String delivery, final EchosounderDatasetType dataset) {
        nmdDatasetDao.update(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, dataset, missiontype, year, platform, delivery);
    }

    @Override
    public boolean hasData(String missiontype, String year, String platform, String delivery) {
        return nmdDatasetDao.hasData(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, missiontype, year, platform, delivery);
    }

    @Override
    public Object getDataByCruiseNr(final String cruisenr) {
        return nmdDatasetDao.getByCruisenr(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, cruisenr);
    }

    @Override
    public boolean hasDataByCruiseNr(final String cruisenr) {
        return nmdDatasetDao.hasDataByCruisenr(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, cruisenr);
    }

    @Override
    public Object getInfo(String missiontype, String year, String platform, String delivery) {
        String format = nmdDatasetDao.getRootNamespace(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, missiontype, year, platform, delivery);
        long checksum = nmdDatasetDao.getChecksum(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, missiontype, year, platform, delivery);
        long lastModified = nmdDatasetDao.getLastModified(DataTypeEnum.ECHOSOUNDER, DATASET_NAME, missiontype, year, platform, delivery);
        OptionKeyValueListType keyValueListType = new OptionKeyValueListType();
        keyValueListType.getElement().add(getOptionKeyValueType("format", format));
        keyValueListType.getElement().add(getOptionKeyValueType("checksum", String.valueOf(checksum)));
        keyValueListType.getElement().add(getOptionKeyValueType("lastModified", String.valueOf(lastModified)));
        return keyValueListType;
    }

    private OptionKeyValueType getOptionKeyValueType(String key, String value) {
        OptionKeyValueType formatType = new OptionKeyValueType();
        formatType.setKey(key);
        formatType.setValue(value);
        return formatType;
    }
}
