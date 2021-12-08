package io.github.toquery.framework.webmvc.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.web.controller.AppBaseWebController;
import io.github.toquery.framework.web.dict.annotation.AppDict;
import io.github.toquery.framework.web.dict.AppDictRuntime;
import io.github.toquery.framework.webmvc.domain.ResponseBody;
import io.github.toquery.framework.webmvc.domain.ResponseBodyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/dict")
public class AppDictRest extends AppBaseWebController implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {


    private ApplicationContext applicationContext;

    private static Set<String> dictRuntimeClassPackages = Sets.newHashSet();
    private static Map<String, Class<? extends AppDictRuntime>> runtimeDictMap = Maps.newHashMap();

    /*public static void addDictRuntimeClassPackage(String dictRuntimeClassPackage) {
        if (!dictRuntimeClassPackages.contains(dictRuntimeClassPackage)) {
            Reflections reflections = new Reflections(dictRuntimeClassPackage, new Scanner[0]);
            Set<Class<? extends AppRuntimeDict>> appRuntimeDictClasses = reflections.getSubTypesOf(AppRuntimeDict.class);
            Iterator var3 = appRuntimeDictClasses.iterator();

            while (var3.hasNext()) {
                Class<? extends AppDictRuntime> appRuntimeDictClass = (Class) var3.next();
                AppDict appDict = (AppDict) AnnotationUtils.findAnnotation(appRuntimeDictClass, AppDict.class);
                String dictParentCode = appDict != null ? appDict.value() : null;
                if (Strings.isNullOrEmpty(dictParentCode)) {
                    log.info("运行时字典 {} 的rest地址访问为空", appRuntimeDictClass.getName());
                } else {
                    log.info("运行时字典 {} 的rest地址访问地址: {}", appRuntimeDictClass.getName(), "/dictionary/runtime/" + appDict.value());
                    Assert.isTrue(!runtimeDictMap.containsKey(dictParentCode), "运行时字典 " + dictParentCode + " 已经存在");
                    runtimeDictMap.put(dictParentCode, appRuntimeDictClass);
                }
            }

        }
    }*/

    public AppDictRest() {
       /* String[] packages = AppPackage.BASE_PACKAGES;
        for (int i = 0; i < packages.length; ++i) {
            String basePackage = packages[i];
            addDictRuntimeClassPackage(basePackage);
        }*/

    }

    @RequestMapping({"{code}"})
    public ResponseBody runtimeDictionary(@PathVariable String code, @RequestParam(required = false) Set<String> excludes, @RequestParam(required = false, defaultValue = "true") Boolean wrapped) {
        if (!runtimeDictMap.containsKey(code)) {
            return new ResponseBodyBuilder().fail().message("没有指定的字典项").build();
        }

        Class<?> runtimeDictClass = runtimeDictMap.get(code);
        if (!runtimeDictClass.isEnum()) {
            return new ResponseBodyBuilder().fail().message("运行时字典必须为枚举类型").build();
        }

        AppDictRuntime[] appRuntimeDicts = null;

        try {
            Method valuesMethod = runtimeDictClass.getMethod("values");
            appRuntimeDicts = (AppDictRuntime[]) ((AppDictRuntime[]) valuesMethod.invoke((Object) null, (Object[]) null));
        } catch (Exception exception) {
            return new ResponseBodyBuilder().fail().message("运行时字典必须为枚举类型, " + exception.getMessage()).build();
        }

        ResponseBodyBuilder responseParamBuilder = new ResponseBodyBuilder();
        if (appRuntimeDicts != null) {
            List<Map<String, String>> dictList = Lists.newArrayListWithExpectedSize(appRuntimeDicts.length);
            Arrays.stream(appRuntimeDicts).filter((appRuntimeDict) -> !excludes.contains(appRuntimeDict.name())).forEach((appRuntimeDict) -> {
                Map<String, String> item = Maps.newHashMapWithExpectedSize(2);
                item.put("code", appRuntimeDict.name());
                item.put("name", appRuntimeDict.getRemark());
                dictList.add(item);
            });
            responseParamBuilder.content(dictList);
        }

        return responseParamBuilder.build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        runtimeDictMap = Maps.newHashMap();

        Class<? extends AppDict> annotationClass = AppDict.class;
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);

        beansWithAnnotation.forEach((key, val) -> {

            Class<? extends AppDictRuntime> clazz = (Class) val.getClass();//获取bean对象
            // fixme
            //  AppDict componentDesc = AnnotationUtils.findAnnotation(clazz, AppDict.class);
            // runtimeDictMap.put(componentDesc.value(), clazz);
        });
    }
}

