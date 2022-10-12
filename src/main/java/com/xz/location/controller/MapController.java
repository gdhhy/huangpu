package com.xz.location.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xz.location.dao.AssetsMapper;
import com.xz.location.pojo.Assets;
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
@RequestMapping("/map")

public class MapController {
    @Resource
    private Properties configs;
    @Autowired
    private AssetsMapper assetsMapper;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").serializeNulls().create();

   /* @RequestMapping(value = "cluster", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String cluster(@RequestParam(value = "assets", required = false, defaultValue = "led") String assetsType,
                          @RequestParam(value = "street", required = false, defaultValue = "") String street, ModelMap model) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", 0);
        param.put("limit", 999999);
        param.put("street", street);
        param.put("assetsType", assetsType);
        param.put("coordinate", "fixed");
        ArrayList<HashMap<String, Object>> json = new ArrayList<>();


        List<Assets> assetses = assetsMapper.selectAssets(param);
        for (Assets assets : assetses) {
            double[] lnglat = {assets.getLongitude(), assets.getLatitude()};
            HashMap<String, Object> map = new HashMap(3);
            map.put("lnglat", lnglat);
            map.put("name", assets.getOwner());
            json.add(map);
        }

        model.addAttribute("assets", gson.toJson(json));
        return "map/cluster";
    }*/

    @RequestMapping(value = "massmarks", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String massmarks(ModelMap model) {
        model.addAttribute("key1", configs.getProperty("amap_key1"));
        model.addAttribute("key2", configs.getProperty("amap_key2"));

        model.addAttribute("longitudeMin", configs.getProperty("longitudeMin"));
        model.addAttribute("longitudeMax", configs.getProperty("longitudeMax"));
        model.addAttribute("latitudeMin", configs.getProperty("latitudeMin"));
        model.addAttribute("latitudeMax", configs.getProperty("latitudeMax"));

        return "map/massmarks";
    }

    @ResponseBody
    @RequestMapping(value = "getAssetsList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getAssetsList(@RequestParam(value = "assetsType", required = false, defaultValue = "led") String assetsType) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", 0);
        param.put("assetsType", assetsType);
        param.put("showDeleted", "false");
        param.put("limit", 999999);
        param.put("coordinate", "fixed");//已有坐标的资产
        ArrayList<HashMap<String, Object>> json = new ArrayList<>();
        List<Assets> assetses = assetsMapper.selectAssets(param);
        for (Assets assets : assetses) {
            double[] lnglat = {assets.getLongitude(), assets.getLatitude()};
            HashMap<String, Object> map = new HashMap(3);
            map.put("lnglat", lnglat);
            map.put("name", assets.getName());
            map.put("street", assets.getStreet());
            map.put("imageUrl", assets.getImageUrl());
            map.put("longitude", assets.getLongitude());
            map.put("latitude", assets.getLatitude());
            map.put("id", assets.getAssetsID());
            if ("led".equals(assetsType))
                map.put("style", 0);
            if ("idc".equals(assetsType))
                map.put("style", 1);
            if ("netbar".equals(assetsType))
                map.put("style", 2);
            if ("secsys".equals(assetsType))
                map.put("style", 3);
            json.add(map);
        }

        return gson.toJson(json);
    }

