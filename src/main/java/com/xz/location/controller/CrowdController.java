package com.xz.location.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xz.location.dao.CrowdMapper;
import com.xz.location.pojo.Crowd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/crowd")

public class CrowdController {
    private final static Logger logger = LogManager.getLogger(CrowdController.class);
    @Resource
    private Properties configs;
    @Autowired
    private CrowdMapper crowdMapper;
    /*@Autowired
    private UploadFileMapper uploadFileMapper;*/

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").serializeNulls().create();


    @ResponseBody
    @RequestMapping(value = "listCrowd", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String listCrowd(@RequestParam(value = "byStreet", required = false, defaultValue = "") String street,
                            @RequestParam(value = "address", required = false, defaultValue = "") String address,
                            @RequestParam(value = "coordinate", required = false, defaultValue = "all") String coordinate,
                            @RequestParam(value = "showDeleted", required = false, defaultValue = "false") String showDeleted,
                            @RequestParam(value = "draw", required = false) Integer draw,
                            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                            @RequestParam(value = "length", required = false, defaultValue = "100") int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", start);
        param.put("limit", limit);
        param.put("street", street);
        param.put("address", address);
        param.put("coordinate", coordinate);
        param.put("showDeleted", showDeleted);

        int count = crowdMapper.selectCrowdCount(param);
        List<Crowd> crowd1 = crowdMapper.selectCrowd(param);

        Map<String, Object> result = new HashMap<>();
        result.put("draw", draw);
        result.put("data", crowd1);
        result.put("iTotalRecords", count);//todo 表的行数，未加任何调剂
        result.put("iTotalDisplayRecords", count);

        return gson.toJson(result);
    }

    @ResponseBody
    @RequestMapping(value = "getCrowd", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCrowd(@RequestParam(value = "crowdID") Integer crowdID) {
        Map<String, Object> param = new HashMap<>();
        param.put("crowdID", crowdID);
        List<Crowd> crowd1 = crowdMapper.selectCrowd(param);
        if (crowd1.size() == 1) {
            Crowd crowd = crowd1.get(0);
            /*if (crowd.getImageID() > 0) {
                UploadFile file = uploadFileMapper.getUploadFile(crowd.getImageID());
                crowd.setImageJson(gson.toJson(file));
            }*/
            return gson.toJson(crowd);
        } else return "";
    }


    @ResponseBody
    @Transactional
    @RequestMapping(value = "/saveCrowd", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveCrowd(@ModelAttribute("crowd") Crowd postCrowd) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        if (principal instanceof UserDetails) {
            int result;
            map.put("title", "保存服务器配置");
            if (postCrowd.getCrowdID() > 0)
                result = crowdMapper.updateCrowd(postCrowd);
            else
                result = crowdMapper.insertCrowd(postCrowd);
            map.put("succeed", result > 0);
        } else {
            map.put("title", "保存服务器配置");
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/saveColor", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveColor(@RequestBody String postJson) {
        HashMap postMap = gson.fromJson(postJson, HashMap.class);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("title", "设置流调颜色");
        if (principal instanceof UserDetails) {
            Crowd crowd = new Crowd();
            crowd.setCrowdID(Integer.parseInt(postMap.get("objectID").toString()));
            if (postMap.get("color") != null)
                crowd.setColor(postMap.get("color").toString());
            if (postMap.get("longitude") != null)
                crowd.setLongitude(Double.parseDouble(postMap.get("longitude").toString()));
            if (postMap.get("latitude") != null)
                crowd.setLatitude(Double.parseDouble(postMap.get("latitude").toString()));
            if (postMap.get("street") != null)
                crowd.setStreet(postMap.get("street").toString());
            int result = crowdMapper.updateCrowd(crowd);

            map.put("succeed", result > 0);
        } else {
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/setCrowd", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String setCrowd(@RequestParam(value = "pk") Integer pk,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "value") String value) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("title", "设置流调属性");
        int result = -1;
        if (principal instanceof UserDetails) {
            Crowd crowd = new Crowd();
            crowd.setCrowdID(pk);
            try {
                //crowd.setStatus(value); 通过反射调用，不用这行
                Method method = crowd.getClass().getMethod("set" + name, String.class);
                method.invoke(crowd, value);
                result = crowdMapper.updateCrowd(crowd);
            } catch (Exception e) {
                map.put("message", "调用方法失败，请检查参数！");
            }

            map.put("succeed", result > 0);
        } else {
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteCrowd", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String deleteCrowd(@RequestParam("crowdID") int crowdID) {
        Map<String, Object> returnMap = new HashMap<>();

        int deleteCount = crowdMapper.deleteCrowd(crowdID);
        returnMap.put("succeed", deleteCount > 0);
        returnMap.put("affectedRowCount", deleteCount);

        return gson.toJson(returnMap);
    }

    @RequestMapping(value = "crowd", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String crowd(ModelMap model) {
        putConfigs(model);
        return "location/crowd";
    }

    private void putConfigs(ModelMap model) {
        model.addAttribute("key1", configs.getProperty("amap_key1"));
        model.addAttribute("key2", configs.getProperty("amap_key2"));
        model.addAttribute("key3", configs.getProperty("amap_key3"));

        model.addAttribute("longitudeMin", configs.getProperty("longitudeMin"));
        model.addAttribute("longitudeMax", configs.getProperty("longitudeMax"));
        model.addAttribute("latitudeMin", configs.getProperty("latitudeMin"));
        model.addAttribute("latitudeMax", configs.getProperty("latitudeMax"));
        model.addAttribute("huangpuCenter", configs.getProperty("huangpuCenter"));
    }

    @RequestMapping(value = "drap", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String drap(@RequestParam("crowdID") int crowdID, ModelMap model) {
        Map<String, Object> param = new HashMap<>();
        param.put("crowdID", crowdID);
        List<Crowd> crowd1 = crowdMapper.selectCrowd(param);
        if (crowd1.size() == 1) {
            //if (crowd1.get(0).getLongitude() > 0.0001 && crowd1.get(0).getLatitude() > 0.0001) {
            model.addAttribute("longitude", crowd1.get(0).getLongitude());
            model.addAttribute("latitude", crowd1.get(0).getLatitude());
            //}
            model.addAttribute("address", crowd1.get(0).getAddress());
            model.addAttribute("location", crowd1.get(0).getLocation());
            model.addAttribute("street", crowd1.get(0).getStreet());
            model.addAttribute("objectID", crowdID);
            model.addAttribute("zoom", 16);
        } else {
            model.addAttribute("zoom", 11.7);
            model.addAttribute("longitude", 113.5141753);
            model.addAttribute("latitude", 23.2296782);
        }

        model.addAttribute("savePath", "crowd");

        putConfigs(model);
        return "location/drap";
    }
}
