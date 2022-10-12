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
        param.put("showDeleted", "false");
        param.put("inUse", "true");
        param.put("coordinate", "fixed");
        ArrayList<HashMap<String, Object>> json = new ArrayList<>();

        List<Crowd> crowdes = crowdMapper.selectCrowd(param);
        for (Crowd crowd : crowdes) {
            double[] lnglat = {crowd.getLongitude(), crowd.getLatitude()};
            HashMap<String, Object> map = new HashMap<>(3);
            map.put("lnglat", lnglat);
            map.put("name", crowd.getLocation());
            map.put("highRisk", crowd.getHighRisk());
            map.put("knit", crowd.getKnit());
            map.put("subknit", crowd.getSubknit());
            map.put("important", crowd.getImportant());
            map.put("id", crowd.getCrowdID());
            map.put("style", 0);
            json.add(map);
        }

        HashMap<String, Object> sum = crowdMapper.selectCrowdSum(param);
        model.addAttribute("highRisk", sum.get("highRisk"));
        model.addAttribute("knit", sum.get("knit"));
        model.addAttribute("subknit", sum.get("subknit"));
        model.addAttribute("important", sum.get("important"));

        model.addAttribute("key1", configs.getProperty("amap_key1"));
        model.addAttribute("key2", configs.getProperty("amap_key2"));
        model.addAttribute("key3", configs.getProperty("amap_key3"));

        model.addAttribute("longitudeMin", configs.getProperty("longitudeMin"));
        model.addAttribute("longitudeMax", configs.getProperty("longitudeMax"));
        model.addAttribute("latitudeMin", configs.getProperty("latitudeMin"));
        model.addAttribute("latitudeMax", configs.getProperty("latitudeMax"));
        model.addAttribute("huangpuCenter", configs.getProperty("huangpuCenter"));
       /* model.addAttribute("geoJsonR", crowd("red"));
        model.addAttribute("geoJsonY", crowd("yellow"));*/

        model.addAttribute("crowd", gson.toJson(json));
        return "map/crowdmap";
    }

    @ResponseBody
    @RequestMapping(value = "getCrowdList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCrowdList() {
        Map<String, Object> param = new HashMap<>();
        param.put("start", 0);
        param.put("showDeleted", "false");
        param.put("inUse", "true");
        param.put("limit", 999999);
        param.put("coordinate", "fixed");//已有坐标的资产
        ArrayList<HashMap<String, Object>> json = new ArrayList<>();
        List<Crowd> crowdes = crowdMapper.selectCrowd(param);
        for (Crowd crowd : crowdes) {
            double[] lnglat = {crowd.getLongitude(), crowd.getLatitude()};
            HashMap<String, Object> map = new HashMap<>(3);
            map.put("lnglat", lnglat);
            map.put("location", crowd.getLocation());
            map.put("teams", crowd.getTeams());
            map.put("patient", crowd.getPatient());
            map.put("stayTime", crowd.getStayTime());
            map.put("street", crowd.getStreet());
            //map.put("名称", crowd.getLocation());
            map.put("address", crowd.getAddress());

            map.put("longitude", crowd.getLongitude());
            map.put("latitude", crowd.getLatitude());

            map.put("highRisk", crowd.getHighRisk());
            map.put("knit", crowd.getKnit());
            map.put("subknit", crowd.getSubknit());
            map.put("important", crowd.getImportant());

            map.put("id", crowd.getCrowdID());

            map.put("style", 0);
            json.add(map);
        }

        return gson.toJson(json);
    }

    @ResponseBody
    @RequestMapping(value = "crowd", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String crowd(@RequestParam(value = "color", required = false, defaultValue = "") String color) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", 0);
        param.put("showDeleted", "false");
        param.put("color", color);
        param.put("inUse", "true");
        param.put("limit", 999999);
        param.put("coordinate", "fixed");//已有坐标的资产
        ArrayList<HashMap<String, Object>> json = new ArrayList<>();
        List<Crowd> crowdes = crowdMapper.selectCrowd(param);

        for (Crowd crowd : crowdes) {
            double[] lnglat = {crowd.getLongitude(), crowd.getLatitude()};
            HashMap<String, Object> prop = new HashMap<>(10);
          /*  prop.put("street", crowd.getStreet());
            prop.put("location", crowd.getLocation());
            prop.put("address", crowd.getAddress());*/
          /*  properties.put("longitude", crowd.getLongitude());
            properties.put("latitude", crowd.getLatitude());*/
            prop.put("highRisk", crowd.getHighRisk());
            prop.put("knit", crowd.getKnit());
            prop.put("subknit", crowd.getSubknit());
            prop.put("point_status", 0);
            prop.put("important", crowd.getImportant());
            prop.put("id", crowd.getCrowdID());

            HashMap<String, Object> geometry = new HashMap<>(2);
            geometry.put("type", "Point");
            geometry.put("coordinates", lnglat);

            HashMap<String, Object> map = new HashMap<>(3);
            map.put("type", "Feature");
            map.put("geometry", geometry);
            map.put("properties", prop);
            json.add(map);
        }

        return "{\"type\": \"FeatureCollection\"," +
                "\"features\":" + gson.toJson(json) + "}";
    }

    @ResponseBody
    @RequestMapping(value = "getStreet3", method = RequestMethod.GET, produces = "text/javascript;charset=UTF-8")
    public String getStreet3() {
        Map<String, Object> param = new HashMap<>();
        param.put("longitudeMin", configs.getProperty("longitudeMin"));
        param.put("longitudeMax", configs.getProperty("longitudeMax"));
        param.put("latitudeMin", configs.getProperty("latitudeMin"));
        param.put("latitudeMax", configs.getProperty("latitudeMax"));
        List<HashMap<String, Object>> streets = crowdMapper.selectStreetByCrowd(param);
        List<String> labels = new ArrayList<>();

        String elements = "{\"name\": \"黄埔_%d\"," +
                "\"position\": [%f, %f]," +
                "\"zooms\": [10, 20]," +
                "\"opacity\": 1," +
                "\"zIndex\": 8," +
                //"\"icon\"," +
                "\"highRisk\":%f," +
                "\"knit\":%f," +
                "\"subknit\":%f," +
                "\"important\":%f," +
                "\"streetName\":\"%s\"," +
                "\"text\": {" +
                "\"content\":\"%s(%d)\"," +
                "\"direction\": \"right\"," +
                "\"offset\": [-20, -5]," +
                "\"style\":\"\"" + //在jsp 的html重新设定
                "}}";
        for (HashMap<String, Object> label : streets) {
            labels.add(String.format(elements, label.get("streetID"), label.get("longitude"), label.get("latitude"),
                    label.get("highRisk"), label.get("knit"), label.get("subknit"), label.get("important"), label.get("streetName"),
                    label.get("streetName"), label.get("cc")));
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
