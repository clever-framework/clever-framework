package io.github.toquery.framework.system.runner;

import io.github.toquery.framework.system.properties.AppSystemProperties;
import io.github.toquery.framework.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class AppSystemRunner implements ApplicationRunner {

    private final AppSystemProperties appSystemProperties;

    private final ISysMenuService sysMenuService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AppSystemRunner run");
        boolean generateMenu = appSystemProperties.isGenerateMenu();
        if (!generateMenu) {
            log.info("将不会自动生成菜单 generateMenu is false");
            return;
        }
        // sysMenuService.scanAndInsertMenus(generateMenu);
    }
}
