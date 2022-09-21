package io.github.toquery.framework.front.connfig;

import io.github.toquery.framework.front.filter.CachingHttpHeadersFilter;
import io.github.toquery.framework.front.properties.AppFrontProperties;
import io.github.toquery.framework.web.properties.AppWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.EnumSet;

import static java.net.URLDecoder.decode;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Slf4j
public class AppFrontConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {

    @Autowired
    private Environment env;

    @Autowired
    private AppWebProperties appWebProperties;

    @Autowired
    private AppFrontProperties appFrontProperties;

    public AppFrontConfigurer() {
        log.info("初始化 App Front 配置");
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        EnumSet<DispatcherType> dispatcherTypeEnumSet = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        if (env.acceptsProfiles(Profiles.of("pro"))) {
            initCachingHttpHeadersFilter(servletContext, dispatcherTypeEnumSet);
        }
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            initH2Console(servletContext);
        }
        log.info("Web application fully configured");
    }

    /**
     * Initializes the caching HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext, EnumSet<DispatcherType> dispatcherTypes) {
        log.debug("Registering Caching HTTP Headers Filter");
        FilterRegistration.Dynamic cachingHttpHeadersFilter = servletContext.addFilter("cachingHttpHeadersFilter", new CachingHttpHeadersFilter(appFrontProperties.getTimeToLiveInDays()));

        cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/i18n/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/content/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/static/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/app/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }

    /**
     * Customize the Servlet engine: Mime types, the document root, the cache.
     */
    @Override
    public void customize(WebServerFactory server) {
        setMimeMappings(server);
        // When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
        setLocationForStaticAssets(server);
    }

    private void setMimeMappings(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory) {
            MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
            // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
            mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
            // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
            mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
            ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
            servletWebServer.setMimeMappings(mappings);
        }
    }


    private void setLocationForStaticAssets(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory) {
            ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
            File root;
            String prefixPath = resolvePathPrefix();
            // root = new File(prefixPath + "target/classes/static/");
            // 解决开发环境 下的 解析路径
            root = new File(prefixPath + appFrontProperties.getOutputDir());
            if (root.exists() && root.isDirectory()) {
                servletWebServer.setDocumentRoot(root);
            }
        }
    }

    /**
     * Resolve path prefix to static resources.
     */
    private String resolvePathPrefix() {
        String fullExecutablePath;
        try {
            fullExecutablePath = decode(this.getClass().getResource("").getPath(), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            /* try without decoding if this ever happens */
            fullExecutablePath = this.getClass().getResource("").getPath();
        }
        String rootPath = Paths.get(".").toUri().normalize().getPath();
        String extractedPath = fullExecutablePath.replace(rootPath, "");
        //  解决开发环境 下的 解析路径
        //  int extractionEndIndex = extractedPath.indexOf("target/");
        int extractionEndIndex = extractedPath.indexOf(appFrontProperties.getPrefix());
        if (extractionEndIndex <= 0) {
            return "";
        }
        return extractedPath.substring(0, extractionEndIndex);
    }


    @Bean
    public CorsFilter corsFilter(CorsConfiguration config) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/static/**", config);
            source.registerCorsConfiguration("/management/**", config);
            source.registerCorsConfiguration("/v2/api-docs", config);
        }
        return new CorsFilter(source);
    }

    /**
     * Initializes H2 console.
     */
    private void initH2Console(ServletContext servletContext) {
        log.debug("Initialize H2 console");
        // todo H2ConfigurationHelper
        // H2ConfigurationHelper.initH2Console(servletContext);
    }
}
