<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>黄埔区新冠疫情流调地图</title>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <style>
        html,
        body,
        #map {
            width: 100%;
            height: 100%;
            margin: 0;
            padding: 0;
        }

        .demo-title {
            position: absolute;
            top: 25px;
            left: 225px;
            z-index: 1;
        }

        h1 {
            font-size: 22px;
            margin: 0;
            color: grey;
        }


        /*左上角*/
        .input-card .btn {
            margin-right: 1.2rem;
        }

        .input-card .btn:last-child {
            margin-right: 0;
        }

        /*以下：自定义信息窗口样式*/

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
    </style>
</head>

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
    };
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
<body>
<div class="input-card" style="width:13rem;left:10px;top:10px;bottom:auto;z-index: 100">
    <h4 style="font-weight: bold">风险类型</h4>
    <div id="coordinate">
        <div class="input-item"><input id="highRisk" name="language" type="checkbox" checked="checked"><span class="input-text">高风险(${highRisk})</span></div>
        <div class="input-item"><input id="knit" name="language" type="checkbox" checked="checked"><span class="input-text">密接(${knit})</span></div>
        <div class="input-item"><input id="subknit" name="language" type="checkbox" checked="checked"><span class="input-text">次密接(${subknit})</span></div>
        <div class="input-item"><input id="important" name="language" type="checkbox" checked="checked"><span class="input-text">重点人群(${important})</span></div>
    </div>
</div>
<div class="input-card" style="width:31rem;left:10px;top:250px;bottom:auto;z-index: 100; "> <%--background: rgba(12,0,255,0.1)--%>
    <h4 style="font-weight: bold">街道</h4>
    <div id="coordinate2"></div>
</div>
<div class="demo-title">
    <h1>黄埔区新冠疫情流调地图</h1>
</div>
<div id="map"></div>
<script src="https://webapi.amap.com/maps?v=2.0&key=${key1}&plugin=AMap.Scale,AMap.ToolBar"></script>
<script src="https://webapi.amap.com/loca?v=2.0.0&key=${key1}"></script>
<script src="https://a.amap.com/jsapi_demos/static/demo-center/js/demoutils.js"></script>
<script src="https://webapi.amap.com/ui/1.1/main.js?v=1.1.2"></script>
<script src="/map/huangpu.js"></script>

