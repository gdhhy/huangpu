package com.xz.location.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.util.*;

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
    @RequestMapping(value = "getCrowdExpand", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCrowdExpand(@RequestParam(value = "crowdID") Integer crowdID, @RequestParam(value = "draw", required = false, defaultValue = "1") Integer draw) {
        Map<String, Object> param = new HashMap<>();
        param.put("crowdID", crowdID);
        Map<String, Object> result = new HashMap<>();
        result.put("draw", draw);
        List<Crowd> crowd1 = crowdMapper.selectCrowd(param);
        if (crowd1.size() == 1) {
            Crowd crowd = crowd1.get(0);

            Type listType = new TypeToken<List<HashMap<String, Object>>>() {
            }.getType();
            List<HashMap<String, Object>> json = gson.fromJson(crowd.getExtJson(), listType);

            if (json != null) {
                int k = 0;
                for (HashMap<String, Object> map : json)
                    map.put("orderID", ++k);

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
            crowd.setCrowdID(Integer.parseInt(postMap.get("crowdID").toString()));
            crowd.setColor(postMap.get("color").toString());
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
    @RequestMapping(value = "/saveExtKeyValue", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String saveExtKeyValue(@RequestParam(value = "pk") String pk,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "value") String value) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String[] keys = pk.split("-");

        Map<String, Object> param = new HashMap<>();
        param.put("crowdID", Integer.parseInt(keys[0]));
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("title", "设置流调扩展信息");
        if (principal instanceof UserDetails) {
            List<Crowd> crowd1 = crowdMapper.selectCrowd(param);
            int result = -1;
            if (crowd1.size() == 1) {
                Crowd crowd = crowd1.get(0);
                Type listType = new TypeToken<List<HashMap<String, Object>>>() {
                }.getType();
                List<HashMap<String, Object>> keyValueList = gson.fromJson(crowd.getExtJson(), listType);
                if (keyValueList != null)
                    for (HashMap<String, Object> map : keyValueList) {
                        if (Math.abs(Integer.parseInt(keys[1]) - (Double) map.get("expandID")) < 0.001) {
                            map.put(name, value);

                            crowd.setExtJson(gson.toJson(keyValueList));
                            result = crowdMapper.updateCrowd(crowd);
                            returnMap.put("message", "更新流调扩展信息成功");
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
                    crowd.setExtJson(gson.toJson(keyValueList));
                    result = crowdMapper.updateCrowd(crowd);
                    returnMap.put("message", "增加流调扩展信息成功");
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
    @RequestMapping(value = "/deleteCrowd", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String deleteCrowd(@RequestParam("crowdID") int crowdID) {
        Map<String, Object> returnMap = new HashMap<>();

        int deleteCount = crowdMapper.deleteCrowd(crowdID);
        returnMap.put("succeed", deleteCount > 0);
        returnMap.put("affectedRowCount", deleteCount);

        return gson.toJson(returnMap);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteCrowdExpand", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String deleteCrowdExpand(@RequestParam("crowdID") int crowdID, @RequestParam("expandID") int expandID) {
        Map<String, Object> param = new HashMap<>();
        param.put("crowdID", crowdID);

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("title", "删除流调扩展信息");

        List<Crowd> crowd1 = crowdMapper.selectCrowd(param);
        int result = -1;
        if (crowd1.size() == 1) {
            Crowd crowd = crowd1.get(0);
            Type listType = new TypeToken<List<HashMap<String, Object>>>() {
            }.getType();
            List<HashMap<String, Object>> keyValueList = gson.fromJson(crowd.getExtJson(), listType);
            for (HashMap<String, Object> map : keyValueList) {
                if (Math.abs(expandID - (Double) map.get("expandID")) < 0.001) {
                    keyValueList.remove(map);

                    crowd.setExtJson(gson.toJson(keyValueList));
                    result = crowdMapper.updateCrowd(crowd);
                    returnMap.put("message", "删除流调扩展信息成功");
                    break;
                }
            }
            returnMap.put("succeed", true);
            if (result == -1) returnMap.put("message", "没找到该流调的扩展信息");
        } else {
            returnMap.put("succeed", false);
            returnMap.put("message", "参数错误，找不到主键：" + crowdID);
        }

        return gson.toJson(returnMap);
    }
    @RequestMapping(value = "crowd", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String crowd(ModelMap model) {
        model.addAttribute("key1", configs.getProperty("amap_key1"));
        model.addAttribute("key2", configs.getProperty("amap_key2"));
        return "location/crowd";
    }
}
