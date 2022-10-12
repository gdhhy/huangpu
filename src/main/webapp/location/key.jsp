<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="/assets/js/bootbox.all.min.js"></script>
<div class="breadcrumbs" id="breadcrumbs">
    <script type="text/javascript">
        try {
            ace.settings.check('breadcrumbs', 'fixed')
        } catch (e) {
        }
        jQuery(function ($) {
            $('.btn').click(function () {
                $.ajax({
                    type: "POST",
                    url: "/assets/saveKey.jspa",
                    data: $('#keyForm').serialize(),
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",//http://www.cnblogs.com/yoyotl/p/5853206.html
                    cache: false,
                    success: function (response, textStatus) {
                        var result = JSON.parse(response);
                        bootbox.alert({message: result.message, className: 'rubberBand animated'});
                    },
                    error: function (response, textStatus) {/*能够接收404,500等错误*/
                        bootbox.alert({message: response.responseText});
                    }
                });
            });
        });
    </script>

    <ul class="breadcrumb">
        <li>
            <i class="icon-home home-icon"></i>
            <a href="#">首页 </a>
        </li>
        <li class="active">控制台</li>
    </ul><!-- .breadcrumb -->
</div>

<div class="page-content">
    <div class="page-header">
        <h1>
            参数设置
            <small>
                <i class="icon-double-angle-right"></i>

            </small>
        </h1>
    </div><!-- /.page-header -->

    <div class="row">
        <div class="col-xs-12">
            <form class="form-horizontal" role="form" id="keyForm">
                <!-- PAGE CONTENT BEGINS -->
                <div class="alert alert-block alert-success">
                    高德地图KEY设置，参见：
                    <strong class="green">
                        <a href="https://console.amap.com/dev/key/app" target="_blank"> https://console.amap.com/dev/key/app</a>
                    </strong>。
                </div>
                <!-- #section:elements.form -->
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="key1">JS API（展示）</label>

                    <div class="col-sm-9">
                        <input type="text" id="key1" name="key1" placeholder="key1" class="col-xs-10 col-sm-5" value='${key1}'/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="key2">Web服务（后台）</label>

                    <div class="col-sm-9">
                        <input type="text" id="key2" name="key2" placeholder="key2" class="col-xs-10 col-sm-5" value="${key2}"/>
                    </div>
                </div>

                <!-- PAGE CONTENT BEGINS -->
                <div class="alert alert-block alert-info small">
                    设置本行政区经纬度范围，超出范围的，不显示。

                </div>
                <!-- #section:elements.form -->
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="key1">经度最小值</label>

                    <div class="col-sm-9">
                        <input type="text" id="longitudeMin" name="longitudeMin" placeholder="经度最小值" class="col-xs-10 col-sm-5" value='${longitudeMin}'/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="key2">经度最大值</label>

                    <div class="col-sm-9">
                        <input type="text" id="longitudeMax" name="longitudeMax" placeholder="经度最大值" class="col-xs-10 col-sm-5" value="${longitudeMax}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="key1">纬度最小值</label>

                    <div class="col-sm-9">
                        <input type="text" id="latitudeMin" name="latitudeMin" placeholder="纬度最小值" class="col-xs-10 col-sm-5" value='${latitudeMin}'/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="key2">纬度最大值</label>

                    <div class="col-sm-9">
                        <input type="text" id="latitudeMax" name="latitudeMax" placeholder="纬度最大值" class="col-xs-10 col-sm-5" value="${latitudeMax}"/>
                    </div>
                </div>
                <div class="clearfix form-actions">
                    <div class="col-md-offset-3 col-md-9">
                        <button class="btn btn-info" type="button">
                            <i class="ace-icon fa fa-check bigger-110"></i>
                            保存
                        </button>
                    </div>
                </div>
            </form>

            <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
    </div><!-- /.row -->
</div>
<!-- /.page-content -->