    /*@Deprecated
    @ResponseBody
    @RequestMapping(value = "getStreet", method = RequestMethod.GET, produces = "text/javascript;charset=UTF-8")
    public String getStreet(@RequestParam(value = "assets", required = false, defaultValue = "led") String assets, @RequestParam(value = "draw", required = false) Integer draw) {
        Map<String, Object> param = new HashMap<>();
        List<HashMap<String, Object>> streets = null;
        if ("led".equals(assets))
            streets = assetsMapper.selectStreetByLed(param);
        if ("idc".equals(assets))
            streets = assetsMapper.selectStreetByServer(param);
        List<String> labels = new ArrayList<>();

        String elements = "{" +
                "        name: \"黄埔区街道\"," +
                "        position: [%f, %f]," +
                "        zooms: [10, 20]," +
                "        opacity: 1," +
                "        zIndex: 10," +
                "        icon: {" +
                "            type: \"image\"," +
                "            image: \"https://a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png\"," +
               *//* "            image: \"https://a.amap.com/jsapi_demos/static/images/poi-marker.png\"," +
                "            clipOrigin: [14, 92]," +
                "            clipSize: [50, 68]," +*//*
                "            size: [25, 34]," +
                "            anchor: \"bottom-center\"," +
                "            angel: 0," +
                "            retina: true" +
                "        }," +
                "        text: {" +
                "            content: \"%s(%d)\"," +
                "            direction: \"left\"," +
                "            offset: [0, -5]," +
                "            style: {" +
                "                fontSize: 15," +
                "                fontWeight: \"normal\"," +
                "                fillColor: \"#333\"," +
                "                strokeColor: \"#fff\"," +
                "                strokeWidth: 2," +
                "            }" +
                "        }" +
                "    }";
        for (HashMap<String, Object> label : streets) {
            labels.add(String.format(elements, label.get("longitude"), label.get("latitude"), label.get("streetName"), label.get("cc")));
        }

        return "var LabelsData = [" + String.join(",", labels) + "]";
    }*/

    /*@ResponseBody
    @RequestMapping(value = "getStreet2", method = RequestMethod.GET, produces = "text/javascript;charset=UTF-8")
    public String getStreet2(@RequestParam(value = "assets", required = false, defaultValue = "led") String assets) {
        Map<String, Object> param = new HashMap<>();
        param.put("assetsType", assets);
        List<HashMap<String, Object>> streets = assetsMapper.selectStreetByAssets(param);
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
                "\"style\": textStyle" +
                "}}";
        for (HashMap<String, Object> label : streets) {
            labels.add(String.format(elements, label.get("streetID"), label.get("longitude"), label.get("latitude"), label.get("streetName"), label.get("cc")));
        }

        return "var streetLabel = [" + String.join(",", labels) + "]";
    }*/

    @ResponseBody
    @RequestMapping(value = "getStreet3", method = RequestMethod.GET, produces = "text/javascript;charset=UTF-8")
    public String getStreet3(@RequestParam(value = "assetsType", required = false, defaultValue = "led") String assetsType) {
        Map<String, Object> param = new HashMap<>();
        param.put("assetsType", assetsType);

        param.put("longitudeMin", configs.getProperty("longitudeMin"));
        param.put("longitudeMax", configs.getProperty("longitudeMax"));
        param.put("latitudeMin", configs.getProperty("latitudeMin"));
        param.put("latitudeMax", configs.getProperty("latitudeMax"));
        List<HashMap<String, Object>> streets = assetsMapper.selectStreetByAssets(param);
        List<String> labels = new ArrayList<>();

        String elements = "{\"name\": \"黄埔led_%d\"," +
                "\"position\": [%f, %f]," +
                "\"zooms\": [10, 20]," +
                "\"opacity\": 1," +
                "\"zIndex\": 8," +
                "\"count\": %d," +
                //"\"icon\"," +
                "\"streetName\":\"%s\"," +
                "\"text\": {" +
                "\"content\":\"%s(%d)\"," +
                "\"direction\": \"right\"," +
                "\"offset\": [-20, -5]," +
                "\"style\":\"\"" + //在jsp 的html重新设定
                "}}";
        for (HashMap<String, Object> label : streets) {
            labels.add(String.format(elements, label.get("streetID"), label.get("longitude"), label.get("latitude"), label.get("cc"), label.get("streetName"), label.get("streetName"), label.get("cc")));
        }

        return "[" + String.join(",", labels) + "]";
    }

    @ResponseBody
    @RequestMapping(value = "getAssets", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getAssets(@RequestParam(value = "assetsID") Integer assetsID) {
        Map<String, Object> param = new HashMap<>();
        param.put("assetsID", assetsID);

        List<Assets> assets1 = assetsMapper.selectAssets(param);
        if (assets1.size() == 1)
            return gson.toJson(assets1.get(0));

        return "{}";
    }
}
