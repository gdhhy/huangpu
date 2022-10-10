package com.xz.location.pojo;

import java.io.Serializable;

/**
 * create table crowd
 * (
 * crowdID   int auto_increment primary key,
 * location  varchar(255) comment '场所名称',             -- 共同
 * address   varchar(1024) comment '场所地址',            -- 共同
 * stayTime  varchar(1024) comment '场所地址',            -- 共同
 * longitude decimal(12, 8) comment '经度',             -- 共同
 * latitude  decimal(12, 8) comment '纬度',             -- 共同
 * highRisk  int,
 * knit      int,
 * subknit   int,
 * important int,
 * teams     varchar(1024),
 * street    varchar(255)  default '' comment '所属街道', -- 共同
 * extJson   varchar(8191) default '',
 * color     varchar(255),
 * status    varchar(20)
 * );
 */

public class Crowd implements Serializable {
    private int crowdID;
    private String location;
    private String address;
    private String stayTime;
    private double longitude = 0; //OK
    private double latitude = 0;//OK
    private Integer highRisk;
    private Integer knit;
    private Integer subknit;
    private Integer important;
    private Integer sourceID;
    private Integer fixPosition;
    private String patient;
    private String teams;
    private String street;//OK
    private String extJson;
    private String color;
    private String status;
    private Integer deleted;

    public int getCrowdID() {
        return crowdID;
    }

    public void setCrowdID(int crowdID) {
        this.crowdID = crowdID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStayTime() {
        return stayTime;
    }

    public void setStayTime(String stayTime) {
        this.stayTime = stayTime;
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

    public Integer getHighRisk() {
        return highRisk;
    }

    public void setHighRisk(Integer highRisk) {
        this.highRisk = highRisk;
    }

    public Integer getKnit() {
        return knit;
    }

    public void setKnit(Integer knit) {
        this.knit = knit;
    }

    public Integer getSubknit() {
        return subknit;
    }

    public void setSubknit(Integer subknit) {
        this.subknit = subknit;
    }

    public Integer getImportant() {
        return important;
    }

    public void setImportant(Integer important) {
        this.important = important;
    }

    public Integer getSourceID() {
        return sourceID;
    }

    public void setSourceID(Integer sourceID) {
        this.sourceID = sourceID;
    }

    public Integer getFixPosition() {
        return fixPosition;
    }

    public void setFixPosition(Integer fixPosition) {
        this.fixPosition = fixPosition;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getTeams() {
        return teams;
    }

    public void setTeams(String teams) {
        this.teams = teams;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getExtJson() {
        return extJson;
    }

    public void setExtJson(String extJson) {
        this.extJson = extJson;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
