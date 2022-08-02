package com.xz.location.dao;

import com.xz.location.pojo.Led;
import com.xz.location.pojo.Server;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ServerMapper {
    //server
    List<Server> selectServer(@Param("param") Map<String, Object> param);

    int selectServerCount(@Param("param") Map<String, Object> param);

    int updateServer(@Param("pojo") Server server);

    int deleteServer(@Param("locationID") int locationID);

    int insertServer(@Param("pojo") Server server);

    //led
    List<Led> selectLed(@Param("param") Map<String, Object> param);

    int selectLedCount(@Param("param") Map<String, Object> param);

    int updateLed(@Param("pojo") Led led);

    int deleteLed(@Param("locationID") int locationID);

    int insertLed(@Param("pojo") Led led);

    List<HashMap<String, Object>> selectStreetByLed(Map param);
    List<HashMap<String, Object>> selectStreetByServer(Map param);
}