<script>
    var map = new AMap.Map('map', {
        zoom: 12,
        showLabel: true,
        viewMode: '2D', // 默认使用 2D 模式，如果希望使用带有俯仰角的 3D 模式，请设置 viewMode: '3D',
        center: [113.4841753, 23.2096782],
        pitch: 60
        //mapStyle: 'amap://styles/45311ae996a8bea0da10ad5151f72979',
    });
    AMapUI.loadUI(['control/BasicControl'], function (BasicControl) {
        var layerCtrl1 = new BasicControl.LayerSwitcher({
            position: 'tr'
        });
        map.addControl(layerCtrl1);
       /* var tool = new AMap.ToolBar();//+-缩放工具
        tool.addTo(map);*/

        var loca = new Loca.Container({
            map,
        });
        // 图例
        var lengend = new Loca.Legend({
            loca: loca,
            title: {
                label: '图列',
                fontColor: 'rgba(9,0,255,0.4)',
                fontSize: '16px',
            },
            style: {
                backgroundColor: 'rgba(0,0,255,0.2)',
                right: '20px',
                bottom: '30px',
                fontSize: '12px',
            },
            dataMap: [
                {label: '有高风险、密接', color: 'red'},
                {label: '无高风险、密接', color: 'orange'}
            ],
        });
        //呼吸点
        var geoR = new Loca.GeoJSONSource({
            url: '/crowdmap/crowd.jspa?color=red',
        });
        var geoY = new Loca.GeoJSONSource({
            url: '/crowdmap/crowd.jspa?color=yellow',
        });


        var breathRed = new Loca.ScatterLayer({
            zIndex: 121,
        });
        breathRed.setSource(geoR);
        breathRed.setStyle({
            unit: 'px',
            //size: [50, 50],
            size: (index, f) => {
                var n = Math.log(f.properties['highRisk'] + f.properties['knit'] + f.properties['subknit'] + f.properties['important']) * 10;
                return [n, n];
            },
            texture: 'https://a.amap.com/Loca/static/loca-v2/demos/images/breath_red.png',
            animate: true,
            duration: 1000,
        });

        var breathYellow = new Loca.ScatterLayer({
            zIndex: 121,
        });
        breathYellow.setSource(geoY);
        breathYellow.setStyle({
            unit: 'px',
            //size: [50, 50],
            size: (index, f) => {
                var n = Math.log(f.properties['highRisk'] + f.properties['knit'] + f.properties['subknit'] + f.properties['important']) * 10;
                return [n, n];
            },
            texture: 'https://a.amap.com/Loca/static/loca-v2/demos/images/breath_yellow.png',
            animate: true,
            duration: 1000,
        });
        loca.add(breathRed);
        loca.add(breathYellow);
        loca.animate.start();
        //呼吸点结束

        //街道边界
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
            });
            polygon.on('mouseout', () => {
                polygon.setOptions({
                    fillOpacity: 0.2,
                    fillColor: color
                })
            });
            map.add(polygon);
        }

        addPolygon(huangpudistrinct, 'LightBLue', [1, 13]);
        //街道边界结束

        //街道label开始
        //var allowCollision = false;
        var layer = new AMap.LabelsLayer({
            zooms: [3, 20],
            zIndex: 1000,
            // collision: false,
            // 设置 allowCollision：true，可以让标注避让用户的标注
            allowCollision: false
        });
        // 图层添加到地图
        map.add(layer);
        var streetJson;

        function loadStreetMarker() {
            layer.clear();
            // 初始化 labelMarker 街道的label
            ajax('/crowdmap/getStreet3.jspa', function (err, json) {
                if (!err) {
                    streetJson = json;
                    var markers = [];
                    json.forEach(function (item) {
                        //item.icon = icon; 注释掉，icon也注释掉，以后有需要再加上
                        item.text.style = textStyle;

                        var labelMarker = new AMap.LabelMarker(item);
                        markers.push(labelMarker);

                        if (item.highRisk + item.knit + item.subknit + item.important > 0)
                            $('#coordinate2').append(('<div class="input-item"><input id="{0}" name="crowd" type="radio">' +
                                '<span class="input-text"><span style="font-weight: bold" >{1}：</span>高风险：{2}，密接：{3}，次密：{4}，重点：{5}</span></div>')
                                .format(item.name, item.streetName, item.highRisk, item.knit, item.subknit, item.important));
                    });
                    // 将 marker 添加到图层
                    layer.add(markers);

                    //绑定街道radio点击事件
                    var radios = document.querySelectorAll("#coordinate2 input");
                    radios.forEach(function (ratio) {
                        ratio.onclick = changeCenterZoom;
                    });
                }
            });
        } // 街道的label结束
        loadStreetMarker();

        function changeCenterZoom() {
            var streetID = this.id;
            streetJson.forEach(function (item) {
                if (item.name === streetID) {
                    map.setCenter(item.position);
                    map.setZoom(15);
                }
            });
        }

        //绑定风险类型checkbox点击事件，重新累加风险人群数量
        var radios2 = document.querySelectorAll("#coordinate input");
        radios2.forEach(function (ratio) {
            ratio.onclick = refreshCount;
        });

        function refreshCount() {
            breathRed.setStyle({
                unit: 'px',
                size: (index, f) => {
                    let ss = 0;
                    if ($('#highRisk').is(':checked')) ss += f.properties['highRisk'];
                    if ($('#knit').is(':checked')) ss += f.properties['knit'];
                    if ($('#subknit').is(':checked')) ss += f.properties['subknit'];
                    if ($('#important').is(':checked')) ss += f.properties['important'];
                    let n = Math.log(ss) * 10;
                    return [n, n];
                },
                texture: 'https://a.amap.com/Loca/static/loca-v2/demos/images/breath_red.png',
                // texture: 'https://a.amap.com/Loca/static/loca-v2/demos/images/breath_yellow.png',
                animate: true,
                duration: 1000,
            });
            breathYellow.setStyle({
                unit: 'px',
                size: (index, f) => {
                    let ss = 0;
                    if ($('#highRisk').is(':checked')) ss += f.properties['highRisk'];
                    if ($('#knit').is(':checked')) ss += f.properties['knit'];
                    if ($('#subknit').is(':checked')) ss += f.properties['subknit'];
                    if ($('#important').is(':checked')) ss += f.properties['important'];
                    let n = Math.log(ss) * 10;
                    return [n, n];
                },
                texture: 'https://a.amap.com/Loca/static/loca-v2/demos/images/breath_yellow.png',
                animate: true,
                duration: 1000,
            });
        }

        //信息窗体开始
        //构建自定义信息窗体
        function createInfoWindow(title, content) {
            var info = document.createElement("div");
            info.className = "custom-info input-card content-window-card";

            //可以通过下面的方式修改自定义窗体的宽高
            info.style.width = "430px";
            // 定义顶部标题
            var top = document.createElement("div");
            var titleD = document.createElement("div");
            var closeX = document.createElement("img");
            top.className = "info-top";
            //top.style.mixBlendMode ="difference";用了背景颜色会随机
            //top.style.backgroundColor = 'grey';
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

        var style = [{
            url: 'https://webapi.amap.com/images/mass/mass2.png',
            anchor: new AMap.Pixel(3, 3),
            size: new AMap.Size(5, 5),
            zIndex: 1,
        }];
        var mass;

        function loadMassPoints() {
            closeInfoWindow();
            // layer.hide();
            if (mass) mass.clear();

            ajax('/crowdmap/getCrowdList.jspa', function (err, json) {
                if (!err) {
                    mass = new AMap.MassMarks(json, {opacity: 0.8, zIndex: 111, cursor: 'pointer', style: style});
                    mass.on('mouseover', function (e) {
                        //var title = '<span style="font-size:11px;;color:#F00">{0}</span>'.format(e.data.location);
                        var hh = '<p>病人：<span style="color: #0288d1;font-weight:bold;">{patient}</span></p>' +
                            '<p>地址：<span style="color: #0288d1;font-weight:bold;">{address}</span></p>' +
                            '<p>停留时段：<span style="color: #0288d1;font-weight:bold;">{stayTime}</span></p>' +
                            '<p>高风险：<span style="color: #F00;font-weight:bold;">{highRisk}</span>，' +
                            '密接：<span style="color: #F00;font-weight:bold;">{knit}</span>，' +
                            '次密接：<span style="color: #F00;font-weight:bold;">{subknit}</span>，' +
                            '重点人群：<span style="color: #F00;font-weight:bold;">{important}</span></p>';//purple
                        var infoWindow = new AMap.InfoWindow({
                            isCustom: true, //使用自定义窗体
                            content: createInfoWindow(e.data.location, hh.signMix(e.data)),
                            offset: new AMap.Pixel(20, -20)
                        });
                        infoWindow.open(map, e.data.lnglat);
                    });
                   /* mass.on('mouseout', function (e) {
                        closeInfoWindow();
                    });*/

                    mass.setMap(map);
                }
            });
        }

        loadMassPoints();
    });
</script>
</body>
</html>