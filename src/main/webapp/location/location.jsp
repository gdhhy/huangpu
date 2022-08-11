<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.2.3/js/dataTables.buttons.min.js"></script>
<script src="http://ace.jeka.by/assets/js/jquery.dataTables.bootstrap.min.js"></script>
<script src="https://cdn.datatables.net/select/1.4.0/js/dataTables.select.min.js"></script>
<script src="http://bootboxjs.com/assets/js/bootbox.all.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.3.0/js/dataTables.responsive.min.js"></script>
<script src="../js/jquery-validation-messages_zh.js"></script>
<script src="../js/resize.js"></script>
<!-- page specific plugin scripts -->

<link href="https://cdn.datatables.net/1.12.1/css/jquery.dataTables.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.2.3/css/buttons.dataTables.min.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/select/1.4.0/css/select.dataTables.min.css"/>
<link href="https://cdn.bootcdn.net/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">

<link href="https://cdn.datatables.net/responsive/2.3.0/css/responsive.dataTables.min.css" rel="stylesheet">
<%--<script src="../components/dropzone/dist/dropzone.js"></script>
<link rel="stylesheet" href="../components/dropzone/dist/dropzone.css"/>--%>
<script src="https://unpkg.com/dropzone@5/dist/min/dropzone.min.js"></script>
<link rel="stylesheet" href="https://unpkg.com/dropzone@5/dist/min/dropzone.min.css" type="text/css"/>
<script type="text/javascript">
    function showPic(e, sUrl) {
        var x, y;
        x = e.clientX;
        y = e.clientY;
        document.getElementById("Layer1").style.left = x + 2 + 'px';
        document.getElementById("Layer1").style.top = y + 2 + 'px';
        document.getElementById("Layer1").innerHTML = "<img border='0' src=\"" + sUrl + "\">";
        document.getElementById("Layer1").style.display = "";
    }

    function hiddenPic() {
        document.getElementById("Layer1").innerHTML = "";
        document.getElementById("Layer1").style.display = "none";
    }

    Dropzone.autoDiscover = false;

    jQuery(function ($) {
        var myTable = $('#dynamic-table')
            //.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
            .DataTable({
                bAutoWidth: false,
                "columns": [
                    {"data": "locationID", "sClass": "center"},
                    {"data": "location", "sClass": "center", defaultContent: ''},
                    {"data": "address", "sClass": "center"},
                    {"data": "longitude", "sClass": "center"},
                    {"data": "owner", "sClass": "center"},//4
                    {"data": "street", "sClass": "center"},
                    {"data": "link", "sClass": "center"},
                    {"data": "color", "sClass": "center"},
                    {"data": "locationID", "sClass": "center"}//8
                ],

                'columnDefs': [
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 0},
                    {"orderable": false, className: 'text-center', "targets": 1, title: '位置'},
                    {"orderable": false, className: 'text-center', "targets": 2, title: '地址（百度坐标）'},
                    {
                        "orderable": false, className: 'text-center', "targets": 3, title: '经纬度（高德坐标）', render: function (data, type, row, meta) {
                            if (data > 113.607677 || data < 113.398067 || row.latitude > 23.41208 || row.latitude < 23.030213) {
                                return "<span style='color:red'>{0},{1}</span>".format(data, row.latitude);
                            } else
                                return "<a href=\"\" onmouseout=\"hiddenPic();\" onmousemove=\"showPic(event,'https://restapi.amap.com/v3/staticmap?markers=mid,0xFF0000,:{0},{1}&key=b772bf606b75644e7c2f3dcda3639896&radius&size=300*200');\">{2},{3}</a>".format(data, row.latitude, data, row.latitude);
                        }
                    },
                    /*  {
                          "orderable": false, 'targets': 4, 'searchable': false, title: '维度', render: function (data, type, row, meta) {
                              if (data > 23.41208 || data < 23.030213) {
                                  return "<span style='color:red'>{0}</span>".format(data);
                              } else return data;
                          }
                      },*/
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 4, title: '权属单位'},
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 5, title: '辖区', width: 70},
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 6, title: '联系人'},
                    {
                        "orderable": false, "searchable": false, className: 'text-center', "targets": 7, title: '状态',
                        render: function (data, type, row, meta) {
                            return '<select id="skin-colorpicker"  class="hide" data-id="{0}" data-color="{1}">'.format(row["locationID"], data) +
                                '<option data-skin="no-skin" value="#438EB9">#438EB9</option>' +
                                '<option data-skin="skin-1" value="red">red</option>' +
                                '<option data-skin="skin-2" value="#C6487E">#C6487E</option>' +
                                '<option data-skin="skin-3" value="green">green</option> ' +
                                '</select>';
                        }
                    },
                    {
                        "orderable": false, 'searchable': false, 'targets': 8, title: '操作', width: 140,
                        render: function (data, type, row, meta) {
                            return '<div class="hidden-sm hidden-xs action-buttons">' +
                                '<a class="hasLink" title="新增" href="#" data-Url="javascript:addAssets(\'{0}\',{1});">'.format($('#assets option:selected').val(), row["locationID"]) +
                                '<i class="ace-icon glyphicon glyphicon-plus red bigger-120"></i>' +
                                '</a> ' +
                                '<a class="hasLink" title="上传图片" href="#" data-Url="javascript:uploadImage(\'{0}\',{1});">'.format($('#assets option:selected').val(), row["locationID"]) +
                                '<i class="ace-icon glyphicon glyphicon-picture purple bigger-120"></i>' +
                                '</a> ' +
                                /*    '<a class="hasLink" title="设置经纬度" href="#" data-Url="javascript:longLatAssets(\'{0}\',{1});">'.format($('#assets option:selected').val(), row["locationID"]) +
                                    '<i class="ace-icon glyphicon glyphicon-map-marker blue bigger-120"></i>' +
                                    '</a> ' +*/
                                '<a class="hasLink" title="编辑" href="#" data-Url="javascript:longLatAssets(\'{0}\',{1});">'.format($('#assets option:selected').val(), row["locationID"]) +
                                '<i class="ace-icon glyphicon glyphicon-edit green bigger-120"></i>' +
                                '</a> ' +
                                '<a class="hasLink" title="删除" href="#" data-Url="javascript:deleteLed({0},\'{1}\');">'.format(row["locationID"], row["filename"]) +
                                '<i class="ace-icon glyphicon glyphicon-trash bigger-120"></i>' +
                                '</a>' +
                                '</div>';
                        }
                    }

                ],
                "aLengthMenu": [[15, 100], ["15", "100"]],//二组数组，第一组数量，第二组说明文字;
                "aaSorting": [],//"aaSorting": [[ 4, "desc" ]],//设置第5个元素为默认排序
                language: {
                    url: '../components/datatables/datatables.chinese.json'
                },
                searching: false,
                "ajax": {
                    url: "/location/listAssets.jspa",
                    "data": function (d) {//删除多余请求参数
                        for (var key in d)
                            if (key.indexOf("columns") === 0 || key.indexOf("order") === 0 || key.indexOf("search") === 0) //以columns开头的参数删除
                                delete d[key];
                    }
                },
                "processing": true,
                "serverSide": true,
                select: {style: 'single'}
            });

        myTable.on('order.dt search.dt', function () {
            myTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });
        myTable.on('draw', function () {
            $('#dynamic-table tr').find('a:eq(1)').click(function () {
                $.cookie("data-locationID", $(this).attr("data-locationID"));
            });
            $('#dynamic-table tr').find('#skin-colorpicker').each(function () {
                $(this).val($(this).attr("data-color"))
            });
            $('#dynamic-table tr').find('#skin-colorpicker').ace_colorpicker().on('change', function () {
                var submitData = {locationID: $(this).attr("data-id"), color: this.value};
                $.ajax({
                    type: "POST",
                    url: "/location/saveLedJson.jspa",
                    data: JSON.stringify(submitData),
                    //contentType: "application/x-www-form-urlencoded; charset=UTF-8",//http://www.cnblogs.com/yoyotl/p/5853206.html
                    contentType: "application/json; charset=utf-8",
                    cache: false,
                    success: function (response, textStatus) {
                        var result = JSON.parse(response);
                        if (!result.succeed) {
                            $("#errorText").html(result.errmsg);
                            $("#dialog-error").removeClass('hide').dialog({
                                modal: true,
                                width: 600,
                                title: result.title,
                                buttons: [{
                                    text: "确定", "class": "btn btn-primary btn-xs", click: function () {
                                        $(this).dialog("close");
                                        myTable.ajax.reload(null, false);//null为callback,false是是否回到第一页
                                    }
                                }]
                            });
                        } else {
                            myTable.ajax.reload(null, false);//null为callback,false是是否回到第一页
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
            });
            $('#dynamic-table tr').find('.hasLink').click(function () {
                if ($(this).attr("data-Url").indexOf('javascript:') >= 0) {
                    eval($(this).attr("data-Url"));
                } else
                    window.open($(this).attr("data-Url"), "_blank");
            });
        });
        $('.btn-success').click(function () {
            search();
        });

        $('.form-search :text').keydown(function (event) {
            if (event.keyCode === 13) return;
            //search();
        });

        function search() {
            var url = "/location/listAssets.jspa";
            // var searchParam = "?threeThirty=" + $('#three_thirty').is(':checked');
            var searchParam = "";
            $('.form-search select').each(function () {
                if ($(this).val())
                    searchParam += "&" + $(this).attr("name") + "=" + $(this).val();
            });
            $('.form-search :text').each(function () {
                if ($(this).val())
                    searchParam += "&" + $(this).attr("name") + "=" + $(this).val();
            });
            if (searchParam !== "")
                url = "/location/listAssets.jspa" + searchParam.replace('&', '?');//只替换第一个
            myTable.ajax.url(encodeURI(url)).load();
        }


        var ledForm = $('#ledForm');
        var saveUrl = "/location/saveLed.jspa";
        ledForm.validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            ignore: "",
            rules: {
                latitude: {max: 23.41208, min: 23.030213},
                longitude: {max: 113.607677, min: 113.398067}
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },

            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
                $(e).remove();
            },

            errorPlacement: function (error, element) {
                error.insertAfter(element.parent());
            },

            submitHandler: function (form) {
                $.ajax({
                    type: "POST",
                    url: saveUrl,
                    data: ledForm.serialize(),
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",//http://www.cnblogs.com/yoyotl/p/5853206.html
                    cache: false,
                    success: function (response, textStatus) {
                        var result = JSON.parse(response);
                        if (!result.succeed) {
                            bootbox.alert({
                                message: result.errmsg,
                                callback: function () {
                                    $("#dialog-edit").dialog("close");
                                }
                            });
                        } else {
                            myTable.ajax.reload(null, false);//null为callback,false是是否回到第一页
                            $("#dialog-edit").dialog("close");
                        }
                    },
                    error: function (response, textStatus) {/*能够接收404,500等错误*/
                        bootbox.alert({
                            message: response.responseText,
                            callback: function () {
                                $("#dialog-edit").dialog("close");
                            }
                        });
                    }
                });
            },
            invalidHandler: function (form) {
                console.log("invalidHandler");
            }
        });

        function longLatAssets(assetsType, locationID) {
            if (assetsType === 'led')
                $.getJSON("/location/getLed.jspa?locationID=" + locationID, function (ret) {
                    saveUrl = "/location/saveLed.jspa";
                    showLedDialog(ret);
                });
            else
                $.getJSON("/location/getServer.jspa?locationID=" + locationID, function (ret) {
                    saveUrl = "/location/saveServer.jspa";
                    showLedDialog(ret);
                });
        }

        var markers = [];
        var url = 'https://restapi.amap.com/v3/geocode/geo?address={0}&output=json&key=b772bf606b75644e7c2f3dcda3639896';
        var reg = /\d{2,}\.\d+/;

        function p1(addr, chinese, color) {
            ///console.log("addr:" + addr);
            ///console.log("reg.test(addr):" + reg.test(addr));
            if (addr && !reg.test(addr)) {
                if (addr.length > 2 && addr.indexOf("黄埔") < 0) //&&
                    addr = "黄埔区" + addr;
                $.getJSON(url.format(addr), function (ret) {
                    if (ret.status === "1" && ret.count === "1") {
                        if (ret.geocodes[0].formatted_address.indexOf("广东省广州市黄埔区") === 0) {
                            markers.push({label: chinese, color: color, lnglat: ret.geocodes[0].location.split(",")});
                        }
                    }
                    runTwice();
                });
            } else runTimes++;
        }

        var runTimes = 0;

//https://restapi.amap.com/v3/staticmap?markers=large,0xFF0000,%E4%BD%8D:116.37359,39.92437|large,0xFF0000,%E5%9C%B0:116.47359,39.92437&key=b772bf606b75644e7c2f3dcda3639896&radius
        function runTwice() {
            runTimes++;
            let markerparam = "large,{0},{1}:{2},{3}";
            let lastUrl = "https://restapi.amap.com/v3/staticmap?key=b772bf606b75644e7c2f3dcda3639896&radius&markers="
            if (runTimes === 2) {
                if ($('#longitude').val() > 0 && $('#latitude').val() > 0) {
                    markers.push({label: "", color: '0xFF0000', lnglat: [$('#longitude').val(), $('#latitude').val()]});
                }

                for (let k = markers.length - 1; k >= 0; k--) {
                    var repeat = 0;
                    for (let j = k - 1; j >= 0; j--) {
                        if (markers[k].lnglat[0] === markers[j].lnglat[0] && markers[k].lnglat[1] === markers[j].lnglat[1])
                            repeat = 1;
                    }
                    if (repeat === 0) {
                        lastUrl += markerparam.format(markers[k].color, markers[k].label, markers[k].lnglat[0], markers[k].lnglat[1]) + "|";
                    }
                }
                //console.log("lastUrl:" + lastUrl.substring(0, lastUrl.length - 1));
                $('#img').attr("src", lastUrl.substring(0, lastUrl.length - 1));
            }
        }

        function queryLnglat(addr) {
            $('#amap').html("");
            if (reg.test(addr)) {
                $('#amap').html('<div class="center red">不能用经纬度查询</div>');
                return;
            }
            if (addr.length > 2 && addr.indexOf("黄埔") < 0) //&&
                addr = "黄埔区" + addr;
            //https://restapi.amap.com/v3/staticmap?markers=large,0xFF0000,%E4%BD%8D:116.37359,39.92437|large,0xFF0000,%E5%9C%B0:116.47359,39.92437&key=b772bf606b75644e7c2f3dcda3639896&radius
            $.getJSON(url.format(addr), function (ret) {
                if (ret.status === "1") {
                    if (ret.geocodes.length === 1) {
                        $('#amap').html('位置标准名： <span   style="color: #0288d1; ">{0}</span>'.format(ret.geocodes[0].formatted_address));
                        if (ret.geocodes[0].formatted_address.indexOf("广东省广州市黄埔区") === 0) {
                            var lnglat = ret.geocodes[0].location.split(",");
                            $('#longitude').val(lnglat[0]);
                            $('#latitude').val(lnglat[1]);

                            let markerparam = "large,{0},{1}:{2},{3}";
                            let lastUrl = "https://restapi.amap.com/v3/staticmap?key=b772bf606b75644e7c2f3dcda3639896&radius&markers="
                            lastUrl += markerparam.format('0xE59866', '新', lnglat[0], lnglat[1]);
                            $('#img').attr("src", lastUrl);
                        } else {
                            $('#longitude').val(0);
                            $('#latitude').val(0);
                        }
                    }
                } else {
                    $('#amap').html('<div class="center red">查询失败！</div>');
                }
            });
        }

        $('#byLocation').on("click", function () {
            queryLnglat($('#location').val());
        });

        $('#byAddress').on("click", function () {
            queryLnglat($('#address').val());
        });

        function showLedDialog(loc) {
            /*  var map = new AMap.Map("container", {
                  resizeEnable: true
              });
              //为地图注册click事件获取鼠标点击出的经纬度坐标
              map.on('click', function (e) {
                  // document.getElementById("lnglat").value = e.lnglat.getLng() + ',' + e.lnglat.getLat()
              });*/

            $('#amap').html("");
            $('#locationID').val(loc.locationID);
            $('#address').val(loc.address);
            $('#location').val(loc.location);
            $('#longitude').val(loc.longitude);
            $('#latitude').val(loc.latitude);
            $('#owner').val(loc.owner);
            $('#link').val(loc.link);
            runTimes = 0;
            markers = [];
            /* if (loc.location === null || loc.location === '') runTimes++;
             else*/
            p1(loc.location, "位", '0xFFFF00');
            p1(loc.address, "地", '0x00FF00');
            // "https://restapi.amap.com/v3/staticmap?markers=large,0xFF0000,%E4%BD%8D:116.37359,39.92437|large,0xFF0000,%E5%9C%B0:116.47359,39.92437&key=b772bf606b75644e7c2f3dcda3639896&radius");
            $("#dialog-edit").removeClass('hide').dialog({
                resizable: false,
                //icon:'fa fa-key',
                width: 860,
                height: 600,
                modal: true,
                title: "编辑资产信息",
                buttons: [
                    {
                        html: "<i class='ace-icon fa fa-floppy-o bigger-110'></i>&nbsp;保存",
                        "class": "btn btn-danger btn-minier",
                        click: function () {
                            if (ledForm.valid())
                                ledForm.submit();
                        }
                    }, {
                        html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp;关闭",
                        "class": "btn btn-minier",
                        click: function () {
                            $('#dialog-edit').dialog('close');
                        }
                    }],
                title_html: true
            });
        }

        $("#dropzone").dropzone({
            url: '/upload/uploadImage.jspa',thumbnail: function(file, dataUrl) {
                if (file.previewElement) {
                    $(file.previewElement).removeClass("dz-file-preview");
                    var images = $(file.previewElement).find("[data-dz-thumbnail]").each(function() {
                        var thumbnailElement = this;
                        thumbnailElement.alt = file.name;
                        thumbnailElement.src = dataUrl;
                    });
                    setTimeout(function() { $(file.previewElement).addClass("dz-image-preview"); }, 1);
                }
            },/*,
            autoProcessQueue: false,// 如果为false，文件将被添加到队列中，但不会自动处理队列。
            uploadMultiple: false, // 是否在一个请求中发送多个文件。
            parallelUploads: 1, // 并行处理多少个文件上传
            maxFiles: 1, // 用于限制此Dropzone将处理的最大文件数
            maxFilesize: 10,
            acceptedFiles: ".jpg,.gif,.png",
            dictDefaultMessage: "拖拉图片文件到这里或者点击",
            dictFallbackMessage: "你的浏览器不支持拖拉文件来上传",
            dictMaxFilesExceeded: "文件数量过多",
            dictFileTooBig: "可添加的最大文件大小为{{maxFilesize}}Mb，当前文件大小为{{filesize}}Mb "*/
        });


        /* let myDropzone = new Dropzone("#dropzone");
         myDropzone.on("addedfile", file => {
             console.log(`File added:啊水水`);
         });*/


        /*     var dropz = new Dropzone('#dropzone', {
                 url: '/upload/uploadImage.jspa',
                 autoProcessQueue: false,// 如果为false，文件将被添加到队列中，但不会自动处理队列。
                 uploadMultiple: true, // 是否在一个请求中发送多个文件。
                 parallelUploads: 3, // 并行处理多少个文件上传
                 maxFiles: 1, // 用于限制此Dropzone将处理的最大文件数
                 maxFilesize: 10,
                 acceptedFiles: ".jpg,.gif,.png",
                 dictDefaultMessage: "拖拉图片文件到这里或者点击",
                 dictFallbackMessage: "你的浏览器不支持拖拉文件来上传",
                 dictMaxFilesExceeded: "文件数量过多",
                 dictFileTooBig: "可添加的最大文件大小为{{maxFilesize}}Mb，当前文件大小为{{filesize}}Mb ",
                 init: function () { // dropzone初始化时调用您可以在此处添加事件侦听器
                     var myDropzone = this;
                     this.on("addedfile", function (file) {
                         /!* var removeButton = Dropzone.createElement("<button class='btn btn-sm btn-block'>移除</button>");
                          removeButton.addEventListener("click",function(e) {
                              e.preventDefault();
                              e.stopPropagation();
                              myDropzone.removeFile(file);
                          });
                          file.previewElement.appendChild(removeButton);*!/
                     });
                 },
                 sendingmultiple: function (file, xhr, formData) {// 在每个文件发送之前调用。获取xhr对象和formData对象作为第二和第三个参数，可以修改它们（例如添加CSRF令牌）或添加其他数据。
                     $.each(submitParams, function (key, value) {
                         // formData.set(key, value);
                     });
                 },
                 successmultiple: function (file, response) {// 该文件已成功上传。获取服务器响应作为第二个参数。
                 },
                 completemultiple: function (file, data) {
                 }
             });*/

        //ACE
        /* Dropzone.autoDiscover = false;

         var myDropzone = new Dropzone('#dropzone', {
             previewTemplate: $('#preview-template').html(),

             thumbnailHeight: 120,
             thumbnailWidth: 120,
             maxFilesize: 0.5,

             //addRemoveLinks : true,
             //dictRemoveFile: 'Remove',

             dictDefaultMessage:
                 '<span class="bigger-150 bolder"><i class="ace-icon fa fa-caret-right red"></i> Drop files</span> to upload \
                 <span class="smaller-80 grey">(or click)</span> <br /> \
                 <i class="upload-icon ace-icon fa fa-cloud-upload blue fa-3x"></i>'
             ,

             thumbnail: function (file, dataUrl) {
                 if (file.previewElement) {
                     $(file.previewElement).removeClass("dz-file-preview");
                     var images = $(file.previewElement).find("[data-dz-thumbnail]").each(function () {
                         var thumbnailElement = this;
                         thumbnailElement.alt = file.name;
                         thumbnailElement.src = dataUrl;
                     });
                     setTimeout(function () {
                         $(file.previewElement).addClass("dz-image-preview");
                     }, 1);
                 }
             }

         });


         //simulating upload progress
         var minSteps = 6,
             maxSteps = 60,
             timeBetweenSteps = 100,
             bytesPerStep = 100000;

         myDropzone.uploadFiles = function (files) {
             var self = this;

             for (var i = 0; i < files.length; i++) {
                 var file = files[i];
                 totalSteps = Math.round(Math.min(maxSteps, Math.max(minSteps, file.size / bytesPerStep)));

                 for (var step = 0; step < totalSteps; step++) {
                     var duration = timeBetweenSteps * (step + 1);
                     setTimeout(function (file, totalSteps, step) {
                         return function () {
                             file.upload = {
                                 progress: 100 * (step + 1) / totalSteps,
                                 total: file.size,
                                 bytesSent: (step + 1) * file.size / totalSteps
                             };

                             self.emit('uploadprogress', file, file.upload.progress, file.upload.bytesSent);
                             if (file.upload.progress == 100) {
                                 file.status = Dropzone.SUCCESS;
                                 self.emit("success", file, 'success', null);
                                 self.emit("complete", file);
                                 self.processQueue();
                             }
                         };
                     }(file, totalSteps, step), duration);
                 }
             }
         }


         //remove dropzone instance when leaving this page in ajax mode
         $(document).one('ajaxloadstart.page', function (e) {
             try {
                 myDropzone.destroy();
             } catch (e) {
             }
         });*/

    })
