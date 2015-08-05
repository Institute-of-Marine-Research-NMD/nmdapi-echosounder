package no.imr.nmdapi.nmdechosounder.service;

import no.imr.nmdapi.dao.file.NMDDataDao;
import no.imr.nmdapi.generic.nmdechosounder.domain.luf20.EchosounderDatasetType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NMDEchosounder service layer implementation.
 *
 * @author kjetilf
 */
public class NMDEchosounderServiceImpl implements NMDEchosounderService {

    @Autowired
    private NMDDataDao nmdDataDao;

    @Override
    public Object getData(final String missiontype, final String year, final String platform, final String delivery) {
        return nmdDataDao.get(missiontype, year, platform, delivery, EchosounderDatasetType.class);
    }

    @Override
    public void deleteData(final String missiontype, final String year, final String platform, final String delivery) {
        nmdDataDao.delete(missiontype, year, platform, delivery);
    }

   @Override
    public void insertData(final String missiontype, final String year, final String platform, final String delivery, final EchosounderDatasetType dataset) {
        nmdDataDao.insert(missiontype, year, platform, delivery, dataset, EchosounderDatasetType.class);
    }


    @Override
    public void updateData(final String missiontype, final String year, final String platform, final String delivery, final EchosounderDatasetType dataset) {
        nmdDataDao.update(missiontype, year, platform, delivery, dataset, EchosounderDatasetType.class);
    }

    @Override
    public boolean hasData(String missiontype, String year, String platform, String delivery) {
        return nmdDataDao.hasData(missiontype, year, platform, delivery);
    }

}
