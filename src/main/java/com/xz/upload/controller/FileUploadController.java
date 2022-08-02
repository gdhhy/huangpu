package com.xz.upload.controller;

/**
 * Created by hhy on 2020-08-30
 */

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xz.ExceptionAdapter;
import com.xz.filter.dao.SourceMapper;
import com.xz.filter.pojo.Source;
import com.xz.location.GPSUtil;
import com.xz.location.dao.ServerMapper;
import com.xz.location.pojo.Led;
import com.xz.location.pojo.Server;
import com.xz.rbac.web.DeployRunning;
import com.xz.upload.pojo.FileBucket;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    @Autowired
    private SourceMapper sourceMapper;
    @Autowired
    private ServerMapper serverMapper;

    private static String relative_directory = "upload";
    private static String UPLOAD_LOCATION = DeployRunning.getDir() + relative_directory + File.separator;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 海报上传
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
                        source.setHtmlCount(parseExcel(saveFile, source.getSourceID()));
                        sourceMapper.insertSource(source);
                        logger.debug("sourceID:" + source.getSourceID());
                    }

                    resultMap.put("success", true);
                    resultMap.put("url", source.getServerPath() + File.separator + source.getServerFilename());
                } else {
                    resultMap.put("success", false);
                    resultMap.put("error", "文件曾被上传，上传时间：" + DateUtil.format(sources.get(0).getUploadTime(), "yyyy-MM-dd HH:mm"));
                }
            } catch (IOException e) {
                resultMap.put("error", e.getMessage());
            }

            resultMap.putIfAbsent("success", false);
        }

        return gson.toJson(resultMap);
    }

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
                    logger.debug("rowNum=" + rowNum);
                    Row row = sheet.getRow(rowNum);

                    Led led = new Led();
                    led.setSourceID(sourceID);

                    led.setLocation(getCellValueAsString(row.getCell(1)));
                    if ("".equals(led.getLocation()) || led.getLocation() == null) continue;
                    led.setAddress(getCellValueAsString(row.getCell(2)));//todo解析坐标
                    double[] d = parseCoordinate(getCellValueAsString(row.getCell(2)));
                    led.setLongitude(d[0]);
                    led.setLatitude(d[1]);
                    led.setSize(row.getCell(3).getStringCellValue().trim());
                    led.setSysClass(row.getCell(4).getStringCellValue().trim());
                    led.setCommMode(row.getCell(5).getStringCellValue().trim());
                    led.setControlMode(row.getCell(6).getStringCellValue().trim());
                    led.setStrongCipher(getChineseTrue(row.getCell(7).getStringCellValue().trim()));
                    led.setLicense(getChineseTrue(row.getCell(8).getStringCellValue().trim()));
                    led.setLink(getCellValueAsString(row.getCell(9)));
                    led.setRecordUnit(row.getCell(10).getStringCellValue().trim());
                    led.setOwner(row.getCell(11).getStringCellValue().trim());
                    led.setApprovalUnit(row.getCell(12).getStringCellValue().trim());
                    led.setStreet(row.getCell(13).getStringCellValue().trim());
                    led.setLeader(getCellValueAsString(row.getCell(14)));
                    led.setPolice(getCellValueAsString(row.getCell(15)));
                    led.setPoliceApp(getChineseTrue(row.getCell(8).getStringCellValue().trim()));
                    led.setMemo(row.getCell(17).getStringCellValue().trim());

                    serverMapper.insertLed(led);
                    count++;
                }
            } else if ((sheet = workbook.getSheet("Sheet1")) != null) {//网络资产
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    logger.debug("rowNum=" + rowNum);
                    Row row = sheet.getRow(rowNum);

                    Server server = new Server();
                    server.setSourceID(sourceID);

                    server.setOwner(getCellValueAsString(row.getCell(0)));
                    server.setWebName(getCellValueAsString(row.getCell(1)));
                    server.setWww(getCellValueAsString(row.getCell(2)));
                    server.setIpFrom(getCellValueAsString(row.getCell(3)));
                    server.setAddress(getCellValueAsString(row.getCell(4)));
                    server.setStreet(getCellValueAsString(row.getCell(5)));
                    server.setLink(getCellValueAsString(row.getCell(6)));
                    server.setLinkPhone(getCellValueAsString(row.getCell(7)));
                    server.setSafeGrade((int) row.getCell(8).getNumericCellValue());

                    serverMapper.insertServer(server);
                    count++;
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

    public static void main(String[] args) {
        String[] str = {"113.421912,23.073626", "经度113.42.072 纬度23.079.58", "经：113.4051 纬：23.0741", "X:113.55688, Y:23.20839", "北纬23°09'43''东经113°31'35.36\""};
        for (String s : str) {
            double[] f = parseCoordinate(s);
            System.out.println("f = " + f[0] + "," + f[1]);
        }
    }

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
}