</script>
<!-- #section:basics/content.breadcrumbs -->
<div class="breadcrumbs ace-save-state" id="breadcrumbs">
    <ul class="breadcrumb">
        <li>
            <i class="ace-icon fa fa-home home-icon"></i>
            <a href="/index.jspa">首页</a>
        </li>
        <li class="active">资产管理</li>
    </ul><!-- /.breadcrumb -->

    <!-- #section:basics/content.searchbox -->
    <div class="nav-search" id="nav-search">
        <form class="form-search">
  <span class="input-icon">
  <input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off"/>
  <i class="ace-icon fa fa-search nav-search-icon"></i>
  </span>
        </form>
    </div><!-- /.nav-search -->

    <!-- /section:basics/content.searchbox -->
</div>
<!-- /section:basics/content.breadcrumbs -->
<div class="page-content">
    <div class="page-header">
        <ul class="breadcrumb">
            <form class="form-search form-inline">
                <label>资产类型 ：</label>
                <select id="assets" name="assets" class="nav-search-input">
                    <option value="led" selected>LED</option>
                    <option value="server">服务器</option>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;辖区：
                <select id="street" name="street" class="nav-search-input">
                    <option value="" selected>全部</option>
                    <option value="黄埔">黄埔</option>
                    <option value="长洲">长洲</option>
                    <option value="文冲">文冲</option>
                    <option value="永和">永和</option>
                    <option value="南岗">南岗</option>
                    <option value="红山">红山</option>
                    <option value="联和">联和</option>
                    <option value="云埔">云埔</option>
                    <option value="大沙">大沙</option>
                    <option value="萝岗">萝岗</option>
                    <option value="长岭">长岭</option>
                    <option value="新龙">新龙</option>
                    <option value="龙湖">龙湖</option>
                    <option value="鱼珠">九佛</option>
                    <option value="鱼珠">鱼珠</option>
                    <option value="夏港">夏港</option>
                    <option value="穗东">穗东</option>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <label>经纬度 ：</label>
                <select id="coordinate" name="coordinate" class="nav-search-input">
                    <option value="all" selected>全部</option>
                    <option value="fixed">已确定</option>
                    <option value="unfixed">未定</option>
                </select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <label>地址 ：</label>
                <input type="text" placeholder="地址 ..." name="address" autocomplete="off"/>
                <button type="button" class="btn btn-sm btn-success">
                    查询
                    <i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
                </button>
            </form>
        </ul>
    </div><!-- /.page-header -->
    <div class="row">
        <div class="col-sm-12">

            <div class="row">

                <div class="col-sm-12">
                    <div class="table-header">
                        资产列表
                        <div class="pull-right tableTools-container"></div>
                    </div>

                    <!-- div.table-responsive -->

                    <!-- div.dataTables_borderWrap -->
                    <div id="dt">
                        <table id="dynamic-table" class="table table-striped table-bordered table-hover">
                        </table>
                    </div>
                </div>
            </div>
            <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
    </div><!-- /.row -->

