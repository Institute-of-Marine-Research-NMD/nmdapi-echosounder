

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Integration tests for biotic data.
 *
 * @author kjetilf
 */
public class TestEchosounderIT {

    @Test
    @Ignore
    public void testPostGetDeleteData() throws IOException, SAXException {
        System.out.println("testGetTables");
        WebConversation wc = new WebConversation();
        InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream("luf20.xml");
        PostMethodWebRequest postMethod = new PostMethodWebRequest("http://localhost:8080/apis/nmdapi/echosounder/v1/Forskningsfart%C3%B8y/2014/GOSars-LMEL/2014107", source, "application/xml");
        WebResponse response = wc.sendRequest(postMethod);
        System.out.println("testGetTables");
    }

}
