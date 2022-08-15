package com.xz.location.pojo;

import java.io.Serializable;

/*assetsID    int auto_increment primary key,
assetsType  varchar(255) comment 'idc、led、netbar、secsys',
name        varchar(255) comment '网站名称',
owner       varchar(255) comment '企业名称',-- 共同=>owner  corpName
address     varchar(255) comment '企业地址', -- 共同 =>address corpAddress
longitude   decimal(12, 8) comment '经度', -- 共同
latitude    decimal(12, 8) comment '纬度', -- 共同
link        varchar(255) comment '联系人', -- 共同=>link  legalPerson
linkPhone   varchar(255) comment '联系人电话', -- 共同=>linkPhone  LegalPhone
imageID     int,
imageUrl    varchar(255),
status      int,
street      varchar(255) comment '所属街道', -- 共同
color       varchar(255),
sourceID    int comment '上传excel文件源source的ID',
fixPosition int comment '已定位（即是经纬度已解析）'*/
public class Assets implements Serializable {
    private int assetsID;
    private String assetsType;//idc、led、netbar、secsys
    private String name;
    private String owner;
    private String address;
    private double longitude = 0; //OK
    private double latitude = 0;//OK
    private String link;//OK
    private String linkPhone;//OK
    private int imageID;
    private String imageUrl;
    private String status;
    private String street;//OK
    private String color;
    private String extJson;
    private String imageJson;
    private int sourceID;
    private boolean fixPosition;
    private int deleted;//1未删除，默认0

    public int getAssetsID() {
        return assetsID;
    }

    public void setAssetsID(int assetsID) {
        this.assetsID = assetsID;
    }

    public String getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getExtJson() {
        return extJson;
    }

    public void setExtJson(String extJson) {
        this.extJson = extJson;
    }

    public int getSourceID() {
        return sourceID;
    }

    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }

    public boolean isFixPosition() {
        return fixPosition;
    }

    public void setFixPosition(boolean fixPosition) {
        this.fixPosition = fixPosition;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getImageJson() {
        return imageJson;
    }

    public void setImageJson(String imageJson) {
        this.imageJson = imageJson;
    }
}
