package com.xz.location.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xz.location.dao.CrowdMapper;
import com.xz.location.pojo.Crowd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/crowdmap")

public class CrowdMapController {
    @Resource
    private Properties configs;
    @Autowired
    private CrowdMapper crowdMapper;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").serializeNulls().create();

    @RequestMapping(value = "crowdmap", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String crowdmap(@RequestParam(value = "assetsType", required = false, defaultValue = "led") String assetsType,
                           @RequestParam(value = "street", required = false, defaultValue = "") String street, ModelMap model) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", 0);
        param.put("limit", 999999);
        param.put("street", street);
        param.put("coordinate", "fixed");
        ArrayList<HashMap<String, Object>> json = new ArrayList<>();

        List<Crowd> crowdes = crowdMapper.selectCrowd(param);
        for (Crowd crowd : crowdes) {
            double[] lnglat = {crowd.getLongitude(), crowd.getLatitude()};
            HashMap<String, Object> map = new HashMap(3);
            map.put("lnglat", lnglat);
            map.put("name", crowd.getLocation());
            map.put("people", crowd.getSubknit());
            map.put("id", crowd.getCrowdID());
            map.put("style", 0);
            json.add(map);
        }
        model.addAttribute("key1", configs.getProperty("amap_key1"));
        model.addAttribute("key2", configs.getProperty("amap_key2"));

        model.addAttribute("crowd", gson.toJson(json));
        return "map/crowdmap";
    }

    @ResponseBody
    @RequestMapping(value = "getCrowdList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCrowdList() {
        Map<String, Object> param = new HashMap<>();
        param.put("start", 0);
        param.put("showDeleted", "false");
        param.put("limit", 999999);
        param.put("coordinate", "fixed");//已有坐标的资产
        ArrayList<HashMap<String, Object>> json = new ArrayList<>();
        List<Crowd> crowdes = crowdMapper.selectCrowd(param);
        for (Crowd crowd : crowdes) {
            double[] lnglat = {crowd.getLongitude(), crowd.getLatitude()};
            HashMap<String, Object> map = new HashMap(3);
            map.put("lnglat", lnglat);
            map.put("name", crowd.getLocation());
            map.put("longitude", crowd.getLongitude());
            map.put("latitude", crowd.getLatitude());
            map.put("id", crowd.getCrowdID());

            map.put("style", 0);
            json.add(map);
        }

        return gson.toJson(json);
    }

    @ResponseBody
    @RequestMapping(value = "getStreet3", method = RequestMethod.GET, produces = "text/javascript;charset=UTF-8")
    public String getStreet3() {
        Map<String, Object> param = new HashMap<>();
        List<HashMap<String, Object>> streets = crowdMapper.selectStreetByCrowd(param);
        List<String> labels = new ArrayList<>();

        String elements = "{\"name\": \"黄埔led_%d\"," +
                "\"position\": [%f, %f]," +
                "\"zooms\": [10, 20]," +
                "\"opacity\": 1," +
                "\"zIndex\": 8," +
                //"\"icon\"," +
                "\"text\": {" +
                "\"content\":\"%s(%d)\"," +
                "\"direction\": \"right\"," +
                "\"offset\": [-20, -5]," +
                "\"style\":\"\"" + //在jsp 的html重新设定
                "}}";
        for (HashMap<String, Object> label : streets) {
            labels.add(String.format(elements, label.get("streetID"), label.get("longitude"), label.get("latitude"), label.get("streetName"), label.get("cc")));
        }

        return "[" + String.join(",", labels) + "]";
    }

    @ResponseBody
    @RequestMapping(value = "getCrowd", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCrowd(@RequestParam(value = "crowdID") Integer crowdID) {
        Map<String, Object> param = new HashMap<>();
        param.put("crowdID", crowdID);

        List<Crowd> crowd1 = crowdMapper.selectCrowd(param);
        if (crowd1.size() == 1)
            return gson.toJson(crowd1.get(0));

        return "{}";
    }
}
