package com.xz.location.pojo;

import java.sql.Timestamp;

public class UploadFile {
    private int fileID;// '唯一标识'
    //private String contentType;// ''
    private String filename;// '文件名'
    private String path;// '路径'
    private long size;// '文件尺寸'
    private Timestamp uploadTime;// '上传时间'
    private String serverPath;// '服务器保存路径'
    private String serverFilename;// '服务器保存文件名'
    private String username;// '上传用户名'

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }



    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getServerFilename() {
        return serverFilename;
    }

    public void setServerFilename(String serverFilename) {
        this.serverFilename = serverFilename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
