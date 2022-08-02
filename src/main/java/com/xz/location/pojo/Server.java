package com.xz.location.pojo;

import java.io.Serializable;

/*
   locationID int auto_increment primary key,
    longitude  decimal(12, 8) comment '经度', -- 共同
    latitude   decimal(12, 8) comment '纬度', -- 共同
    owner      varchar(255) comment '企业名称',-- 共同=>owner  corpName
    webName    varchar(255) comment '网站名称',
    www        varchar(255) comment '官网',
    ipFrom     varchar(255) comment 'IP地址',
    ipTo       varchar(255) comment 'IP地址',
    address    varchar(255) comment '企业地址', -- 共同 =>address corpAddress
    street     varchar(255) comment '所属街道', -- 共同
    link       varchar(255) comment '法人',   -- 共同=>link  legalPerson
    linkPhone  varchar(255) comment '法人电话', -- 共同=>linkPhone  LegalPhone
    safeGrade  int comment '等保级别'
 */
public class Server  extends Location  implements Serializable {
    private String webName;
    private String www;
    private String ipFrom;
    private String ipTo;
    private int safeGrade;

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getIpFrom() {
        return ipFrom;
    }

    public void setIpFrom(String ipFrom) {
        this.ipFrom = ipFrom;
    }

    public String getIpTo() {
        return ipTo;
    }

    public void setIpTo(String ipTo) {
        this.ipTo = ipTo;
    }

    public int getSafeGrade() {
        return safeGrade;
    }

    public void setSafeGrade(int safeGrade) {
        this.safeGrade = safeGrade;
    }
}
