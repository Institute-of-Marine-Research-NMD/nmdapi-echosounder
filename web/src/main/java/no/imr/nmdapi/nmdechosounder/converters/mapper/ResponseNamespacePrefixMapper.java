package no.imr.nmdapi.nmdechosounder.converters.mapper;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 *
 * @author kjetilf
 */
public class ResponseNamespacePrefixMapper extends NamespacePrefixMapper {

    public static final String RESPONSE_NS = "http://www.imr.no/formats/nmdapi/responses/v1";

    @Override
    public String getPreferredPrefix(String namespaceUri,
                               String suggestion,
                               boolean requirePrefix) {
        return "";
    }

}
