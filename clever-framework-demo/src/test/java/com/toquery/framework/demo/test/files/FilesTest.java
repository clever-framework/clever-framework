package com.toquery.framework.demo.test.files;

import com.toquery.framework.demo.test.BaseSpringTest;
import io.github.toquery.framework.files.rest.AppFilesRest;
import io.github.toquery.framework.files.service.ISysFilesService;
import io.github.toquery.framework.files.service.impl.SysFilesServiceImpl;
import io.toquery.framework.test.AppTestSpringMvcBase;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = AppFilesRest.class)
public class FilesTest extends AppTestSpringMvcBase {


    // @MockBean
    // private SysFilesServiceImpl service;

//    @Autowired
//    private AppFilesRest appFilesRest;

    @Test
    public void testUploadFiles() throws Exception {
       // when(service.greet()).thenReturn("Hello Mock");

        MockMultipartFile jsonFile = new MockMultipartFile("file", "test.json", "application/json", "{\"key1\": \"value1\"}".getBytes());


        mockMvc.perform(
                multipart("/app/files/upload").file(jsonFile)
                        .param("fileStoreType","DB")).andDo(print()).andExpect(status().isOk());
    }
}
