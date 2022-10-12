<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <script src="../js/jquery-3.5.1.js"></script>

    <script src="../js/bootstrap.js"></script>
    <script src="../components/jquery-ui/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="https://cdn.bootcss.com/jqueryui/1.12.1/jquery-ui.min.css"/>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <title>拖拽选址123</title>
    <style>
        html,
        body {
            height: 100%;
            margin: 0;
            width: 100%;
            padding: 0;
            overflow: hidden;
            font-size: 13px;
        }

        .map {
            width: 100%;
            height: 100%;
            margin: 0;
            padding: 0;
        }


        button {
            border: solid 1px;
            margin-left: 15px;
            background-color: #dadafa;
        }

        .c {
            font-weight: 600;
            padding-left: 15px;
            padding-top: 4px;
        }


        #nearestJunction,
        #nearestRoad,
        #nearestPOI,
        .title, .posInfo {
            padding-left: 15px;
        }

        /*左上角*/
        .input-card .btn {
            margin-right: 1.2rem;
        }

        .input-card .btn:last-child {
            margin-right: 0;
        }

        .amap-icon img,
        .amap-marker-content img {
            width: 25px;
            height: 34px;
        }

        .marker {
            position: absolute;
            top: -20px;
            right: -118px;
            color: #fff;
            padding: 4px 10px;
            box-shadow: 1px 1px 1px rgba(10, 10, 10, .2);
            white-space: nowrap;
            font-size: 12px;
            font-family: "";
            background-color: #25A5F7;
            border-radius: 3px;
        }

        #outer-box {
            height: 100%;
            padding-right: 300px;
        }

        #container {
            height: 100%;
            width: 100%;
        }

        #panel {

            position: absolute;
            top: 0;
            bottom: 0;
            right: 0;
            height: 100%;
            overflow: auto;
            width: 300px;
            z-index: 999;
            border-left: 1px solid #eaeaea;
            background: #fff;
        }

        #searchBar {
            height: 30px;
            background: #ccc;
        }

        #searchInput {
            width: 100%;
            height: 30px;
            line-height: 30%;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            border: none;
            border-bottom: 1px solid #ccc;
            padding: 0 5px;
        }

        #searchResults {
            overflow: auto;
            height: calc(100% - 30px);
        }

        .amap_lib_placeSearch,
        .amap-ui-poi-picker-sugg-container {
            border: none !important;
        }

        .amap_lib_placeSearch .poibox.highlight {
            background-color: #CAE1FF;
        }

        .poi-more {
            display: none !important;
        }

        .demo-title {
            position: absolute;
            top: 25px;
            left: 325px;
            z-index: 1;
        }

        h1 {
            font-size: 22px;
            margin: 0;
            color: grey;
        }

        span {
            margin-left: 5px;
            font-size: 11px;
        }
    </style>
</head>

<body>
<div class="demo-title">
    <h1>拖拽选址（未获试用授权，随时可能不能用）</h1>
</div>
<div class="input-card" style="width:20rem;left:10px;top:10px;bottom:auto;z-index: 100">
    <h4 style="font-weight: bold">原位置</h4>
    <div id="coordinate2">
        <div class='c'>场所:</div>
        <div class="posInfo"><a title="搜索场所" id="goLocation" href="#">${location}&nbsp;</a></div>
        <div class='c'>地址:</div>
        <div class="posInfo"><a title="搜索地址" id="goAddress" href="#">${address}&nbsp;</a></div>
        <div class='c'>经纬度:</div>
        <div class="posInfo"><a title="回到本位置" id="goback" href="#">${longitude},${latitude}</a>&nbsp;</div>
        <div class='c'>街道:</div>
        <div class="posInfo">${street}&nbsp;</div>
    </div>

    <hr>
    <h4 style="font-weight: bold">选址结果</h4>
    <div id="coordinate3">
        <div class='c'>地址:</div>
        <div class="posInfo" id='address'>&nbsp;</div>
        <div class='c'>经纬度:</div>
        <div class="posInfo" id='lnglat'>&nbsp;</div>
        <div class='c'>街道:</div>
        <div class="posInfo" id="street">&nbsp;</div>
    </div>
    <hr>
    <div style="text-align:center;">
        <button type="button" class="btn btn-info" id="saveBtn">
            <span class="glyphicon glyphicon-save"></span> 保存
        </button>
    </div>
</div>
<div id="outer-box">
    <div id="container" class="map" tabindex="0"></div>
    <div id="panel" class="scrollbar1">
        <div id="searchBar">
            <input id="searchInput" placeholder="输入关键字搜素POI"/>
        </div>
        <div id="searchResults"></div>
    </div>
</div>

