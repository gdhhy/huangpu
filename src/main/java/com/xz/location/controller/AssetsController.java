package com.xz.location.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    public String listAssets(@RequestParam(value = "assetsType", required = false, defaultValue = "assets") String assetsType,
                             @RequestParam(value = "byStreet", required = false, defaultValue = "") String street,
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
                assets.setImageJson(gson.toJson(file));
            }
            return gson.toJson(assets);
        } else return "";
    }

    @ResponseBody
    @RequestMapping(value = "getAssetsExpand", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getAssetsExpand(@RequestParam(value = "assetsID") Integer assetsID, @RequestParam(value = "draw", required = false, defaultValue = "1") Integer draw) {
        Map<String, Object> param = new HashMap<>();
        param.put("assetsID", assetsID);
        Map<String, Object> result = new HashMap<>();
        result.put("draw", draw);
        List<Assets> assets1 = assetsMapper.selectAssets(param);
        if (assets1.size() == 1) {
            Assets assets = assets1.get(0);

            Type listType = new TypeToken<List<HashMap<String, Object>>>() {
            }.getType();
            List<HashMap<String, Object>> json = gson.fromJson(assets.getExtJson(), listType);
            if (json != null) {
                result.put("data", json);
                result.put("iTotalRecords", json.size());
                result.put("iTotalDisplayRecords", json.size());
            }
        }
        if (result.get("data") == null) {
            result.put("data", new ArrayList<>());
            result.put("iTotalRecords", 0);
            result.put("iTotalDisplayRecords", 0);
        }

        return gson.toJson(result);
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
    @RequestMapping(value = "/saveColor", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveColor(@RequestBody String postJson) {
        HashMap postMap = gson.fromJson(postJson, HashMap.class);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("title", "设置资产颜色");
        if (principal instanceof UserDetails) {
            Assets assets = new Assets();
            assets.setAssetsID(Integer.parseInt(postMap.get("assetsID").toString()));
            assets.setColor(postMap.get("color").toString());
            int result = assetsMapper.updateAssets(assets);

            map.put("succeed", result > 0);
        } else {
            map.put("succeed", false);
            map.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/saveExtKeyValue", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveExtKeyValue(@RequestParam(value = "pk") String pk,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "value") String value) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String[] keys = pk.split("-");

        Map<String, Object> param = new HashMap<>();
        param.put("assetsID", Integer.parseInt(keys[0]));
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("title", "设置资产扩展信息");
        if (principal instanceof UserDetails) {
            List<Assets> assets1 = assetsMapper.selectAssets(param);
            int result = -1;
            if (assets1.size() == 1) {
                Assets assets = assets1.get(0);
               /* if (assets.getExtJson() == null || "".equals(assets.getExtJson())) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("expandID", Integer.parseInt(keys[1]));
                    map.put(name, value);
                    map.put(name.equals("key") ? "name" : "key", "");
                    assets.setExtJson(gson.toJson(keyValueList));
                    result = assetsMapper.updateAssets(assets);
                    returnMap.put("message", "增加资产扩展信息成功");
                } else {*/
                Type listType = new TypeToken<List<HashMap<String, Object>>>() {
                }.getType();
                List<HashMap<String, Object>> keyValueList = gson.fromJson(assets.getExtJson(), listType);
                if (keyValueList != null)
                    for (HashMap<String, Object> map : keyValueList) {
                        if (Math.abs(Integer.parseInt(keys[1]) - (Double) map.get("expandID")) < 0.001) {
                            map.put(name, value);

                            assets.setExtJson(gson.toJson(keyValueList));
                            result = assetsMapper.updateAssets(assets);
                            returnMap.put("message", "更新资产扩展信息成功");
                            break;
                        }
                    }
                //}
                if (result == -1) {
                    if (keyValueList == null)
                        keyValueList = new ArrayList<>();
                    HashMap<String, Object> map = new HashMap<>();//增加一次需要两次saveExtKeyValue
                    map.put("expandID", Integer.parseInt(keys[1]));
                    map.put(name, value);
                    map.put(name.equals("key") ? "name" : "key", "");
                    keyValueList.add(map);
                    assets.setExtJson(gson.toJson(keyValueList));
                    result = assetsMapper.updateAssets(assets);
                    returnMap.put("message", "增加资产扩展信息成功");
                }

                returnMap.put("succeed", result > 0);
            } else {
                returnMap.put("succeed", false);
                returnMap.put("message", "参数错误，找不到主键：" + keys[0]);
            }
        } else {
            returnMap.put("succeed", false);
            returnMap.put("message", "没登录用户信息，请重新登录！");
        }

        return gson.toJson(returnMap);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAssets", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String deleteAssets(@RequestParam("assetsID") int assetsID) {
        Map<String, Object> returnMap = new HashMap<>();

        int deleteCount = assetsMapper.deleteAssets(assetsID);
        returnMap.put("succeed", deleteCount > 0);
        returnMap.put("affectedRowCount", deleteCount);

        return gson.toJson(returnMap);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAssetsExpand", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String deleteAssetsExpand(@RequestParam("assetsID") int assetsID, @RequestParam("expandID") int expandID) {
        Map<String, Object> param = new HashMap<>();
        param.put("assetsID", assetsID);

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("title", "删除资产扩展信息");

        List<Assets> assets1 = assetsMapper.selectAssets(param);
        int result = -1;
        if (assets1.size() == 1) {
            Assets assets = assets1.get(0);
            Type listType = new TypeToken<List<HashMap<String, Object>>>() {
            }.getType();
            List<HashMap<String, Object>> keyValueList = gson.fromJson(assets.getExtJson(), listType);
            for (HashMap<String, Object> map : keyValueList) {
                if (Math.abs(expandID - (Double) map.get("expandID")) < 0.001) {
                    keyValueList.remove(map);

                    assets.setExtJson(gson.toJson(keyValueList));
                    result = assetsMapper.updateAssets(assets);
                    returnMap.put("message", "删除资产扩展信息成功");
                    break;
                }
            }
            returnMap.put("succeed", result > 0);
            if (result == -1) returnMap.put("message", "没找到该资产的扩展信息");
        } else {
            returnMap.put("succeed", false);
            returnMap.put("message", "参数错误，找不到主键：" + assetsID);
        }

        return gson.toJson(returnMap);
    }
}
