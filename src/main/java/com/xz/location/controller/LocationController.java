package com.xz.location.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xz.location.dao.AssetsMapper;
import com.xz.location.dao.UploadFileMapper;
import com.xz.location.pojo.Led;
import com.xz.location.pojo.IDC;
import com.xz.location.pojo.Location;
import com.xz.location.pojo.UploadFile;
import com.xz.upload.controller.FileUploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/location")

public class LocationController {
    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    @Autowired
    private AssetsMapper assetsMapper;
    @Autowired
    private UploadFileMapper uploadFileMapper;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").serializeNulls().create();


    @ResponseBody
    @RequestMapping(value = "listAssets", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String listAssets(@RequestParam(value = "assets", required = false, defaultValue = "led") String assets,
                             @RequestParam(value = "street", required = false, defaultValue = "") String street,
                             @RequestParam(value = "address", required = false, defaultValue = "") String address,
                             @RequestParam(value = "coordinate", required = false, defaultValue = "all") String coordinate,
                             @RequestParam(value = "showDeleted", required = false, defaultValue = "false") String showDeleted,
                             @RequestParam(value = "draw", required = false) Integer draw,
                             @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                             @RequestParam(value = "length", required = false, defaultValue = "100") int limit) {
        if ("led".equals(assets)) return listLed(draw, street, address, coordinate, showDeleted, start, limit);
        if ("idc".equals(assets)) return listIdc(draw, street, address, coordinate, showDeleted, start, limit);
        return "";
    }


    public String listIdc(@RequestParam(value = "draw", required = false) Integer draw,
                          @RequestParam(value = "street", required = false, defaultValue = "") String street,
                          @RequestParam(value = "address", required = false, defaultValue = "") String address,
                          @RequestParam(value = "coordinate", required = false, defaultValue = "") String coordinate,
                          @RequestParam(value = "showDeleted", required = false, defaultValue = "false") String showDeleted,
                          @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                          @RequestParam(value = "length", required = false, defaultValue = "100") int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", start);
        param.put("limit", limit);
        param.put("street", street);
        param.put("address", address);
        param.put("coordinate", coordinate);
        param.put("showDeleted", showDeleted);

        int count = assetsMapper.selectLedCount(param);
        // log.debug("count=" + count);
        List<IDC> idcs = assetsMapper.selectIdc(param);

        Map<String, Object> result = new HashMap<>();
        result.put("draw", draw);
        result.put("data", idcs);
        result.put("iTotalRecords", count);//todo 表的行数，未加任何调剂
        result.put("iTotalDisplayRecords", count);

        return gson.toJson(result);
    }

    @ResponseBody
    @RequestMapping(value = "getIdc", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getIdc(@RequestParam(value = "locationID") Integer locationID) {
        Map<String, Object> param = new HashMap<>();
        param.put("locationID", locationID);
        List<IDC> idcs = assetsMapper.selectIdc(param);
        if (idcs.size() == 1) {
            IDC idc = idcs.get(0);
            if (idc.getImageID() > 0) {
                UploadFile file = uploadFileMapper.getUploadFile(idc.getImageID());
                idc.setExtJson(gson.toJson(file));
            }
            return gson.toJson(idc);
        } else return "";
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/saveIdc", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveIdc(@ModelAttribute("idc") IDC postIDC) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        if (principal instanceof UserDetails) {
            int result;
            map.put("title", "保存服务器配置");
            if (postIDC.getLocationID() > 0)
                result = assetsMapper.updateIdc(postIDC);
            else
                result = assetsMapper.insertIdc(postIDC);
            map.put("succeed", result > 0);
        } else {
            map.put("title", "保存服务器配置");
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAssets", method = RequestMethod.POST)
    public String deleteAssets(@RequestParam(value = "assets", required = false, defaultValue = "led") String assets,
                               @RequestParam("locationID") int locationID) {
        Map<String, Object> map = new HashMap<>();
        int deleteCount = 0;
        if ("led".equals(assets))
            deleteCount = assetsMapper.deleteLed(locationID);
        if ("idc".equals(assets))
            deleteCount = assetsMapper.deleteIdc(locationID);
        map.put("succeed", deleteCount > 0);
        map.put("affectedRowCount", deleteCount);
        map.put("message", "删除失败！");

        return gson.toJson(map);
    }


    /*led*/
    public String listLed(@RequestParam(value = "draw", required = false) Integer draw,
                          @RequestParam(value = "street", required = false, defaultValue = "") String street,
                          @RequestParam(value = "address", required = false, defaultValue = "") String address,
                          @RequestParam(value = "coordinate", required = false, defaultValue = "") String coordinate,
                          @RequestParam(value = "showDeleted", required = false, defaultValue = "false") String showDeleted,
                          @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                          @RequestParam(value = "length", required = false, defaultValue = "100") int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("start", start);
        param.put("limit", limit);
        param.put("street", street);
        param.put("address", address);
        param.put("coordinate", coordinate);
        param.put("showDeleted", showDeleted);

        int count = assetsMapper.selectLedCount(param);
        // log.debug("count=" + count);
        List<Led> leds = assetsMapper.selectLed(param);

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
        List<Led> leds = assetsMapper.selectLed(param);
        if (leds.size() == 1) {
            Led led = leds.get(0);
            if (led.getImageID() > 0) {
                UploadFile file = uploadFileMapper.getUploadFile(led.getImageID());
                led.setExtJson(gson.toJson(file));
            }
            return gson.toJson(led);
        } else return "";
    }

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
                result = assetsMapper.updateLed(postLed);
            else
                result = assetsMapper.insertLed(postLed);
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
    @RequestMapping(value = "/saveColor", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveColor(@RequestBody String postJson) {
        HashMap postMap = gson.fromJson(postJson, HashMap.class);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        if (principal instanceof UserDetails) {
            int result = 0;
            map.put("title", "设置资产颜色");
            if ("led".equals(postMap.get("assets"))) {
                Led led = new Led();
                led.setLocationID(Integer.parseInt(postMap.get("locationID").toString()));
                led.setColor(postMap.get("color").toString());
                result = assetsMapper.updateLed(led);
            } else if ("idc".equals(postMap.get("assets"))) {
                IDC idc = new IDC();
                idc.setLocationID(Integer.parseInt(postMap.get("locationID").toString()));
                idc.setColor(postMap.get("color").toString());
                result = assetsMapper.updateIdc(idc);
            }

            map.put("succeed", result > 0);
        } else {
            map.put("title", "设置资产颜色");
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteLed", method = RequestMethod.POST)
    public String deleteLed(@RequestParam("locationID") int locationID) {
        Map<String, Object> map = new HashMap<>();

        int deleteCount = assetsMapper.deleteLed(locationID);
        map.put("succeed", deleteCount > 0);
        map.put("affectedRowCount", deleteCount);

        return gson.toJson(map);
    }
}
