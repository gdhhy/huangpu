package com.xz.location.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xz.location.dao.ServerMapper;
import com.xz.location.pojo.Led;
import com.xz.location.pojo.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/location")

public class LocationController {
    @Autowired
    private ServerMapper serverMapper;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").serializeNulls().create();


    @ResponseBody
    @RequestMapping(value = "listAssets", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String listAssets(@RequestParam(value = "assets", required = false, defaultValue = "led") String assets,
                             @RequestParam(value = "street", required = false, defaultValue = "") String street,
                             @RequestParam(value = "address", required = false, defaultValue = "") String address,
                             @RequestParam(value = "coordinate", required = false, defaultValue = "all") String coordinate,
                             @RequestParam(value = "draw", required = false) Integer draw,
                             @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                             @RequestParam(value = "length", required = false, defaultValue = "100") int limit) {
        if ("led".equals(assets)) return listLed(draw, street, address, coordinate, start, limit);
        if ("server".equals(assets)) return listServer(draw, street, address, coordinate, start, limit);
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "listServer", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String listServer(@RequestParam(value = "draw", required = false) Integer draw,
                             @RequestParam(value = "street", required = false, defaultValue = "") String street,
                             @RequestParam(value = "address", required = false, defaultValue = "") String address,
                             @RequestParam(value = "coordinate", required = false, defaultValue = "") String coordinate,
                             @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                             @RequestParam(value = "length", required = false, defaultValue = "100") int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", start);
        param.put("limit", limit);
        param.put("street", street);
        param.put("address", address);
        param.put("coordinate", coordinate);

        int count = serverMapper.selectServerCount(param);
        // log.debug("count=" + count);
        List<Server> servers = serverMapper.selectServer(param);

        Map<String, Object> result = new HashMap<>();
        result.put("draw", draw);
        result.put("data", servers);
        result.put("iTotalRecords", count);//todo 表的行数，未加任何调剂
        result.put("iTotalDisplayRecords", count);

        return gson.toJson(result);
    }

    @ResponseBody
    @RequestMapping(value = "getServer", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getServer(@RequestParam(value = "locationID") Integer locationID) {
        Map<String, Object> param = new HashMap<>();
        param.put("locationID", locationID);
        List<Server> servers = serverMapper.selectServer(param);
        if (servers.size() == 1)
            return gson.toJson(servers.get(0));
        else return "";
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/saveServer", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveServer(@ModelAttribute("Server") Server postServer) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        if (principal instanceof UserDetails) {
            int result;
            map.put("title", "保存服务器配置");
            if (postServer.getLocationID() > 0)
                result = serverMapper.updateServer(postServer);
            else
                result = serverMapper.insertServer(postServer);
            map.put("succeed", result > 0);
        } else {
            map.put("title", "保存服务器配置");
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteServer", method = RequestMethod.POST)
    public String deleteServer(@RequestParam("locationID") int locationID) {
        Map<String, Object> map = new HashMap<>();

        int deleteCount = serverMapper.deleteServer(locationID);
        map.put("succeed", deleteCount > 0);
        map.put("affectedRowCount", deleteCount);

        return gson.toJson(map);
    }


    /*led*/
    @ResponseBody
    @RequestMapping(value = "listLed", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String listLed(@RequestParam(value = "draw", required = false) Integer draw,
                          @RequestParam(value = "street", required = false, defaultValue = "") String street,
                          @RequestParam(value = "address", required = false, defaultValue = "") String address,
                          @RequestParam(value = "coordinate", required = false, defaultValue = "") String coordinate,
                          @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                          @RequestParam(value = "length", required = false, defaultValue = "100") int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", start);
        param.put("limit", limit);
        param.put("street", street);
        param.put("address", address);
        param.put("coordinate", coordinate);

        int count = serverMapper.selectLedCount(param);
        // log.debug("count=" + count);
        List<Led> leds = serverMapper.selectLed(param);

        Map<String, Object> result = new HashMap<>();
        result.put("draw", draw);
        result.put("data", leds);
        result.put("iTotalRecords", count);//todo 表的行数，未加任何调剂
        result.put("iTotalDisplayRecords", count);

        return gson.toJson(result);
    }

    @ResponseBody
    @RequestMapping(value = "getLed", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getLed(@RequestParam(value = "locationID") Integer locationID) {
        Map<String, Object> param = new HashMap<>();
        param.put("locationID", locationID);
        List<Led> leds = serverMapper.selectLed(param);
        if (leds.size() == 1)
            return gson.toJson(leds.get(0));
        else return "";
    }

    /*@ResponseBody
    @Transactional
    @RequestMapping(value = "/saveLed", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveLed(@RequestBody String postJson) {
        Type ledType = new TypeToken<Led>() {
        }.getType();
        Led led = gson.fromJson(postJson, ledType);

      *//*  log.debug("server = " + server);
        log.debug("server.postLed = " + server.getExpression()); *//*
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        if (principal instanceof UserDetails) {
            int result;
            map.put("title", "保存led配置");
            if (led.getLocationID() > 0)
                result = serverMapper.updateLed(led);
            else
                result = serverMapper.insertLed(led);
            map.put("succeed", result > 0);
        } else {
            map.put("title", "保存led配置");
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }*/
    @ResponseBody
    @Transactional
    @RequestMapping(value = "/saveLed", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveLed(@ModelAttribute("led") Led postLed) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        if (principal instanceof UserDetails) {
            int result;
            map.put("title", "保存LED配置");
            if (postLed.getLocationID() > 0)
                result = serverMapper.updateLed(postLed);
            else
                result = serverMapper.insertLed(postLed);
            map.put("succeed", result > 0);
        } else {
            map.put("title", "保存LED配置");
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/saveLedJson", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveLedJson(@RequestBody String postJson) {
        Type ledType = new TypeToken<Led>() {
        }.getType();
        Led postLed = gson.fromJson(postJson, ledType);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        if (principal instanceof UserDetails) {
            int result;
            map.put("title", "保存LED配置");
            if (postLed.getLocationID() > 0)
                result = serverMapper.updateLed(postLed);
            else
                result = serverMapper.insertLed(postLed);
            map.put("succeed", result > 0);
        } else {
            map.put("title", "保存LED配置");
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteLed", method = RequestMethod.POST)
    public String deleteLed(@RequestParam("locationID") int locationID) {
        Map<String, Object> map = new HashMap<>();

        int deleteCount = serverMapper.deleteLed(locationID);
        map.put("succeed", deleteCount > 0);
        map.put("affectedRowCount", deleteCount);

        return gson.toJson(map);
    }
}
