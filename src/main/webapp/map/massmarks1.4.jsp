<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>海量点</title>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <style>
        html, body, #container {
            height: 100%;
            width: 100%;
        }

        .input-card .btn {
            margin-right: 1.2rem;
            width: 9rem;
        }

        .input-card .btn:last-child {
            margin-right: 0;
        }
    </style>
</head>
<body>
<div id="container" class="map" tabindex="0"></div>
<div class="input-card">
    <h4>海量点效果切换</h4>
    <div class="input-item">
        <input type="button" class="btn" value="单一图标" onclick='setStyle(0)'/>
        <input type="button" class="btn" value="多个图标" onclick='setStyle(1)'/>
    </div>
</div>
<%--<script type="text/javascript" src='https://a.amap.com/jsapi_demos/static/citys.js'></script>--%>
<script type="text/javascript"
        src="https://webapi.amap.com/maps?v=1.4.15&key=b772bf606b75644e7c2f3dcda3639896"></script>
<script src="https://a.amap.com/jsapi_demos/static/demo-center/js/demoutils.js"></script>

<%--<script type="text/javascript" src="labelData.js"></script>--%>
<script type="text/javascript" src="/location/getStreet.jspa?draw=1"></script>
<script type="text/javascript">
    var map = new AMap.Map('container', {
        resizeEnable: true,
        viewMode: '2D', // 默认使用 2D 模式，如果希望使用带有俯仰角的 3D 模式，请设置 viewMode: '3D',
        center: [113.51417533122003, 23.239678207777793],
        zoom: 11, pitch: 60,
        mapStyle: 'amap://styles/whitesmoke'
    });
    //label开始
    var layer = new AMap.LabelsLayer({
        zooms: [4, 14],
        zIndex: 1000,
        // 开启标注避让，默认为开启，v1.4.15 新增属性
        collision: true,
        // 开启标注淡入动画，默认为开启，v1.4.15 新增属性
        animation: true,
    });

    map.add(layer);
    var markers = [];
    for (var i = 0; i < LabelsData.length; i++) {
        var curData = LabelsData[i];
        curData.extData = {
            index: i
        };

        var labelMarker = new AMap.LabelMarker(curData);

        markers.push(labelMarker);

        layer.add(labelMarker);
    }//label结束


    var style = [{
        url: '/assets/images/screen.png',
        anchor: new AMap.Pixel(6, 6),
        size: new AMap.Size(32,22)
    }, {
        url: 'https://a.amap.com/jsapi_demos/static/images/mass1.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(7, 7)
    }, {
        url: 'https://a.amap.com/jsapi_demos/static/images/mass2.png',
        anchor: new AMap.Pixel(3, 3),
        size: new AMap.Size(5, 5)
    }
    ];
    var assets =${assets};

    var mass = new AMap.MassMarks(assets, {
        opacity: 0.3,
        zIndex: 111,
        zooms: [13, 20],
        cursor: 'pointer',
        style: style
    });

    var marker = new AMap.Marker({content: ' ', map: map});

    mass.on('mouseover', function (e) {
        marker.setPosition(e.data.lnglat);
        marker.setLabel({content: e.data.name})
    });

    mass.setMap(map);

    function setStyle(multiIcon) {
        if (multiIcon) {
            mass.setStyle(style);
        } else {
            mass.setStyle(style[2]);
        }
    }


    //街道边界
    //http://datav.aliyun.com/portal/school/atlas/area_selector
    //https://geo.datav.aliyun.com/areas_v3/bound/440112.json
    ajax('huangpu.json', function (err, geoJSON) {
        if (!err) {
            var geojson = new AMap.GeoJSON({
                geoJSON: geoJSON,
                // 还可以自定义getMarker和getPolyline
                getPolygon: function (geojson, lnglats) {
                 /*   console.log('颜色', geojson.properties._parentProperities.color);
                    console.log('街道', geojson.properties._parentProperities.name);*/

                    var color = geojson.properties._parentProperities.color;
                    if (!color) color = 'black';
                    // 计算面积
                    var area = AMap.GeometryUtil.ringArea(lnglats[0])

                    return new AMap.Polygon({
                        path: lnglats,
                        text: geojson.properties._parentProperities.name,
                        fillOpacity: 0.2,// 面积越大透明度越高
                        strokeColor: 'white',
                        fillColor: color
                    });
                }/*,
        getMarker: function (geojson, lnglats) {
          console.log('点', lnglats)
        },
        getPolyline: function (geojson, lnglats) {
          console.log('线', lnglats);
        }*/

            });

            geojson.setMap(map);

            log.success('黄埔区街道边界加载完成')
        } else {
            log.error('黄埔区街道边界请求失败')
        }
    })
</script>
</body>
</html>