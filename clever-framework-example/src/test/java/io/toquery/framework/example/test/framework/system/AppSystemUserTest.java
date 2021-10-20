package io.toquery.framework.example.test.framework.system;

import com.google.common.collect.Sets;
import io.toquery.framework.example.test.BaseSpringMvcTest;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class AppSystemUserTest extends BaseSpringMvcTest {

    @Autowired
    private ISysRoleService sysRoleService;


    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void updateUserRole() throws Exception {

        SysUser sysUser = (SysUser)userDetailsService.loadUserByUsername("admin");

        List<SysRole> sysRoleList = sysRoleService.find();

        Set<SysRole> set = Sets.newHashSet(sysRoleList.get(0));


        log.info("{}",JacksonUtils.object2String(set));


        mockMvc.perform(put("/sys/user")
                .param("rootPwd", appProperties.getRootPassword())
                .content(JacksonUtils.object2String(sysUser))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

    }


}
