package com.xz.upload.controller;

/**
 * Created by hhy on 2020-08-30
 */

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xz.ExceptionAdapter;
import com.xz.filter.dao.SourceMapper;
import com.xz.filter.pojo.Source;
import com.xz.location.GPSUtil;
import com.xz.location.dao.AssetsMapper;
import com.xz.location.dao.CrowdMapper;
import com.xz.location.dao.UploadFileMapper;
import com.xz.location.pojo.Assets;
import com.xz.location.pojo.Crowd;
import com.xz.location.pojo.UploadFile;
import com.xz.rbac.web.DeployRunning;
import com.xz.upload.pojo.FileBucket;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/upload")
public class FileUploadController {
    private static Logger logger = LogManager.getLogger(FileUploadController.class);
    @Autowired
    private SourceMapper sourceMapper;
    @Autowired
    private AssetsMapper assetsMapper;
    @Autowired
    private CrowdMapper crowdMapper;
    @Autowired
    private UploadFileMapper uploadFileMapper;

    private static String relative_directory = "upload";
    private static String UPLOAD_LOCATION = DeployRunning.getDir() + relative_directory + File.separator;
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 上传地图数据文件
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String uploadFile(@Valid FileBucket fileBucket, BindingResult result) {
        Map<String, Object> resultMap = new HashMap<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails ud;
        if (principal instanceof UserDetails) {
            ud = (UserDetails) principal;
        } else {
            resultMap.put("success", false);
            resultMap.put("error", "未登录！");

            return gson.toJson(resultMap);
        }

        if (result.hasErrors()) {
            List<Map<String, Object>> files = new ArrayList<>();
            Map<String, Object> file = new HashMap<>();
            file.put("error", "validation errors");
            files.add(file);

            resultMap.put("success", false);
            resultMap.put("error", files);
        } else {
            System.out.println("Fetching file");
            String ext = fileBucket.getFile().getOriginalFilename().substring(fileBucket.getFile().getOriginalFilename().indexOf("."));
            //MultipartFile multipartFile = fileBucket.getFile();
           /*logger.debug(fileBucket.toString());
            logger.debug(fileBucket.getFile() +"");*/
         /*   logger.debug(StringUtils.toUTF8(fileBucket.getFile().getOriginalFilename()));//todo 中文文件名编码错误
            logger.debug(StringUtils.toGb2312(fileBucket.getFile().getOriginalFilename()));
            logger.debug(StringUtils.toISO8859_1(fileBucket.getFile().getOriginalFilename()));*/
            logger.debug(fileBucket.getFile().getOriginalFilename());
            //Map<String, Object> file = new HashMap<>();

            //String server_save_filename = fileBucket.getFile().getOriginalFilename().substring(0, fileBucket.getFile().getOriginalFilename().indexOf("."));
            String sourceSource = fileBucket.getFile().getOriginalFilename().substring(0, fileBucket.getFile().getOriginalFilename().indexOf("."));
            String server_save_filename = sourceSource + "_" + (new Date()).getTime() + ext;
            logger.debug("server_save_filename:" + server_save_filename);
            try {
                File saveFile = new File(UPLOAD_LOCATION + server_save_filename);
                FileCopyUtils.copy(fileBucket.getFile().getBytes(), saveFile);
                FileInputStream in = new FileInputStream(saveFile);
                String hex = DigestUtils.sha256Hex(in);
                in.close();
                HashMap<String, Object> param = new HashMap<>();
                param.put("checkCode", hex);
                //param.put("htmlCount", 0);
                List<Source> sources = sourceMapper.selectSource(param);

                if (sources.size() == 0) {
                    Source source = new Source();
                    source.setSource(sourceSource);
                    source.setFilename(fileBucket.getFile().getOriginalFilename());
                    source.setCheckCode(hex);
                    source.setPath(UPLOAD_LOCATION);
                    source.setServerFilename(server_save_filename);
                    source.setServerPath(relative_directory);
                    source.setSize(saveFile.length());
                    source.setUploadTime(new Timestamp(System.currentTimeMillis()));

                    source.setUploadUser(ud.getUsername());

                    System.out.println("ext = " + ext);
                    if (".xls".equals(ext) || ".xlsx".equals(ext)) {
                        sourceMapper.insertSource(source);
                        source.setHtmlCount(parseExcel(saveFile, source.getSourceID()));
                        sourceMapper.updateSource(source);
                        logger.debug("sourceID:" + source.getSourceID());
                    }

                    resultMap.put("success", true);
                    resultMap.put("url", source.getServerPath() + File.separator + source.getServerFilename());
                    resultMap.put("message", "成功上传记录数：" + source.getHtmlCount());
                } else {
                    resultMap.put("success", false);
                    resultMap.put("message", "文件曾被上传，上传时间：" + DateUtil.format(sources.get(0).getUploadTime(), "yyyy-MM-dd HH:mm"));
                }
            } catch (IOException e) {
                resultMap.put("error", e.getMessage());
            }

            resultMap.putIfAbsent("success", false);
            resultMap.putIfAbsent("message", "未知错误，联系软件开发者。");
        }

        return gson.toJson(resultMap);
    }

