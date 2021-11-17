package io.github.toquery.framework.system.runner;

import io.github.toquery.framework.system.properties.AppSystemProperties;
import io.github.toquery.framework.system.service.ISysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 *
 */
@Slf4j
public class AppSystemRunner implements ApplicationRunner {

    @Autowired
    private AppSystemProperties appSystemProperties;

    @Autowired
    private ISysMenuService sysMenuService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AppSystemRunner run");
        boolean generateMenu = appSystemProperties.isGenerateMenu();
        if (!generateMenu) {
            log.info("将不会自动生成菜单 generateMenu is false");
            return;
        }
        sysMenuService.scan();
    }
}
