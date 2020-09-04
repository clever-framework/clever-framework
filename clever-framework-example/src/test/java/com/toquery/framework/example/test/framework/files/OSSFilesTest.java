package com.toquery.framework.example.test.framework.files;

import io.toquery.framework.test.AppTestSpringMvcBase;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = AppFilesRest.class)
public class OSSFilesTest extends AppTestSpringMvcBase {


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
