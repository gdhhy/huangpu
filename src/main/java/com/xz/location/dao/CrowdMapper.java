package com.xz.location.dao;

import com.xz.location.pojo.Crowd;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CrowdMapper {
    List<Crowd> selectCrowd(@Param("param") Map<String, Object> param);

    int selectCrowdCount(@Param("param") Map<String, Object> param);

    int updateCrowd(@Param("pojo") Crowd crowd);

    int deleteCrowd(@Param("crowdID") int crowdID);

    int insertCrowd(@Param("pojo") Crowd crowd);

    List<HashMap<String, Object>> selectStreetByCrowd(Map param);
}
