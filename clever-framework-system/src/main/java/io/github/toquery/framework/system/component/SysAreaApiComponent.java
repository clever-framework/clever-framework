package io.github.toquery.framework.system.component;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * List<Area> list = apiScv.getData(null, 1, new Area());
 */
@Slf4j
public class SysAreaApiComponent {

    public static final String key = "GIPBZ-S6M3F-SC6JD-NXREU-YAZ6E-5PB6Y";

    private final static Set<String> zxs = new HashSet<>();

    @Resource
    private RestTemplate restTemplate;

    public SysAreaApiComponent() {
        zxs.add("11");// 北京
        zxs.add("12");// 天津
        zxs.add("31");// 上海
        zxs.add("50");// 重庆
        zxs.add("81");// 香港
        zxs.add("82");// 澳门

    }

    public WarpDTO getchildren(String id, String key) {
        return restTemplate.getForObject("http://apis.map.qq.com/ws/district/v1/getchildren?id=${id}&key=${key}", WarpDTO.class, id, key);
    }

    @Data
    public class WarpDTO {
        private Integer status;
        private String message;
        private String data_version;
        private List<List<Area>> result;
    }

    @Data
    public class Area implements Serializable {
        private String id;
        private Integer level;
        private String parentId;
        private String parentIds;
        private String fullname;
        private List<Area> child;
    }


    public List<Area> getData(String pid, int level, Area parent) {

        log.info(" pid {} level {} parent {}", pid, level, parent.getParentIds());
        WarpDTO warpDTO = new WarpDTO();
        try {
            warpDTO = this.getchildren(pid, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (warpDTO.getStatus() != 0) {
            List<List<Area>> result = Lists.newArrayList(Lists.newArrayList());
            warpDTO.setResult(result);

            log.error(" pid {} level {} parent {} warpDTO {} {}", pid, level, parent.getParentIds(),warpDTO.getStatus(), warpDTO.getMessage());
        }

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info(" pid {} status {} msg {}", pid, warpDTO.getStatus(), warpDTO.getMessage());
        List<List<Area>> list2 = warpDTO.getResult();
        if (list2 == null || list2.size() <=0){
            return Lists.newArrayList();
        }

        List<Area> list = list2.get(0);
        int lz = pid == null ? 4 : (zxs.contains(pid.substring(0, 2)) ? 3 : 4); // 直辖市 3级、其他4级


        list.forEach(item -> {
            item.setLevel(level);
            String parentIds = null;
            if (pid != null) {
                if (parent.getParentIds() != null) {
                    parentIds = parent.getParentIds() + "," + pid;
                } else {
                    parentIds = pid;
                }
            }
            item.setParentIds(parentIds);
            item.setParentId(pid);
            if (level < lz) {
                List<Area> itemList = this.getData(item.getId(), level + 1, item);
                item.setChild(itemList);
            }
            // bj,xc,jd
            // sd,jn,lx,jfl
        });

        return list;
    }
}
