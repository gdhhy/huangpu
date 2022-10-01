<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<div class="input-card" style="width:11rem;left:10px;top:10px;bottom:auto">
    <h4>资产类型</h4>
    <div id="coordinate">
        <div class="input-item"><input id="led" name="language" type="radio" checked="checked"><span class="input-text">LED</span></div>
        <div class="input-item"><input id="idc" name="language" type="radio"><span class="input-text">IDC</span></div>
        <div class="input-item"><input id="netbar" name="language" type="radio"><span class="input-text">网吧</span></div>
        <div class="input-item"><input id="secsys" name="language" type="radio"><span class="input-text">等保系统</span></div>
    </div>
</div>
<%--<div class="input-card">
    <label style="color:grey">标注避让设置</label>
    <div class="input-item">
        <input id="allowCollision" type="button" class="btn collision-btn" onclick="allowCollisionFunc()"
               value="标注避让">
        <input id="notAllowCollision" type="button" class="btn collision-btn" onclick="notAllowCollisionFunc()" value="显示全部">
    </div>
</div>--%>
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
                str = str.replace(new RegExp("\\{" + key + "\\}", "g"), param[key] === null ? "" : param[key]);
            return str;
        } else {
            for (var i = 0; i < arguments.length; i++)
                str = str.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
            return str;
        }
    }
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
<script type="text/javascript" src="https://webapi.amap.com/maps?v=2.0&key=${key1}&plugin=AMap.Adaptor"></script>
<script src="https://a.amap.com/jsapi_demos/static/demo-center/js/demoutils.js"></script>
<script src="https://webapi.amap.com/ui/1.1/main.js?v=1.1.1"></script>
<script src="/map/huangpu.js"></script>
<script type="text/javascript">
    AMapUI.loadUI(['control/BasicControl'], function (BasicControl) {
        var layerCtrl1 = new BasicControl.LayerSwitcher({
            position: 'tr'
        });
        var map = new AMap.Map('container', {
            showIndoorMap: false,
            resizeEnable: true,
            viewMode: '2D', // 默认使用 2D 模式，如果希望使用带有俯仰角的 3D 模式，请设置 viewMode: '3D',
            center: [113.5141753, 23.2296782],
            layers: layerCtrl1.getEnabledLayers(),
            // layers: [new AMap.TileLayer.Satellite()],
            zoom: 11, pitch: 60/*,
  mapStyle: 'amap://styles/macaron' */ //https://lbs.amap.com/demo/javascript-api/example/personalized-map/set-theme-style
        });
        map.addControl(layerCtrl1);
        map.on('zoomstart', closeInfoWindow);
        map.on('zoomchange', closeInfoWindow);
        map.on('zoomend', closeInfoWindow);

        //街道边界
        //http://datav.aliyun.com/portal/school/atlas/area_selector
        //https://geo.datav.aliyun.com/areas_v3/bound/440112.json
        function addPolygon(data, color, zoom) {
            let polygon = new AMap.Polygon({
                path: data,
                fillColor: color,
                strokeOpacity: 1,
                fillOpacity: 0.2,
                //name: '测试黄埔区',
                strokeColor: '#2b8cbe',
                strokeWeight: 1,
                strokeStyle: 'dashed',
                strokeDasharray: [5, 5],
                zooms: zoom
            });
            polygon.on('mouseover', () => {
                polygon.setOptions({
                    fillOpacity: 0.5,
                    fillColor: '#b6d8d4'
                })
            })
            polygon.on('mouseout', () => {
                polygon.setOptions({
                    fillOpacity: 0.2,
                    fillColor: color
                })
            })
            map.add(polygon);
        }

        /*addPolygon(luogang, '#ccebc5', [1, 15]);
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
        addPolygon(jiulong, 'snow', [1, 15]);*/
        //addPolygon(zhongcun, 'snow', [1, 20]);//番禺钟村
        addPolygon(huangpudistrinct, 'LightBLue', [1, 13]);
        //街道边界结束

        //信息窗体开始
        //构建自定义信息窗体
        function createInfoWindow(title, titleColor, content) {
            var info = document.createElement("div");
            info.className = "custom-info input-card content-window-card";

            //可以通过下面的方式修改自定义窗体的宽高
            info.style.width = "300px";
            // 定义顶部标题
            var top = document.createElement("div");
            var titleD = document.createElement("div");
            var closeX = document.createElement("img");
            top.className = "info-top";
            //top.style.mixBlendMode ="difference";用了背景颜色会随机
            top.style.backgroundColor = titleColor;
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

        function createImageWindow(imageUrl) {
            var imageDiv = document.createElement("div");
            imageDiv.className = "custom-info input-card content-window-card";
            imageDiv.style.width = "400px";
            imageDiv.style.zIndex = "100";
            imageDiv.innerHTML = "<img border='0' style='object-fit: cover;width:400px;height: 300px' src=\"/upload/" + imageUrl + "\">";
            // imageDiv.style.display = "";

            // 定义底部内容
            var bottom = document.createElement("div");
            bottom.className = "info-bottom";
            bottom.style.position = 'relative';
            bottom.style.top = '0px';
            bottom.style.margin = '0 auto';
            var sharp = document.createElement("img");
            sharp.src = "https://webapi.amap.com/images/sharp.png";
            bottom.appendChild(sharp);
            imageDiv.appendChild(bottom);
            return imageDiv;
        }

        //信息窗体结束

        //街道label开始
        //var allowCollision = false;
        var layer = new AMap.LabelsLayer({
            zooms: [3, 20],
            zIndex: 1000,
            // collision: false,
            // 设置 allowCollision：true，可以让标注避让用户的标注
            allowCollision:false
        });
        // 图层添加到地图
        map.add(layer);

        function loadStreetMarker(type) {
            layer.clear();
            // 初始化 labelMarker 街道的label
            ajax('/map/getStreet3.jspa?assetsType={0}'.format(type), function (err, json) {
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

        // 街道的label结束

        //大量信息点使用的图标
        // JSAPI 2.0 支持显示设置 zIndex, zIndex 越大约靠前，默认按顺序排列
        var style = [{
            url: '/assets/images/led.png',
            anchor: new AMap.Pixel(6, 6), zIndex: 3,
            size: new AMap.Size(32, 22)
        }, {
            url: '/assets/images/idc.png',
            anchor: new AMap.Pixel(4, 4),
            size: new AMap.Size(24, 24),
            zIndex: 2,
        }, {
            url: '/assets/images/netbar.png',
            anchor: new AMap.Pixel(3, 3),
            size: new AMap.Size(32, 32),
            zIndex: 1,
        }, {
            url: '/assets/images/secsys.png',
            anchor: new AMap.Pixel(3, 3),
            size: new AMap.Size(24, 24),
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
            ajax('/map/getAssetsList.jspa?assetsType={0}'.format(type), function (err, json) {
                if (!err) {
                    mass = new AMap.MassMarks(json, {opacity: 0.8, zIndex: 111, cursor: 'pointer', style: style});
                    // var marker = new AMap.Marker({content: ' ', map: map});
                    mass.on('mouseover', function (e) {
                        //console.log("click：" + e.data.imageUrl);
                        ajax('/map/getAssets.jspa?&assetsID={0}'.format(e.data.id), function (err, json) {
                            if (!err) {
                                var extJson = JSON.parse(json.extJson);

                                var title = "", hh = '<p>状态：<span style="color: #0288d1;font-weight:bold;">{status}</span></p>' +
                                    '<p>地址：<span style="color: #0288d1;font-weight:bold;">{address}</span></p>' +
                                    '<p>街道：<span style="color: #0288d1;font-weight:bold;">{street}</span></p>';
                                if (type === 'led') {
                                    hh += '<p>业主：<span style="color: #0288d1;font-weight:bold;">{owner}</span></p>' +
                                        '<p>联系人及电话：<span style="color: #0288d1;font-weight:bold;">{link}，{linkPhone}</span></p>';

                                    title = '<span style="font-size:11px;color:white;">{0}</span>'.format(json.name);
                                } else {
                                    hh += '<p>法定代表人及电话：<span style="color: #0288d1;font-weight:bold;">{link}</span></p>';

                                    title = '<span style="font-size:11px;color:white">{0}</span>'.format(json.owner);
                                }

                                let line = '<p>{key}：<span style="color: #0288d1;font-weight:bold;">{value}</span></p>';
                                if (extJson)
                                    extJson.forEach(function (obj, index, array) {
                                        if (obj.value)
                                            hh += line.signMix(obj);
                                    });

                                var infoWindow = new AMap.InfoWindow({
                                    isCustom: true, //使用自定义窗体
                                    content: createInfoWindow(title, json.color, hh.signMix(json)),
                                    offset: new AMap.Pixel(20, -20)
                                });
                                infoWindow.open(map, [json.longitude, json.latitude]);
                            } else {
                                log.error('获取资产位置信息失败！')
                            }
                        });
                    });
                    mass.on('mouseout', function (e) {
                        closeInfoWindow();
                    });
                    mass.on('click', function (e) {
                        closeInfoWindow();//关掉信息窗口，显示图片
                        if (e.data.imageUrl) {
                           // console.log("e.data:" + e.data.imageUrl);
                            var imageWindow = new AMap.InfoWindow({
                                isCustom: true, //使用自定义窗体
                                content: createImageWindow(e.data.imageUrl),
                                offset: new AMap.Pixel(20, -20)
                            });
                            imageWindow.open(map, [e.data.longitude, e.data.latitude]);
                        }
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
    });
</script>
</body>
</html>