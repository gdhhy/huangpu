package com.xz.location.dao;

import com.xz.location.pojo.Led;
import com.xz.location.pojo.IDC;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AssetsMapper {
    //server
    List<IDC> selectIdc(@Param("param") Map<String, Object> param);

    int insertIdcCount(@Param("param") Map<String, Object> param);

    int updateIdc(@Param("pojo") IDC idc);

    int deleteIdc(@Param("locationID") int locationID);

    int insertIdc(@Param("pojo") IDC server);

    //led
    List<Led> selectLed(@Param("param") Map<String, Object> param);

    int selectLedCount(@Param("param") Map<String, Object> param);

    int updateLed(@Param("pojo") Led led);

    int deleteLed(@Param("locationID") int locationID);

    int insertLed(@Param("pojo") Led led);

    List<HashMap<String, Object>> selectStreetByLed(Map param);

    List<HashMap<String, Object>> selectStreetByServer(Map param);
}