    /**
     * @return
     */
    // @ResponseBody
    /*@RequestMapping(value = "/uploadImage", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String uploadImage(@Valid FileBucket fileBucket, BindingResult result) {
        Map<String, Object> resultMap = new HashMap<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails ud;
        if (principal instanceof UserDetails) {
            ud = (UserDetails) principal;
        } else {
            resultMap.put("success", false);
            resultMap.put("error", "未登录！");

            return gson.toJson(resultMap);
        }

        if (result.hasErrors()) {
            List<Map<String, Object>> files = new ArrayList<>();
            Map<String, Object> file = new HashMap<>();
            file.put("error", "validation errors");
            files.add(file);

            resultMap.put("success", false);
            resultMap.put("error", files);
        } else {
            System.out.println("Fetching file");
            String ext = fileBucket.getFile().getOriginalFilename().substring(fileBucket.getFile().getOriginalFilename().indexOf("."));

            logger.debug(fileBucket.getFile().getOriginalFilename());
            String sourceSource = fileBucket.getFile().getOriginalFilename().substring(0, fileBucket.getFile().getOriginalFilename().indexOf("."));
            String server_save_filename = sourceSource + "_" + (new Date()).getTime() + ext;
            logger.debug("server_save_filename:" + server_save_filename);
            try {
                File saveFile = new File(UPLOAD_LOCATION + server_save_filename);
                FileCopyUtils.copy(fileBucket.getFile().getBytes(), saveFile);
                FileInputStream in = new FileInputStream(saveFile);
                in.close();

                UploadFile uploadFile = new UploadFile();
                uploadFile.setFilename(fileBucket.getFile().getOriginalFilename());

                uploadFile.setPath(UPLOAD_LOCATION);
                uploadFile.setServerFilename(server_save_filename);
                uploadFile.setServerPath(relative_directory);
                uploadFile.setSize(saveFile.length());
                uploadFile.setUploadTime(new Timestamp(System.currentTimeMillis()));

                uploadFile.setUsername(ud.getUsername());


                resultMap.put("success", true);
                resultMap.put("url", uploadFile.getServerPath() + File.separator + uploadFile.getServerFilename());

            } catch (IOException e) {
                resultMap.put("error", e.getMessage());
            }

            resultMap.putIfAbsent("success", false);
        }

        return gson.toJson(resultMap);
    }*/
    private int parseExcel(File file, int sourceID) {
        int count = 0;
        Workbook workbook = null;
        //xls-2003, xlsx-2007
        FileInputStream is = null;

        try {
            is = new FileInputStream(file);
            if (file.getCanonicalPath().toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else if (file.getCanonicalPath().toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            }/* else {
                //  抛出自定义的业务异常
                throw new ApplicationException("excel格式文件错误");
            }*/
            Sheet sheet;

            assert workbook != null;
            if ((sheet = workbook.getSheet("总表")) != null) {//led
                logger.debug("sheetName = " + sheet.getSheetName());
                for (int rowNum = 3; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    //logger.debug("rowNum=" + rowNum);
                    Row row = sheet.getRow(rowNum);

                    Assets assets = new Assets();

                    assets.setAssetsType("led");
                    assets.setSourceID(sourceID);
                    assets.setName(getCellValueAsString(row.getCell(1)));
                    if ("".equals(assets.getName()) || assets.getName() == null) continue;
                    assets.setAddress(getCellValueAsString(row.getCell(2)));//todo解析坐标
                    double[] d = parseCoordinate(getCellValueAsString(row.getCell(2)));//已转换百度坐标未高德坐标
                    assets.setLongitude(d[0]);
                    assets.setLatitude(d[1]);

                    String[] nameTel = nameTel(getCellValueAsString(row.getCell(9)));
                    assets.setLink(nameTel[0]);
                    assets.setLinkPhone(nameTel[1]);
                    assets.setOwner(row.getCell(11).getStringCellValue().trim());
                    assets.setStreet(row.getCell(13).getStringCellValue().replace("街", "").trim());

                    List<HashMap<String, Object>> json = new ArrayList<>();
                    int[] index = {3, 4, 5, 6, 7, 8,
                            10, 12, 14, 15, 16, 17};
                    String[] head = {"尺寸", "系统分类", "通讯方式", "控制方式", "有无设置强制密码", "是否有营业执照",
                            "有无报备及报备单位", "建设审批单位", "派出所负责领导及联系电话", "派出所负责民警及联系电话", "是否录入社区新警务APP（是或否）", "备注（停用或新增）"};
                    HashMap<String, Object> item;
                    for (int i = 0; i < index.length; i++) {
                        item = new HashMap<>();
                        item.put("expandID", i + 1);
                        item.put("key", head[i]);
                        item.put("value", getCellValueAsString(row.getCell(index[i])));
                        json.add(item);
                    }
                    assets.setExtJson(gson.toJson(json));

                    assetsMapper.insertAssets(assets);
                    count++;
                }
            }
            /*if ((sheet = workbook.getSheet("Sheet1")) != null) {//网络资产
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    //logger.debug("rowNum=" + rowNum);
                    Row row = sheet.getRow(rowNum);

                    Assets assets = new Assets();
                    assets.setAssetsType("idc");
                    assets.setSourceID(sourceID);

                    assets.setOwner(getCellValueAsString(row.getCell(0)));
                    assets.setAddress(getCellValueAsString(row.getCell(4)));
                    assets.setStreet(getCellValueAsString(row.getCell(5)));
                    assets.setLink(getCellValueAsString(row.getCell(6)));
                    assets.setLinkPhone(getCellValueAsString(row.getCell(7)));
                    List<HashMap<String, Object>> json = new ArrayList<>();

                    int[] index = {1, 2, 3, 8};
                    String[] head = {"网站名称", "官网", "IP", "等保级别"};
                    HashMap<String, Object> item;
                    for (int i = 0; i < index.length; i++) {
                        item = new HashMap<>();
                        item.put("expandID", i + 1);
                        item.put("key", head[i]);
                        item.put("value", getCellValueAsString(row.getCell(index[i])));
                        json.add(item);
                    }
                    assets.setExtJson(gson.toJson(json));

                    assetsMapper.insertAssets(assets);
                    count++;
                }
            }*/
            if ((sheet = workbook.getSheet("IDC")) != null) {
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    //logger.debug("rowNum=" + rowNum);
                    Row row = sheet.getRow(rowNum);

                    Assets assets = new Assets();
                    assets.setAssetsType("idc");
                    assets.setSourceID(sourceID);

                    assets.setName(getCellValueAsString(row.getCell(1)));
                    assets.setOwner(getCellValueAsString(row.getCell(2)));
                    assets.setAddress(getCellValueAsString(row.getCell(3)));
                    assets.setStreet(getCellValueAsString(row.getCell(4)));
                    if (row.getCell(5).getCellType() == CellType.NUMERIC)
                        assets.setLongitude(row.getCell(5).getNumericCellValue());
                    if (row.getCell(6).getCellType() == CellType.NUMERIC)
                        assets.setLatitude(row.getCell(6).getNumericCellValue());
                    assets.setLink(getCellValueAsString(row.getCell(7)));
                    assets.setLinkPhone(getCellValueAsString(row.getCell(8)));
                    assets.setStatus(getCellValueAsString(row.getCell(10)));
                    List<HashMap<String, Object>> json = new ArrayList<>();

                    int[] index = {11, 12, 13};
                    String[] head = {"设备数量", "IP数量", "用户数量"};
                    HashMap<String, Object> item;
                    for (int i = 0; i < index.length; i++) {
                        item = new HashMap<>();
                        item.put("expandID", i + 1);
                        item.put("key", head[i]);
                        item.put("value", getCellValueAsString(row.getCell(index[i])));
                        json.add(item);
                    }
                    assets.setExtJson(gson.toJson(json));

                    assetsMapper.insertAssets(assets);
                    count++;
                }
            }
            if ((sheet = workbook.getSheet("网吧")) != null) {
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    logger.debug("rowNum=" + rowNum);
                    Row row = sheet.getRow(rowNum);

                    Assets assets = new Assets();
                    assets.setAssetsType("netbar");
                    assets.setSourceID(sourceID);

                    assets.setName(getCellValueAsString(row.getCell(1)));
                    assets.setOwner(getCellValueAsString(row.getCell(2)));
                    assets.setAddress(getCellValueAsString(row.getCell(3)));
                    assets.setStreet(getCellValueAsString(row.getCell(4)));
                    if (row.getCell(5).getCellType() == CellType.NUMERIC)
                        assets.setLongitude(row.getCell(5).getNumericCellValue());
                    if (row.getCell(6).getCellType() == CellType.NUMERIC)
                        assets.setLatitude(row.getCell(6).getNumericCellValue());
                    assets.setLink(getCellValueAsString(row.getCell(7)));
                    assets.setLinkPhone(getCellValueAsString(row.getCell(8)));
                    assets.setStatus(getCellValueAsString(row.getCell(10)));
                    List<HashMap<String, Object>> json = new ArrayList<>();

                    assetsMapper.insertAssets(assets);
                    count++;
                }
            }
            if ((sheet = workbook.getSheet("等保系统")) != null) {
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    logger.debug("rowNum=" + rowNum);
                    Row row = sheet.getRow(rowNum);

                    Assets assets = new Assets();
                    assets.setAssetsType("secsys");
                    assets.setSourceID(sourceID);

                    assets.setName(getCellValueAsString(row.getCell(1)));
                    assets.setOwner(getCellValueAsString(row.getCell(2)));
                    assets.setAddress(getCellValueAsString(row.getCell(3)));
                    assets.setStreet(getCellValueAsString(row.getCell(4)));
                    if (row.getCell(5).getCellType() == CellType.NUMERIC)
                        assets.setLongitude(row.getCell(5).getNumericCellValue());
                    if (row.getCell(6).getCellType() == CellType.NUMERIC)
                        assets.setLatitude(row.getCell(6).getNumericCellValue());
                    assets.setLink(getCellValueAsString(row.getCell(7)));
                    assets.setLinkPhone(getCellValueAsString(row.getCell(8)));
                    assets.setStatus(getCellValueAsString(row.getCell(10)));
                    List<HashMap<String, Object>> json = new ArrayList<>();

                    assetsMapper.insertAssets(assets);
                    count++;
                }
            }
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                logger.debug("sheetName:" + workbook.getSheetName(i));
                if (workbook.getSheetName(i).contains("流调")) {
                    sheet = workbook.getSheetAt(i);
                    List<CellRangeAddress> cellRangeAddressList = sheet.getMergedRegions();
                    for (CellRangeAddress ca : cellRangeAddressList) {
                        if (ca.getFirstColumn() == ca.getLastColumn() && ca.getLastColumn() == 2) {//重点场所名称
                            int firstR = ca.getFirstRow();
                            int lastR = ca.getLastRow();
                            //logger.debug("firstR:" + firstR + ",lastR:" + lastR);
                            int highRisk = 0;
                            int knit = 0;
                            int subknit = 0;
                            int important = 0;

                            Row row = sheet.getRow(firstR);

                            Crowd crowd = new Crowd();
                            crowd.setSourceID(sourceID);
                            crowd.setPatient(getCellValueAsString(row.getCell(1)));
                            crowd.setLocation(getCellValueAsString(row.getCell(2)));
                            crowd.setAddress(getCellValueAsString(row.getCell(3)));
                            crowd.setStayTime(getCellValueAsString(row.getCell(4)));
                            crowd.setTeams(getCellValueAsString(row.getCell(10)));
                            crowd.setStreet(getCellValueAsString(row.getCell(11)));
                            for (int j = firstR; j <= lastR; j++) {
                                Row row2 = sheet.getRow(j);
                                double num = 0;
                                //logger.debug("row:" + j + ",col:" + 7 + ":" + getCellValueAsString(row2.getCell(7)));

                                if (row2.getCell(7).getCellType() == CellType.STRING)
                                    num = Convert.toInt(row2.getCell(7).getStringCellValue().replace("人", ""), 0);
                                if (row2.getCell(7).getCellType() == CellType.NUMERIC)
                                    num = row2.getCell(7).getNumericCellValue() + 0;

                                if (num > 0) {
                                    if (getCellValueAsString(row2.getCell(6)).contains("高风险"))
                                        highRisk += num;
                                    if ("密接".equals(getCellValueAsString(row2.getCell(6))))
                                        knit += num;
                                    if ("次密接".equals(getCellValueAsString(row2.getCell(6))))
                                        subknit += num;
                                    if (getCellValueAsString(row2.getCell(6)).contains("重点"))
                                        important += num;
                                }
                            }
                            crowd.setHighRisk(highRisk);
                            crowd.setKnit(knit);
                            crowd.setSubknit(subknit);
                            crowd.setImportant(important);

                            crowdMapper.insertCrowd(crowd);
                            count++;
                        }
                    }
                    break;
                }
            }
        } catch (IOException | NullPointerException e) {
            //  抛出自定义的业务异常
            throw new ExceptionAdapter(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return count;
    }

    private int getChineseTrue(String str) {
        return ("是".equals(str) || "有".equals(str) || "对".equals(str)) ? 1 : 0;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue() + "";
        if (cell.getCellType() == CellType.BOOLEAN) return cell.getBooleanCellValue() ? "是" : "否";
        if (cell.getCellType() == CellType.FORMULA) return cell.getCellFormula();
        return "";
    }

  /*  public static void main(String[] args) {
        String[] str = {"113.421912,23.073626", "经度113.42.072 纬度23.079.58", "经：113.4051 纬：23.0741", "X:113.55688, Y:23.20839", "北纬23°09'43''东经113°31'35.36\""};
        for (String s : str) {
            double[] f = parseCoordinate(s);
            System.out.println("f = " + f[0] + "," + f[1]);
        }
    }*/

    private static double[] parseCoordinate(String string) {
        //
        //String pattern[]={"(?<longitude>[1-9]+(\\.\\d+))\\D+(?<latitude>[1-9]+(\\.\\d+))$","(?<longitude>[1-9]+(\\.\\d+)(\\.\\d+))\\D+(?<latitude>[1-9]+(\\.\\d+)(\\.\\d+))$"};
        Pattern p = Pattern.compile("(?<longitude>[1-9]+(\\.\\d+))\\D+(?<latitude>[1-9]+(\\.\\d+))$");
        Matcher m = p.matcher(string);
        if (m.find()) {
                /*System.out.println("m.groupCount() = " + m.groupCount());
                System.out.println("longitude = " + m.group("longitude"));
                System.out.println("latitude = " + m.group("latitude"));*/

            double[] latlon = GPSUtil.gps84_To_Gcj02(Double.parseDouble(m.group("latitude")), Double.parseDouble(m.group("longitude")));
            return new double[]{latlon[1], latlon[0]};

        }
        p = Pattern.compile("(?<lon>[1-9]+)(\\.(?<lon1>\\d+))(\\.(?<lon2>\\d+))\\D+(?<lat>[1-9]+)(\\.(?<lat1>\\d+))(\\.(?<lat2>\\d+))$");
        m = p.matcher(string);
        if (m.find()) {/*longitude = 23°09'43',   latitude = 113°31'35.36"*/
            /*System.out.println("m.groupCount() = " + m.groupCount());
            System.out.println("lon = " + m.group("lon"));
            System.out.println("lon1 = " + m.group("lon1"));
            System.out.println("lon2 = " + m.group("lon2"));
            System.out.println("lat = " + m.group("lat"));
            System.out.println("lat1 = " + m.group("lat1"));
            System.out.println("lat2 = " + m.group("lat2"));*/

            double[] lonlat = GPSUtil.gps84_To_Gcj02(Integer.parseInt(m.group("lat")) + Integer.parseInt(m.group("lat1")) / 60.0 + Float.parseFloat(m.group("lat2")) / 3600,
                    Integer.parseInt(m.group("lon")) + Integer.parseInt(m.group("lon1")) / 60.0 + Float.parseFloat(m.group("lon2")) / 3600);
            return new double[]{lonlat[1], lonlat[0]};
        }

        p = Pattern.compile("(?<lon>[1-9]+)°(?<lon1>\\d{1,2})'(?<lon2>\\d{1,2}(\\.\\d+)?)[''\"]\\D+(?<lat>[1-9]+)°(?<lat1>\\d{1,2})'(?<lat2>\\d{1,2}(\\.\\d+)?)[''\"]$");
        m = p.matcher(string);
        if (m.find()) {/*longitude = 23°09'43',   latitude = 113°31'35.36"*/
           /* System.out.println("m.groupCount() = " + m.groupCount());
            System.out.println("lon = " + m.group("lon"));
            System.out.println("lon1 = " + m.group("lon1"));
            System.out.println("lon2 = " + m.group("lon2"));
            System.out.println("lat = " + m.group("lat"));
            System.out.println("lat1 = " + m.group("lat1"));
            System.out.println("lat2 = " + m.group("lat2"));*/
            double[] lonlat = GPSUtil.gps84_To_Gcj02(Integer.parseInt(m.group("lat")) + Integer.parseInt(m.group("lat1")) / 60.0 + Float.parseFloat(m.group("lat2")) / 3600,
                    Integer.parseInt(m.group("lon")) + Integer.parseInt(m.group("lon1")) / 60.0 + Float.parseFloat(m.group("lon2")) / 3600);
            return new double[]{lonlat[1], lonlat[0]};
        }
        return new double[]{0, 0};
    }

    @RequestMapping(value = "/uploadImage")
    private ResponseEntity<String> uploadImage(MultipartHttpServletRequest request) {
        logger.info("start upload file ......");
        Map<String, Object> loadResultMap = new HashMap<>();
        MultiValueMap<String, MultipartFile> multiMap = request.getMultiFileMap();

       /*String taskKey = request.getParameter("taskKey");
        logger.info("taskKey = "+taskKey);*/

        //TODO 异常
        //int i = 9/0;

        for (Map.Entry<String, List<MultipartFile>> entry : multiMap.entrySet()) {
            List<MultipartFile> mFList = entry.getValue();

            for (MultipartFile mFile : mFList) {
                UploadFile loadResult = loadFile(mFile, UPLOAD_LOCATION);
                String fileName = mFile.getOriginalFilename();
                loadResultMap.put(fileName, loadResult.getServerFilename());
                loadResultMap.put("fileID", loadResult.getFileID());
                loadResultMap.put("filename", loadResult.getServerFilename());
            }
        }

        //封装返回
        return new ResponseEntity<>(gson.toJson(loadResultMap), HttpStatus.OK);
    }

    private UploadFile loadFile(MultipartFile mfile, String filePath) {
        // 获取上传的原始文件名
        String fileName = mfile.getOriginalFilename();

        //TODO 制造有失败上传场景
       /* if(fileName.equals("5a38b9ee3b7fb.jpg")){
            return result;
        }*/

        String fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());

        // 判断并创建上传用的文件夹
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        // 重新设置文件名为 UUID，以确保唯一
        file = new File(filePath, UUID.randomUUID() + fileSuffix);

        UploadFile uploadFile = new UploadFile();
        try {
            // 写入文件
            mfile.transferTo(file);

            uploadFile.setUploadTime(new Timestamp(System.currentTimeMillis()));
            uploadFile.setFilename(fileName);
            uploadFile.setServerFilename(file.getName());
         /* logger.debug("getAbsolutePath:"+file.getAbsolutePath());
            logger.debug("getName:"+file.getName());
            logger.debug("getPath:"+file.getPath());*/
            uploadFile.setServerPath(relative_directory);
            uploadFile.setSize(file.length());
            uploadFileMapper.insertUploadFile(uploadFile);
        } catch (IOException e) {
            logger.error("load file error", e);
        }
        return uploadFile;
    }

    @ResponseBody
    @RequestMapping(value = "getImageInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getImageInfo(@RequestParam(value = "fileID") Integer fileID) {
        Map<String, Object> param = new HashMap<>();
        param.put("fileID", fileID);
        List<UploadFile> files = uploadFileMapper.selectUploadFile(param);
        if (files.size() == 1)
            return gson.toJson(files.get(0));
        else return "";
    }

    public static String[] nameTel(String text) {
        String[] nameTel = {"", ""};
        text = text.replaceAll("[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]", "");//替换掉身份证

        Pattern namePattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,}", Pattern.CASE_INSENSITIVE);
        Matcher m = namePattern.matcher(text);
        if (m.find()) nameTel[0] = m.group(0);

        Pattern telPattern = Pattern.compile("1[3456789]\\d{9}?|(?:\\d{8})", Pattern.CASE_INSENSITIVE);
        Matcher m1 = telPattern.matcher(text);
        while (m1.find()) nameTel[1] += ("".equals(nameTel[1]) ? "" : "、") + m1.group(0);

        return nameTel;
    }

    public static void main(String[] args) {
        String text = "钟帅31602671、18122708802\n";
        String s[] = nameTel(text);
        System.out.println("s0 = " + s[0]);
        System.out.println("s1 = " + s[1]);
      /*  Pattern idPattern = Pattern.compile("[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]");
        Matcher repMatcher = idPattern.matcher(text);
        text = text.replaceAll("[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]", "");

        System.out.println("s = " + text);
        Pattern namePattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,}", Pattern.CASE_INSENSITIVE);
        Matcher m = namePattern.matcher(text);

        int matchCount = 0;
        while (m.find()) {
            System.out.println("m.groupCount() = " + m.groupCount() + " ----------++++----------------------------------------");
            System.out.println("group(0)=" + m.group(0));
            matchCount++;
        }
        System.out.println("matchCount = " + matchCount);

        Pattern telPattern = Pattern.compile("1[3456789]\\d{9}?|(?:\\d{8})", Pattern.CASE_INSENSITIVE);
        Matcher m1 = telPattern.matcher(text);

        matchCount = 0;
        while (m1.find()) {
            System.out.println("m1.groupCount() = " + m1.groupCount() + " ----------++++----------------------------------------");
            System.out.println("group(0)=" + m1.group(0));
            matchCount++;
        }
        System.out.println("matchCount = " + matchCount);*/
    }
}