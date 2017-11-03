package no.imr.nmdapi.nmdechosounder.full;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

/**
 * These tests test the entire application except security.
 *
 * @author kjetilf
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@WebAppConfiguration
public class TestActual {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        FileUtils.deleteDirectory(new File(System.getProperty("java.io.tmpdir") + File.separator + "Forskningsfartøy" + File.separator));

        MockitoAnnotations.initMocks(this);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreComments(true);
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test2012111() throws Exception {
        //Get data and verify that nothing is there.
        mockMvc.perform(get("/Forskningsfartøy/2012/G O Sars-LMEL/2012111").characterEncoding("UTF-8")).andExpect(status().isNotFound());

        //Insert data.
        mockMvc.perform(
                post("/Forskningsfartøy/2012/G O Sars-LMEL/2012111")
                .contentType(MediaType.APPLICATION_XML)
                .characterEncoding("UTF-8")
                .content(FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("2011102.xml").getFile()), "UTF-8"))
                )
                .andDo(print())
                .andExpect(status().isOk());

        // Verify that data is there.
        final String insertedFile = FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("2011102.xml").getFile()), "UTF-8");
        mockMvc.perform(get("/Forskningsfartøy/2012/G O Sars-LMEL/2012111").characterEncoding("UTF-8")).andExpect(status().isOk()).andExpect(content().xml(insertedFile));

        //Delete data.
        mockMvc.perform(delete("/Forskningsfartøy/2012/G O Sars-LMEL/2012111").characterEncoding("UTF-8")).andExpect(status().isOk());
        //Get data and verify that nothing is there.
        mockMvc.perform(get("/Forskningsfartøy/2012/G O Sars-LMEL/2012111").characterEncoding("UTF-8")).andExpect(status().isNotFound());
    }

}
