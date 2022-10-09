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
    private int highRisk;
    private int knit;
    private int subknit;
    private int important;
    private int sourceID;
    private int fixPosition;
    private String patient;
    private String teams;
    private String street;//OK
    private String extJson;
    private String color;
    private String status;
    private int deleted;

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

    public int getHighRisk() {
        return highRisk;
    }

    public void setHighRisk(int highRisk) {
        this.highRisk = highRisk;
    }

    public int getKnit() {
        return knit;
    }

    public void setKnit(int knit) {
        this.knit = knit;
    }

    public int getSubknit() {
        return subknit;
    }

    public void setSubknit(int subknit) {
        this.subknit = subknit;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getSourceID() {
        return sourceID;
    }

    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }

    public int getFixPosition() {
        return fixPosition;
    }

    public void setFixPosition(int fixPosition) {
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

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
