<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>点聚合</title>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <style>
        html, body, #container {
            height: 100%;
            width: 100%;
        }

        .input-card {
            width: 25rem;
        }

        .input-card .btn {
            width: 7rem;
            margin-right: .7rem;
        }

        .input-card .btn:last-child {
            margin-right: 0;
        }
    </style>
</head>
<body>
<div id="container" class="map" tabindex="0"></div>
<div class="input-card">
    <h4>聚合点效果切换</h4>
    <div class="input-item">
        <input type="button" class="btn" value="默认样式" id="add0" onclick='addCluster(0)'/>
        <input type="button" class="btn" value="自定义图标" id="add1" onclick='addCluster(1)'/>
        <input type="button" class="btn" value="完全自定义" id="add2" onclick='addCluster(2)'/>
    </div>
</div>
<%--
<script src="//a.amap.com/jsapi_demos/static/china.js"></script>--%>
<script type="text/javascript"
        src="https://webapi.amap.com/maps?v=1.4.15&key=&plugin=AMap.MarkerClusterer"></script>
<script src="https://a.amap.com/jsapi_demos/static/demo-center/js/demoutils.js"></script>
<script type="text/javascript">
    var cluster, markers = [];
    var points =${assets};
    var map = new AMap.Map("container", {
        resizeEnable: true,
        viewMode: '2D', // 默认使用 2D 模式，如果希望使用带有俯仰角的 3D 模式，请设置 viewMode: '3D',
        center: [113.51417533122003, 23.239678207777793],
        zoom: 11
    });

    for (var i = 0; i < points.length; i += 1) {
        markers.push(new AMap.Marker({
            position: points[i]['lnglat'],
            content: '<div style="background-color: hsla(180, 100%, 50%, 0.7); height: 24px; width: 24px; border: 1px solid hsl(180, 100%, 40%); border-radius: 12px; box-shadow: hsl(180, 100%, 50%) 0px 0px 1px;"></div>',
            offset: new AMap.Pixel(-15, -15)
        }))
    }

    var count = markers.length;
    var _renderClusterMarker = function (context) {
        var factor = Math.pow(context.count / count, 1 / 18);
        var div = document.createElement('div');
        var Hue = 180 - factor * 180;
        var bgColor = 'hsla(' + Hue + ',100%,50%,0.7)';
        var fontColor = 'hsla(' + Hue + ',100%,20%,1)';
        var borderColor = 'hsla(' + Hue + ',100%,40%,1)';
        var shadowColor = 'hsla(' + Hue + ',100%,50%,1)';
        div.style.backgroundColor = bgColor;
        var size = Math.round(30 + Math.pow(context.count / count, 1 / 5) * 20);
        div.style.width = div.style.height = size + 'px';
        div.style.border = 'solid 1px ' + borderColor;
        div.style.borderRadius = size / 2 + 'px';
        div.style.boxShadow = '0 0 1px ' + shadowColor;
        div.innerHTML = context.count;
        div.style.lineHeight = size + 'px';
        div.style.color = fontColor;
        div.style.fontSize = '14px';
        div.style.textAlign = 'center';
        context.marker.setOffset(new AMap.Pixel(-size / 2, -size / 2));
        context.marker.setContent(div)
    };

    addCluster(1);

    function addCluster(tag) {
        if (cluster) {
            cluster.setMap(null);
        }
        if (tag === 2) {//完全自定义
            cluster = new AMap.MarkerClusterer(map, markers, {
                gridSize: 80,
                renderClusterMarker: _renderClusterMarker
            });
        } else if (tag === 1) {//自定义图标
            var sts = [{
                url: "https://a.amap.com/jsapi_demos/static/images/blue.png",
                size: new AMap.Size(32, 32),
                offset: new AMap.Pixel(-16, -16)
            }, {
                url: "https://a.amap.com/jsapi_demos/static/images/green.png",
                size: new AMap.Size(32, 32),
                offset: new AMap.Pixel(-16, -16)
            }, {
                url: "https://a.amap.com/jsapi_demos/static/images/orange.png",
                size: new AMap.Size(36, 36),
                offset: new AMap.Pixel(-18, -18)
            }, {
                url: "https://a.amap.com/jsapi_demos/static/images/red.png",
                size: new AMap.Size(48, 48),
                offset: new AMap.Pixel(-24, -24)
            }, {
                url: "https://a.amap.com/jsapi_demos/static/images/darkRed.png",
                size: new AMap.Size(48, 48),
                offset: new AMap.Pixel(-24, -24)
            }];
            cluster = new AMap.MarkerClusterer(map, markers, {
                styles: sts,
                gridSize: 80
            });
        } else {//默认样式
            cluster = new AMap.MarkerClusterer(map, markers, {gridSize: 80});
        }
    }
    // 无参数，默认包括所有覆盖物的情况
    //cluster.setFitView();
    /*var currentZoom = map.getZoom();
    if (currentZoom < 13)*/
        ajax('huangpu.json', function (err, geoJSON) {
            if (!err) {
                var geojson = new AMap.GeoJSON({
                    geoJSON: geoJSON,
                    // 还可以自定义getMarker和getPolyline
                    getPolygon: function (geojson, lnglats) {
                        console.log('颜色', geojson.properties._parentProperities.color);
                        console.log('街道', geojson.properties._parentProperities.name);

                        var color = geojson.properties._parentProperities.color;
                        if (!color) color = 'pink';
                        // 计算面积
                        var area = AMap.GeometryUtil.ringArea(lnglats[0])

                        return new AMap.Polygon({
                            path: lnglats,
                            text: geojson.properties._parentProperities.name,
                            fillOpacity: 0.3,// 面积越大透明度越高
                            strokeColor: 'white',
                            fillColor: color
                        });
                    },
                    getMarker: function (geojson, lnglats) {
                        console.log('点', lnglats)
                    },
                    getPolyline: function (geojson, lnglats) {
                        console.log('线', lnglats);
                    }

                });

                geojson.setMap(map);

                log.success('GeoJSON 数据加载完成')
            } else {
                log.error('GeoJSON 服务请求失败')
            }
        })
</script>
</body>
</html>