<script type="text/javascript" src='//webapi.amap.com/maps?v=2.0&key=${key3}&plugin=AMap.ToolBar'></script>
<!-- UI组件库 1.1 -->
<script src="//webapi.amap.com/ui/1.1/main.js?v=1.1.2"></script>
<script type="text/javascript">
    String.prototype.format = function () {
        var args = arguments;
        return this.replace(/\{(\d+)\}/g,
            function (m, i) {
                return args[i];
            });
    };

    var home = [${huangpuCenter}];
    var zoom = 11.7;
    <c:if test="${longitude < longitudeMax and  longitude >  longitudeMin  and latitude < latitudeMax and latitude > latitudeMin}">
    home = [${longitude}, ${latitude}];
    zoom =${zoom};
    </c:if>
    var map = new AMap.Map('container', {
        zoom: zoom
    });

    AMapUI.loadUI(['control/BasicControl'], function (BasicControl) {
        var layerCtrl1 = new BasicControl.LayerSwitcher({
            position: 'tr'
        });
        map.addControl(layerCtrl1);
    });
    AMapUI.loadUI(['misc/PositionPicker'], function (PositionPicker) {

        var positionPicker = new PositionPicker({
            mode: 'dragMap',//dragMap、dragMarker
            map: map
        });
        positionPicker.on('success', function (positionResult) {
            $('#lnglat').html('<span style="color: #0288d1;font-weight:bold;">{0}</span>'.format(positionResult.position));
            $('#address').text(positionResult.address);
            $('#street').html(' ');
            if (positionResult.address.indexOf("街道") >= 2) {
                if (positionResult.address.indexOf("黄埔区") >= 0)
                    $('#street').html('<span style="color: #0288d1;font-weight:bold;">{0}</span>'
                        .format(positionResult.address.substring(positionResult.address.indexOf("街道") - 2, positionResult.address.indexOf("街道"))));
                else
                    $('#street').html('<span style="color: red;font-weight:bold;">{0}</span>'
                        .format(positionResult.address.substring(positionResult.address.indexOf("街道") - 2, positionResult.address.indexOf("街道"))));
            }

        });
        positionPicker.on('fail', function (positionResult) {
            $('#lnglat').html(' ');
            $('#address').html(' ');
            $('#street').html(' ');
        });
        positionPicker.start();
        map.panBy(0, 1);
        if (home[0])
            map.setCenter(home);//没这一行，总会偏移小小

        map.addControl(new AMap.ToolBar({
            liteStyle: true
        }));

        new AMap.Marker({
            map: map,
            //icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png",
            position: home,
            offset: new AMap.Pixel(-13, -30)
        });

        $('#goback').on('click', function () {
            map.setCenter(home);
        });
    });

    AMapUI.loadUI(['misc/PoiPicker'], function (PoiPicker) {

        var poiPicker = new PoiPicker({
            input: 'searchInput',
            placeSearchOptions: {
                map: map,
                pageSize: 10
            },
            searchResultsContainer: 'searchResults'
        });

        poiPicker.on('poiPicked', function (poiResult) {
            var source = poiResult.source,
                poi = poiResult.item;

            if (source !== 'search') {
                //suggest来源的，同样调用搜索
                poiPicker.searchByKeyword(poi.name);
            }
        });

        $('#goLocation').on('click', function () {
            search($('#goLocation').text());
        });
        $('#goAddress').on('click', function () {
            search($('#goAddress').text());
        });

        function search(searchText) {
            if (searchText.length > 2 && searchText.indexOf("黄埔") < 0) searchText = "黄埔区" + searchText;
            $('#searchInput').val(searchText);
            poiPicker.searchByKeyword(searchText);
        }
    });

    $('#saveBtn').on('click', function () {
        var lnglat = $('#lnglat').text().split(",");
        //console.log("lnglat:" + lnglat[0]);
        if (2 === lnglat.length) {
            var submitData = {objectID: '${objectID}', longitude: lnglat[0], latitude: lnglat[1], street: $('#street').text()};
            $.ajax({
                type: "POST",
                url: "/${savePath}/saveColor.jspa",
                data: JSON.stringify(submitData),
                //contentType: "application/x-www-form-urlencoded; charset=UTF-8",//http://www.cnblogs.com/yoyotl/p/5853206.html
                contentType: "application/json; charset=utf-8",
                cache: false,
                success: function (response, textStatus) {
                    var result = JSON.parse(response);
                    if (!result.succeed) {
                        $("#errorText").html(result.message);
                        $("#dialog-error").removeClass('hide').dialog({
                            modal: true,
                            width: 600,
                            title: result.title,
                            buttons: [{
                                text: "确定", "class": "btn btn-primary btn-xs", click: function () {
                                    $(this).dialog("close");
                                }
                            }]
                        });
                    } else {
                        //null为callback,false是是否回到第一页
                        window.close();
                    }
                },
                error: function (response, textStatus) {/*能够接收404,500等错误*/
                    $("#errorText").html(response.responseText);
                    $("#dialog-error").removeClass('hide').dialog({
                        modal: true,
                        width: 600,
                        title: "请求状态码：" + response.status,//404，500等
                        buttons: [{
                            text: "确定", "class": "btn btn-primary btn-xs", click: function () {
                                $(this).dialog("close");
                            }
                        }]
                    });
                }
            });
        }
    });
</script>
<div id="dialog-error" class="hide alert" title="提示">
    <p id="errorText">失败，请稍后再试，或与系统管理员联系。</p>
</div>
</body>

</html>
