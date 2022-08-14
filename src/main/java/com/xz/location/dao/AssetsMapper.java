package com.xz.location.dao;

import com.xz.location.pojo.Assets;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AssetsMapper {
    List<Assets> selectAssets(@Param("param") Map<String, Object> param);

    int selectAssetsCount(@Param("param") Map<String, Object> param);

    int updateAssets(@Param("pojo") Assets assets);

    int deleteAssets(@Param("assetsID") int assetsID);

    int insertAssets(@Param("pojo") Assets assets);

    List<HashMap<String, Object>> selectStreetByAssets(Map param);
}
