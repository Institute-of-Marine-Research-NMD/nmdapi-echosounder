package no.imr.nmdapi.nmdechosounder.full;

import java.io.File;
import no.imr.nmdapi.nmdechosounder.controller.EchosounderController;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration
public class TestFull {

    private MockMvc mockMvc;

    @Autowired
    private EchosounderController controller;

    @Mock
    private Configuration configuration;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        FileUtils.deleteDirectory(new File(System.getProperty("java.io.tmpdir") + File.separator + "Forskningsfartøy"));

        MockitoAnnotations.initMocks(this);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreComments(true);
        doReturn(System.getProperty("java.io.tmpdir") + File.separator).when(configuration).getString("pre.data.dir");
        doReturn("biotic").when(configuration).getString("post.data.dir");
        doReturn("unrestricted").when(configuration).getString("default.readrole");
        doReturn("SG-FAG-CRUISE-WRITE").when(configuration).getString("default.writerole");
        doReturn("imr").when(configuration).getString("default.owner");
        doReturn("no.imr.nmd.commons.dataset.jaxb:no.imr.nmd.commons.cruise.jaxb").when(configuration).getString("app.packages");
        doReturn("SG-FAG-430-NMD").when(configuration).getString("admin.role");
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFull() throws Exception {
        //Get data and verify that nothing is there.
        mockMvc.perform(get("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201").characterEncoding("UTF-8")).andExpect(status().isNotFound());
        //Insert data.
        mockMvc.perform(
                post("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201")
                .contentType(MediaType.APPLICATION_XML)
                .characterEncoding("UTF-8")
                .content(FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("luf20_1.xml").getFile()), "UTF-8"))
                )
                .andDo(print())
                .andExpect(status().isOk());
        // Verify that data is there.
        final String insertedFile = FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("luf20_1.xml").getFile()), "UTF-8");
        mockMvc.perform(get("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201").characterEncoding("UTF-8")).andExpect(status().isOk()).andExpect(content().xml(insertedFile));

        // Update data.
        mockMvc.perform(
                put("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201")
                .contentType(MediaType.APPLICATION_XML)
                .characterEncoding("UTF-8")
                .content(FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("luf20_2.xml").getFile()), "UTF-8"))
                )
                .andExpect(status().isOk());

        // Verify that data is there.
        String updatedFile = FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("luf20_2.xml").getFile()), "UTF-8");
        mockMvc.perform(get("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201").characterEncoding("UTF-8")).andExpect(status().isOk()).andExpect(content().xml(updatedFile));

        //Test get information
        mockMvc.perform(get("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201?type=info").characterEncoding("UTF-8")).andExpect(status().isOk()).andDo(print());

        //Delete data.
        mockMvc.perform(delete("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201").characterEncoding("UTF-8")).andExpect(status().isOk());
        //Get data and verify that nothing is there.
        mockMvc.perform(get("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201").characterEncoding("UTF-8")).andExpect(status().isNotFound());
    }

    @Test
    public void testFailure() throws Exception {
        //Insert data.
        mockMvc.perform(
                post("/Forskningsfartøy/2014/Johan Hjort-LDGJ/2014201")
                .contentType(MediaType.APPLICATION_XML)
                .characterEncoding("UTF-8")
                .content(FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("luf20_failure.xml").getFile()), "UTF-8"))
                )
                .andExpect(status().isBadRequest());
    }

}