</div>
<!-- /.page-content -->
<div id="dialog-delete" class="hide">
    <div class="alert alert-info bigger-110">
        删除上传文件： <span id="filename" class="red"></span> ，分析结果、索引、服务器上文件一起删除！
    </div>

    <div class="space-6"></div>

    <p class="bigger-110 bolder center grey">
        <i class="icon-hand-right blue bigger-120"></i>
        确认吗？
    </p>
</div>
<div id="dialog-index" class="hide">
    <div class="alert alert-info bigger-110">
        将对<br/> <span id="filename4" class="light-orange "></span> <br/>文件创建索引，索引成功后，在全文搜索能够快速查询。
    </div>

    <div class="space-6"></div>

    <p class="bigger-110 bolder center grey">
        <i class="icon-hand-right blue bigger-120"></i>
        确认吗？
    </p>
</div>

<div id="dialog-edit" class="hide">
    <div class="col-xs-6" style="padding-top: 10px">
        <!-- PAGE CONTENT BEGINS -->
        <form class="form-horizontal" role="form" id="ledForm">
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="latitude">单位名称</label>
                    <div class="col-xs-10">
                        <input type="text" id="owner" name="owner" style="width: 100%" placeholder="单位名称"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="link">联系人</label>
                    <div class="col-xs-10">
                        <input type="text" id="link" name="link" style="width: 100%" placeholder="联系人"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="location"> 位置</label>
                    <div class="col-xs-8">
                        <input type="text" id="location" name="location" style="width: 100%" placeholder="位置"/>
                    </div>
                    <div class="col-xs-1">
                        <button type="button" class="btn btn-info btn-minier" id="byLocation" title="位置 -> 经纬度">
                            <i class="ace-icon  fa fa-map-pin icon-on-right bigger-110"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="address"> 地址</label>
                    <div class="col-xs-8">
                        <input type="text" id="address" readonly name="address" style="width: 100%" placeholder="地址"/>
                    </div>
                    <div class="col-xs-1 pull-left">
                        <button type="button" class="btn btn-info btn-minier" id="byAddress" title="地址 -> 经纬度">
                            <i class="ace-icon  fa fa-map-pin icon-on-right bigger-110"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="longitude"> 经度 </label>
                    <div class="col-xs-4">
                        <input type="text" id="longitude" name="longitude" style="width: 100%" placeholder="经度"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="latitude"> 纬度 </label>
                    <div class="col-xs-4">
                        <input type="text" id="latitude" name="latitude" style="width: 100%" placeholder="纬度"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="dropzone">图片</label>
                    <div class="col-xs-9 dropzone " id="dropzone" style="margin: 10px 10px 10px 10px">
                        <div class="am-text-success dz-message">
                            将文件拖拽到此处<br>或点此打开文件管理器选择文件
                        </div>
                    </div>
                </div>
            </div>
            <input type="hidden" id="locationID" name="locationID">
        </form>
    </div>

    <div class="col-xs-6">
        <label class=" control-label no-padding-right" for="address"> 位置参考：红色是已保存的位置</label>
        <div id="container" class="map"><img id="img"/></div>
        <label class=" control-label no-padding-right " style="height: 20px" for="address" id="amap"> </label>
    </div>
</div>
<div id="dialog-error" class="hide alert" title="提示">
    <p id="errorText">失败，请稍后再试，或与系统管理员联系。</p>
</div>

<div class="modal fade" id="loadingModal">
    <div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
        <div class="progress progress-striped active" style="margin-bottom: 0;">
            <div class="progress-bar" style="width: 100%;" id="loadingText">正在抽取……</div>
        </div>
    </div>
</div>
<div id="Layer1" style="display: none; position: absolute; z-index: 100;">
</div>