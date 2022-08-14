package com.xz.location.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xz.location.dao.AssetsMapper;
import com.xz.location.dao.UploadFileMapper;
import com.xz.location.pojo.Assets;
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
@RequestMapping("/assets")

public class AssetsController {
    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    @Autowired
    private AssetsMapper assetsMapper;
    @Autowired
    private UploadFileMapper uploadFileMapper;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").serializeNulls().create();


    @ResponseBody
    @RequestMapping(value = "listAssets", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String listAssets(@RequestParam(value = "assetsType", required = false, defaultValue = "led") String assetsType,
                          @RequestParam(value = "street", required = false, defaultValue = "") String street,
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
        param.put("assetsType", assetsType);
        param.put("coordinate", coordinate);
        param.put("showDeleted", showDeleted);

        int count = assetsMapper.selectAssetsCount(param);
        List<Assets> assets1 = assetsMapper.selectAssets(param);

        Map<String, Object> result = new HashMap<>();
        result.put("draw", draw);
        result.put("data", assets1);
        result.put("iTotalRecords", count);//todo 表的行数，未加任何调剂
        result.put("iTotalDisplayRecords", count);

        return gson.toJson(result);
    }

    @ResponseBody
    @RequestMapping(value = "getAssets", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getAssets(@RequestParam(value = "assetsID") Integer assetsID) {
        Map<String, Object> param = new HashMap<>();
        param.put("assetsID", assetsID);
        List<Assets> assets1 = assetsMapper.selectAssets(param);
        if (assets1.size() == 1) {
            Assets assets = assets1.get(0);
            if (assets.getImageID() > 0) {
                UploadFile file = uploadFileMapper.getUploadFile(assets.getImageID());
                assets.setExtJson(gson.toJson(file));
            }
            return gson.toJson(assets);
        } else return "";
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/saveAssets", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveAssets(@ModelAttribute("assets") Assets postAssets) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        if (principal instanceof UserDetails) {
            int result;
            map.put("title", "保存服务器配置");
            if (postAssets.getAssetsID() > 0)
                result = assetsMapper.updateAssets(postAssets);
            else
                result = assetsMapper.insertAssets(postAssets);
            map.put("succeed", result > 0);
        } else {
            map.put("title", "保存服务器配置");
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
        map.put("title", "设置资产颜色");
        if (principal instanceof UserDetails) {
            Assets led = new Assets();
            led.setAssetsID(Integer.parseInt(postMap.get("assetsID").toString()));
            led.setColor(postMap.get("color").toString());
            int result = assetsMapper.updateAssets(led);

            map.put("succeed", result > 0);
        } else {
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAssets", method = RequestMethod.POST)
    public String deleteAssets(@RequestParam("assetsID") int assetsID) {
        Map<String, Object> map = new HashMap<>();

        int deleteCount = assetsMapper.deleteAssets(assetsID);
        map.put("succeed", deleteCount > 0);
        map.put("affectedRowCount", deleteCount);

        return gson.toJson(map);
    }
}
