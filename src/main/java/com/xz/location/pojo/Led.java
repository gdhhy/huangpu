package com.xz.location.pojo;

import java.io.Serializable;

/*
   assetsID   int AUTO_INCREMENT primary key,
    location     varchar(255) comment '位置',   -- 共同
    address      varchar(255) comment '地址',   -- 共同
    longitude    decimal(12, 8) comment '经度', -- 共同
    latitude     decimal(12, 8) comment '纬度', -- 共同
    size         varchar(255) comment '尺寸',
    sysClass     varchar(255) comment '系统分类',
    commMode     varchar(255) comment '通讯方式',
    controlMode  varchar(255) comment '控制方式',
    strongCipher int comment '有无设置强密码',
    license      int comment '有无营业执照',
    link         varchar(255) comment '联系人',-- 共同
    linkPhone    varchar(255) comment '联系电话', -- 共同
    recordUnit   varchar(255) comment '报备单位',
    approvalUnit varchar(255) comment '建设审批单位',
    owner        varchar(255) comment '权属单位',
    street       varchar(255) comment '辖区',   -- 共同
    leader       varchar(255) comment '派出所领导',
    leaderPhone  varchar(255) comment '派出所领导电话',
    police       varchar(255) comment '民警',
    policePhone  varchar(255) comment '民警电话',
    policeApp    int comment '是否录入警务app',
    memo         varchar(255)
 */
public class Led extends Assets implements Serializable {
    private String location;
    private String size;
    private String sysClass;
    private String commMode;
    private String controlMode;
    private int strongCipher;
    private int license;
    private String recordUnit;
    private String approvalUnit;
    private String leader;
    private String leaderPhone;
    private String police;
    private String policePhone;
    private int policeApp;
    private String memo;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSysClass() {
        return sysClass;
    }

    public void setSysClass(String sysClass) {
        this.sysClass = sysClass;
    }

    public String getCommMode() {
        return commMode;
    }

    public void setCommMode(String commMode) {
        this.commMode = commMode;
    }

    public String getControlMode() {
        return controlMode;
    }

    public void setControlMode(String controlMode) {
        this.controlMode = controlMode;
    }

    public int getStrongCipher() {
        return strongCipher;
    }

    public void setStrongCipher(int strongCipher) {
        this.strongCipher = strongCipher;
    }

    public int getLicense() {
        return license;
    }

    public void setLicense(int license) {
        this.license = license;
    }

    public String getRecordUnit() {
        return recordUnit;
    }

    public void setRecordUnit(String recordUnit) {
        this.recordUnit = recordUnit;
    }

    public String getApprovalUnit() {
        return approvalUnit;
    }

    public void setApprovalUnit(String approvalUnit) {
        this.approvalUnit = approvalUnit;
    }


    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public String getPolicePhone() {
        return policePhone;
    }

    public void setPolicePhone(String policePhone) {
        this.policePhone = policePhone;
    }

    public int getPoliceApp() {
        return policeApp;
    }

    public void setPoliceApp(int policeApp) {
        this.policeApp = policeApp;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
