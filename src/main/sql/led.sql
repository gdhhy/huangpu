drop table led;
create table led
(
    locationID   int AUTO_INCREMENT primary key,
    sourceID     int comment '上传excel文件源source的ID',
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
    memo         varchar(255),
    fixPosition  int comment '已定位（即是经纬度已解析）',
    color        varchar(255),
    status       int,
    imageID      int
) engine = MyISAM;
ALTER table led
    add COLUMN color varchar(255);
ALTER table led
    add COLUMN status int;
ALTER table led
    add COLUMN imageID int;
drop table server;
create table server
(
    locationID  int auto_increment primary key,
    sourceID    int comment '上传excel文件源source的ID',
    longitude   decimal(12, 8) comment '经度', -- 共同
    latitude    decimal(12, 8) comment '纬度', -- 共同
    owner       varchar(255) comment '企业名称',-- 共同=>owner  corpName
    webName     varchar(255) comment '网站名称',
    www         varchar(255) comment '官网',
    ipFrom      varchar(255) comment 'IP地址',
    ipTo        varchar(255) comment 'IP地址',
    address     varchar(255) comment '企业地址', -- 共同 =>address corpAddress
    street      varchar(255) comment '所属街道', -- 共同
    link        varchar(255) comment '法人',   -- 共同=>link  legalPerson
    linkPhone   varchar(255) comment '法人电话', -- 共同=>linkPhone  LegalPhone
    safeGrade   int comment '等保级别',
    fixPosition int comment '已定位（即是经纬度已解析）'
) engine = MyISAM;

create table street
(
    streetID    int auto_increment primary key,
    streetName  varchar(255),
    longitude   decimal(12, 8) comment '经度',
    latitude    decimal(12, 8) comment '纬度',
    assetsCount int
) engine = MyISAM;

create table UploadFile
(
    fileID         int,
    filename       varchar(255),
    path           varchar(255),
    contentType    varchar(255),
    size           int,
    uploadTime     timestamp,
    serverPath     varchar(255),
    serverFilename varchar(255)
)