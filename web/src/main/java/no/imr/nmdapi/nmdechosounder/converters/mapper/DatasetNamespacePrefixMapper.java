package no.imr.nmdapi.nmdechosounder.converters.mapper;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Echosounder namespace prefix mapper.
 *
 * @author kjetilf
 */
public class DatasetNamespacePrefixMapper extends NamespacePrefixMapper {

    public static final String ECHO_NS = "http://www.imr.no/formats/nmddataset/v1";

    @Override
    public String getPreferredPrefix(String namespaceUri,
                               String suggestion,
                               boolean requirePrefix) {
        return "";
    }

}
