<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>黄埔区资产位置信息</title>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <style>
        html, body, #container {
            height: 100%;
            width: 100%;
        }

        .content-window-card {
            position: relative;
            box-shadow: none;
            bottom: 0;
            left: 0;
            width: auto;
            padding: 0;
        }

        .content-window-card p {
            height: 2rem;
        }

        .custom-info {
            border: solid 1px silver;
        }

        div.info-top {
            position: relative;
            background: none repeat scroll 0 0 #F9F9F9;
            border-bottom: 1px solid #CCC;
            border-radius: 5px 5px 0 0;
        }

        div.info-top div {
            display: inline-block;
            color: #333333;
            font-size: 14px;
            font-weight: bold;
            line-height: 31px;
            padding: 0 10px;
        }

        div.info-top img {
            position: absolute;
            top: 10px;
            right: 10px;
            transition-duration: 0.25s;
        }

        div.info-top img:hover {
            box-shadow: 0px 0px 5px #000;
        }

        div.info-middle {
            font-size: 12px;
            padding: 10px 6px;
            line-height: 20px;
        }

        div.info-bottom {
            height: 0px;
            width: 100%;
            clear: both;
            text-align: center;
        }

        div.info-bottom img {
            position: relative;
            z-index: 104;
        }

        span {
            margin-left: 5px;
            font-size: 11px;
        }

        .info-middle img {
            float: left;
            margin-right: 6px;
        }

        /*右下角*/
        .input-card .btn {
            margin-right: 1.2rem;
            width: 9rem;
        }

        .input-card .btn:last-child {
            margin-right: 0;
        }

        .collision-btn.disable {
            background-image: none;
            color: lightgrey;
            border-color: lightgrey;
            cursor: not-allowed;
        }

        .collision-btn.disable:hover {
            background-color: #fff;
            border-color: lightgrey;
        }

    </style>
</head>
<body>
<div id="container" class="map" tabindex="0"></div>

<div class="input-card" style="width: 120px; top: 10px; bottom: auto;">
    <h4>资产类型</h4>
    <div id="coordinate">
        <div class="input-item"><input id="led" name="language" type="radio" checked="checked"><span class="input-text">LED</span></div>
        <div class="input-item"><input id="server" name="language" type="radio"><span class="input-text">服务器</span></div>
    </div>
</div>
<div class="input-card">
    <label style="color:grey">标注避让设置</label>
    <div class="input-item">
        <input id="allowCollision" type="button" class="btn collision-btn" onclick="allowCollisionFunc()"
               value="标注避让">
        <input id="notAllowCollision" type="button" class="btn collision-btn" onclick="notAllowCollisionFunc()" value="显示全部">
    </div>
</div>

<script type="text/javascript">
    String.prototype.format = function () {
        var args = arguments;
        return this.replace(/\{(\d+)\}/g,
            function (m, i) {
                return args[i];
            });
    };
    String.prototype.signMix = function () {
        if (arguments.length === 0) return this;
        var param = arguments[0], str = this;
        if (typeof (param) === 'object') {
            for (var key in param)
                str = str.replace(new RegExp("\\{" + key + "\\}", "g"), param[key]);
            return str;
        } else {
            for (var i = 0; i < arguments.length; i++)
                str = str.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
            return str;
        }
    }
    /* var icon = {
         // 图标类型，现阶段只支持 image 类型
         type: 'image',
         // 图片 url
         image: 'https://a.amap.com/jsapi_demos/static/demo-center/marker/express2.png',
         // 图片尺寸
         size: [64, 30],
         // 图片相对 position 的锚点，默认为 bottom-center
         anchor: 'center',
     };*/
    //街道用的，例如：长洲（5）
    var textStyle = {
        fontSize: 14,
        fontWeight: 'normal',
        fillColor: '#88226d',
        strokeColor: '#fff',
        strokeWidth: 2,
        fold: true,
        padding: '2, 5',
    };
</script>
<script type="text/javascript"
        src="https://webapi.amap.com/maps?v=2.0&key=592ac591c3ad221629d4e47448a81436&plugin=AMap.Adaptor"></script>
