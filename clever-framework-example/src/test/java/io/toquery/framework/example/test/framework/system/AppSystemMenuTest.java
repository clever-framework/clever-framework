package io.toquery.framework.example.test.framework.system;

import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.system.service.ISysMenuService;
import io.toquery.framework.example.test.BaseSpringMvcTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class AppSystemMenuTest extends BaseSpringMvcTest {

    @Autowired
    private ISysMenuService sysMenuService;


    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void updateUserRole() throws Exception {

        // SysUser sysUser = (SysUser) userDetailsService.loadUserByUsername("admin");

        mockMvc.perform(post("/sys/menu/scan"))
                .andDo(print())
                .andExpect(status().isOk());

    }


}
