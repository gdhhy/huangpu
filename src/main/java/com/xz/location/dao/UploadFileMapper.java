package com.xz.location.dao;

import com.xz.location.pojo.Led;
import com.xz.location.pojo.UploadFile;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UploadFileMapper {
    //uploadFile
    List<UploadFile> selectUploadFile(@Param("param") Map<String, Object> param);

    UploadFile getUploadFile(@Param("fileID") int fileID);

    int deleteUploadFile(@Param("fileID") int fileID);

    int insertUploadFile(@Param("pojo") UploadFile uploadFile);

}