<script src="https://a.amap.com/jsapi_demos/static/demo-center/js/demoutils.js"></script>
<script src="/map/huangpu.js"></script>
<script type="text/javascript">
    var map = new AMap.Map('container', {
        showIndoorMap: false,
        resizeEnable: true,
        viewMode: '2D', // 默认使用 2D 模式，如果希望使用带有俯仰角的 3D 模式，请设置 viewMode: '3D',
        center: [113.51417533122003, 23.229678207777793],
        zoom: 12, pitch: 60/*,
  mapStyle: 'amap://styles/macaron' */ //https://lbs.amap.com/demo/javascript-api/example/personalized-map/set-theme-style
    });

    //街道边界
    //http://datav.aliyun.com/portal/school/atlas/area_selector
    //https://geo.datav.aliyun.com/areas_v3/bound/440112.json
    function addPolygon(data, color, zoom) {
        let polygon = new AMap.Polygon({
            path: data,
            fillColor: color,
            strokeOpacity: 1,
            fillOpacity: 0.5,
            //name: '测试黄埔区',
            strokeColor: '#2b8cbe',
            strokeWeight: 1,
            strokeStyle: 'dashed',
            strokeDasharray: [5, 5],
            zooms: zoom
        });
        polygon.on('mouseover', () => {
            polygon.setOptions({
                fillOpacity: 0.7,
                fillColor: '#7bccc4'
            })
        })
        polygon.on('mouseout', () => {
            polygon.setOptions({
                fillOpacity: 0.5,
                fillColor: color

            })
        })
        map.add(polygon);
    }

    addPolygon(luogang, '#ccebc5', [1, 15]);
    addPolygon(lianghe, 'LightBLue', [1, 15]);
    addPolygon(hongshan, 'LightBLue', [1, 15]);
    addPolygon(dasha, 'Khaki', [1, 15]);
    addPolygon(nangang, 'LightBLue', [1, 15]);
    addPolygon(yonghe, 'LightBLue', [1, 15]);
    addPolygon(wenchong, 'pink', [1, 15]);
    addPolygon(changzhou, 'LightBLue', [1, 15]);
    addPolygon(yuzhu, 'pink', [1, 15]);
    addPolygon(xiagang, 'Khaki', [1, 15]);
    addPolygon(suidong, 'pink', [1, 15]);
    addPolygon(huangpustreet, '#ccebc5', [1, 15]);
    addPolygon(jiulong, 'snow', [1, 15]);
    //addPolygon(zhongcun, 'snow', [1, 20]);//番禺钟村
    //addPolygon(huangpudistrinct, 'snow', [1, 15]);
    //街道边界结束

    //信息窗体开始
    //构建自定义信息窗体
    function createInfoWindow(title, content) {
        var info = document.createElement("div");
        info.className = "custom-info input-card content-window-card";

        //可以通过下面的方式修改自定义窗体的宽高
        info.style.width = "250px";
        // 定义顶部标题
        var top = document.createElement("div");
        var titleD = document.createElement("div");
        var closeX = document.createElement("img");
        top.className = "info-top";
        titleD.innerHTML = title;
        closeX.src = "https://webapi.amap.com/images/close2.gif";
        closeX.onclick = closeInfoWindow;

        top.appendChild(titleD);
        top.appendChild(closeX);
        info.appendChild(top);

        // 定义中部内容
        var middle = document.createElement("div");
        middle.className = "info-middle";
        middle.style.backgroundColor = 'white';
        middle.innerHTML = content;
        info.appendChild(middle);

        // 定义底部内容
        var bottom = document.createElement("div");
        bottom.className = "info-bottom";
        bottom.style.position = 'relative';
        bottom.style.top = '0px';
        bottom.style.margin = '0 auto';
        var sharp = document.createElement("img");
        sharp.src = "https://webapi.amap.com/images/sharp.png";
        bottom.appendChild(sharp);
        info.appendChild(bottom);
        return info;
    }

    //关闭信息窗体
    function closeInfoWindow() {
        map.clearInfoWindow();
    }

    //信息窗体结束

    //街道label开始
    var allowCollision = false;
    var layer;
    layer = new AMap.LabelsLayer({
        zooms: [3, 20],
        zIndex: 1000,
        // collision: false,
        // 设置 allowCollision：true，可以让标注避让用户的标注
        allowCollision
    });
    // 图层添加到地图
    map.add(layer);

    function loadStreetMarker(type) {
        layer.clear();
        // 初始化 labelMarker 街道的label
        ajax('/map/getStreet3.jspa?assets={0}'.format(type), function (err, json) {
            if (!err) {
                var markers = [];
                json.forEach(function (item) {
                    //item.icon = icon; 注释掉，icon也注释掉，以后有需要再加上
                    item.text.style = textStyle;

                    var labelMarker = new AMap.LabelMarker(item);
                    markers.push(labelMarker);
                });
                // 将 marker 添加到图层
                layer.add(markers);
            }
        });
    }

    //map.setFitView(null, false, [100, 150, 10, 10]);
    toggleBtn();

    function allowCollisionFunc() {
        allowCollision = true;
        layer.setAllowCollision(true);
        toggleBtn();
    }

    function notAllowCollisionFunc() {
        allowCollision = false;
        layer.setAllowCollision(false);
        toggleBtn();
    }

    function toggleBtn() {
        var allowCollisionBtn = document.getElementById('allowCollision');
        var notAllowCollisionBtn = document.getElementById('notAllowCollision');
        var disableClass = 'disable';

        if (allowCollision) {
            allowCollisionBtn.classList.add(disableClass);
            notAllowCollisionBtn.classList.remove(disableClass);
        } else {
            allowCollisionBtn.classList.remove(disableClass);
            notAllowCollisionBtn.classList.add(disableClass);
        }
    }

    // 街道的label结束

    //大量信息点使用的图标
    // JSAPI 2.0 支持显示设置 zIndex, zIndex 越大约靠前，默认按顺序排列
    var style = [{
        url: '/assets/images/screen.png',
        anchor: new AMap.Pixel(6, 6), zIndex: 3,
        size: new AMap.Size(32, 22)
    }, {
        url: '/assets/images/server.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(32, 10),
        zIndex: 2,
    }, {
        url: 'https://webapi.amap.com/images/mass/mass2.png',
        anchor: new AMap.Pixel(3, 3),
        size: new AMap.Size(5, 5),
        zIndex: 1,
    }];
    //
    /*  function setStyle(multiIcon) {
          if (multiIcon) {
              mass.setStyle(style);
          } else {
              mass.setStyle(style[2]);
          }
      }*/

    var mass;

    function loadMassPoints() {
        var type = this.id;
        if (!type) type = 'led';
        closeInfoWindow();
        // layer.hide();
        if (mass) mass.clear();

        loadStreetMarker(type);
        ajax('/map/getAssetsList.jspa?assets={0}'.format(type), function (err, json) {
            if (!err) {
                mass = new AMap.MassMarks(json, {
                    opacity: 0.8,
                    zIndex: 111,
                    cursor: 'pointer',
                    style: style
                });

                // var marker = new AMap.Marker({content: ' ', map: map});
                mass.on('click', function (e) {
                    ajax('/map/getAssets.jspa?assets={0}&locationID={1}'.format(type, e.data.id), function (err, json) {
                        if (!err) {
                            var hh =
                                '<p>尺寸：<span style="color: #0288d1;font-weight:bold;">{size}</span></p>' +
                                '<p>系统分类：<span style="color: #0288d1;font-weight:bold;">{sysClass}</span></p>' +
                                '<p>通信方式：<span style="color: #0288d1;font-weight:bold;">{commMode}</span></p>' +
                                '<p>地址：<span style="color: #0288d1;font-weight:bold;">{address}</span></p>' +
                                '<p>联系人：<span style="color: #0288d1;font-weight:bold;">{link}</span></p>' +
                                '<p>业主：<span style="color: #0288d1;font-weight:bold;">{owner}</span></p>' +
                                '<p>街道：<span style="color: #0288d1;font-weight:bold;">{street}</span></p>';
                            var title = '<span style="font-size:11px;color:#123139;">{0}</span>'.format(json.location);
                            if (type === 'server') {
                                hh =
                                    '<p>网站名称：<span style="color: #0288d1;font-weight:bold;">{webName}</span></p>' +
                                    '<p>官网：<span style="color: #0288d1;font-weight:bold;">{www}</span></p>' +
                                    '<p>IP：<span style="color: #0288d1;font-weight:bold;">{ipFrom}</span></p>' +
                                    '<p>企业地址：<span style="color: #0288d1;font-weight:bold;">{address}</span></p>' +
                                    '<p>法定代表人：<span style="color: #0288d1;font-weight:bold;">{link}</span></p>' +
                                    '<p>电话：<span style="color: #0288d1;font-weight:bold;">{linkPhone}</span></p>' +
                                    '<p>等保级别：<span style="color: #0288d1;font-weight:bold;">{safeGrade}</span></p>' +
                                    '<p>街道：<span style="color: #0288d1;font-weight:bold;">{street}</span></p>';

                                title = '<span style="font-size:11px;color:#123139;">{0}</span>'.format(json.owner);
                            }
                            var infoWindow = new AMap.InfoWindow({
                                isCustom: true, //使用自定义窗体
                                content: createInfoWindow(title, hh.signMix(json)),
                                offset: new AMap.Pixel(20, -20)
                            });
                            infoWindow.open(map, [json.longitude, json.latitude]);
                        } else {
                            log.error('获取资产位置信息失败！')
                        }
                    })
                });
                mass.setMap(map);
            }
        });
    }

    loadMassPoints();

    //绑定radio点击事件
    var radios = document.querySelectorAll("#coordinate input");
    radios.forEach(function (ratio) {
        ratio.onclick = loadMassPoints;
    });
</script>
</body>
</html>