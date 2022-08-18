<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.2.3/js/dataTables.buttons.min.js"></script>
<script src="http://ace.jeka.by/assets/js/jquery.dataTables.bootstrap.min.js"></script>
<script src="https://cdn.datatables.net/select/1.4.0/js/dataTables.select.min.js"></script>
<script src="/assets/js/bootbox.all.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.3.0/js/dataTables.responsive.min.js"></script>
<script src="../js/jquery-validation-messages_zh.js"></script>
<script src="../js/resize.js"></script>
<script src="../assets/js/x-editable/bootstrap-editable.min.js"></script>
<!-- page specific plugin scripts -->

<link href="https://cdn.datatables.net/1.12.1/css/jquery.dataTables.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.2.3/css/buttons.dataTables.min.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/select/1.4.0/css/select.dataTables.min.css"/>
<link rel="stylesheet" href="../components/font-awesome-4.7.0/css/font-awesome.min.css"/>
<link href="https://cdn.datatables.net/responsive/2.3.0/css/responsive.dataTables.min.css" rel="stylesheet">
<link rel="stylesheet" href="../assets/css/bootstrap-editable.css"/>
<%--<script src="../components/dropzone/dist/dropzone.js"></script>
<link rel="stylesheet" href="../components/dropzone/dist/dropzone.css"/>--%>
<script src="https://unpkg.com/dropzone@5/dist/min/dropzone.min.js"></script>
<link rel="stylesheet" href="https://unpkg.com/dropzone@5/dist/min/dropzone.min.css" type="text/css"/>
<style>
    table.dataTable tbody tr.deletedClass {
        background-color: #d7d5d5;
    }

    table.dataTable tbody tr.myodd {
        background-color: #bce8f1;
    }</style>
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

    function showPic2(e, sUrl) {
        var x, y;
        x = e.clientX;
        y = e.clientY;
        document.getElementById("Layer1").style.left = x + 2 + 'px';
        document.getElementById("Layer1").style.top = y + 2 + 'px';
        document.getElementById("Layer1").innerHTML = "<img border='0' style='object-fit: cover;width:300px' src=\"" + sUrl + "\">";
        document.getElementById("Layer1").style.display = "";
    }

    function hiddenPic() {
        document.getElementById("Layer1").innerHTML = "";
        document.getElementById("Layer1").style.display = "none";
    }

    Dropzone.autoDiscover = false;

    var editor;
    jQuery(function ($) {
        var myTable = $('#dynamic-table')
            //.wrap("<div class='dataTables_borderWrap' />") //if you are applying horizontal scrolling (sScrollX)
            .DataTable({
                bAutoWidth: false,
                "columns": [
                    {"data": "assetsID", "sClass": "center"},
                    {"data": "name", "sClass": "center", defaultContent: ''},
                    {"data": "address", "sClass": "center"},
                    {"data": "longitude", "sClass": "center"},
                    {"data": "imageUrl", "sClass": "center", defaultContent: ''},//4
                    {"data": "owner", "sClass": "center"},
                    {"data": "street", "sClass": "center"},
                    {"data": "link", "sClass": "center"},
                    {"data": "status", "sClass": "center"},
                    {"data": "color", "sClass": "center"},//9
                    {"data": "assetsID", "sClass": "center"}
                ],

                'columnDefs': [
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 0},
                    {"orderable": false, className: 'text-center', "targets": 1, title: '位置'},//,width: 40
                    {"orderable": false, className: 'text-center', "targets": 2, title: '地址（百度坐标）'},
                    {
                        "orderable": false, className: 'text-center', "targets": 3, title: '经纬度（高德坐标）', width: 120, render: function (data, type, row, meta) {
                            if (data > 113.607677 || data < 113.398067 || row.latitude > 23.41208 || row.latitude < 23.030213) {
                                return "<span style='color:red;font-weight: bold'>{0},{1}</span>".format(data, row.latitude);
                            } else
                                return "<a href=\"\" style=' color:saddlebrown' onmouseout=\"hiddenPic();\" onmousemove=\"showPic(event,'https://restapi.amap.com/v3/staticmap?scale=2&markers=mid,0xFF0000,:{0},{1}&key=b772bf606b75644e7c2f3dcda3639896&radius&size=300*200');\">{2},{3}</a>".format(data, row.latitude, data, row.latitude);
                        }
                    },
                    {
                        "orderable": false, "searchable": false, className: 'text-center', "targets": 4, title: '图片', width: 40,
                        render: function (data, type, row, meta) {
                            if (data !== '' && data !== null) {
                                return "<a href=\"\" onmouseout=\"hiddenPic();\" onmousemove=\"showPic2(event,'/upload/{0}');\">".format(data) +
                                    '<i class="ace-icon glyphicon glyphicon-picture purple bigger-120"></i></a>';
                            }
                            return "";
                        }
                    },
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 5, title: '单位名称'},
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 6, title: '街道', width: 60},
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 7, title: '联系人', width: 60},
                    {
                        "orderable": false, "searchable": false, className: 'text-center', "targets": 8, title: '状态', width: 40, render: function (data, type, row, meta) {
                            let statusColor="black";
                            if(data==="未测评" || data==="停用") statusColor="silver";
                            return '<a href="#" data-pk="{0}" id="Status" data-value="{1}" data-type="select" style="color:{2};" class="editable" data-url="/assets/setAssets.jspa">{3}</a>'
                                .format(row["assetsID"], data, statusColor,data === null ? "空" : data);
                        }
                    },
                    {
                        "orderable": false, "searchable": false, className: 'text-center', "targets": 9, title: '标色', width: 30,
                        render: function (data, type, row, meta) {
                            return '<select id="skin-colorpicker" class="hide" data-id="{0}" data-color="{1}">'.format(row["assetsID"], data) +
                                '<option data-skin="no-skin" value="darkgray">darkgray</option>' +
                                '<option data-skin="skin-1" value="red">red</option>' +
                                '<option data-skin="skin-2" value="orange">orange</option>' +
                                '<option data-skin="skin-3" value="green">green</option> ' +
                                '<option data-skin="skin-4" value="purple">purple</option> ' +
                                '</select>';
                        }
                    },
                    {
                        "orderable": false, 'searchable': false, 'targets': 10, title: '操作', width: 80,
                        render: function (data, type, row, meta) {
                            let deleteHtml = row["deleted"] === 0 ? '<a class="hasLink" title="删除" href="#" data-Url="javascript:deleteAssets(\'{0}\',{1},\'{2}\',\'{3}\',\'{4}\');">'
                                    .format($('#selectedAssetsType').val(), row["assetsID"], row["name"], row["address"], $('#assets option:selected').text()) +
                                '<i class="ace-icon glyphicon glyphicon-trash brown bigger-120"></i></a>' : "";

                            return '<div class="hidden-sm hidden-xs action-buttons">' +
                                '<a class="hasLink" title="编辑" href="#" data-Url="javascript:editAssets(\'{0}\',{1});">'.format($('#selectedAssetsType').val(), row["assetsID"]) +
                                '<i class="ace-icon glyphicon glyphicon-edit green bigger-120"></i>' +
                                '</a> ' +
                                '<a class="hasLink" title="扩展信息" href="#" data-Url="javascript:showExpandDialog(\'{0}\',{1},\'{2}\',);">'.format($('#selectedAssetsType').val(), row["assetsID"], row["name"]) +
                                '<i class="ace-icon glyphicon  glyphicon-equalizer maroon bigger-120"></i>' +
                                '</a> ' +
                                deleteHtml +
                                '</div>';
                        }
                    }

                ],
                "createdRow": function (row, data, dataIndex) {
                    if (data["deleted"] === 1)
                        $(row).addClass('deletedClass');
                },
                "aLengthMenu": [[15, 100], ["15", "100"]],//二组数组，第一组数量，第二组说明文字;
                "aaSorting": [],//"aaSorting": [[ 4, "desc" ]],//设置第5个元素为默认排序
                language: {
                    url: '../components/datatables/datatables.chinese.json'
                },
                searching: false,
                "ajax": {
                    url: "/assets/listAssets.jspa?assetsType=led",
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
        new $.fn.dataTable.Buttons(myTable, {
            buttons: [
                {
                    "text": "<i class='ace-icon glyphicon glyphicon-plus red bigger-120'></i>&nbsp;&nbsp;新增",
                    "className": "btn btn-xs btn-white btn-primary "
                }
            ]
        });
        myTable.buttons().container().appendTo($('.tableTools-container'));
        myTable.button(0).action(function (e, dt, button, config) {
            e.preventDefault();
            showAssetsDialog({assetsID: 0, imageID: 0, assetsType: $('#selectedAssetsType').val()});
        });
        myTable.on('order.dt search.dt', function () {
            myTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });
        var options = [{value: "停用", text: '停用'}, {value: "在用", text: '在用'}];
        myTable.on('draw', function () {
            $('#dynamic-table tr').find('a:eq(1)').click(function () {
                $.cookie("data-assetsID", $(this).attr("data-assetsID"));
            });
            $('#dynamic-table tr').find('#skin-colorpicker').each(function () {
                $(this).val($(this).attr("data-color"));
            });
            $('#dynamic-table tr').find('#skin-colorpicker').ace_colorpicker().on('change', function () {
                var submitData = {assetsID: $(this).attr("data-id"), color: this.value, assets: $('#selectedAssetsType').val()};
                $.ajax({
                    type: "POST",
                    url: "/assets/saveColor.jspa",
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

            $("#dynamic-table tr").find(".editable").editable({
                mode: "popup",
                source: options,
                success: function (response, newValue) {
                    if (response.succeed !== 'false')
                        myTable.ajax.reload(null, false);//null为callback,false是是否回到第一页
                }
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
            // console.log($('#assets option:selected').val());
            $('#selectedAssetsType').val($('#assets option:selected').val());
            var url = "/assets/listAssets.jspa?assetsType={0}&showDeleted={1}".format($('#selectedAssetsType').val(), $('#showDeleted').is(':checked'));
            $('.form-search select').each(function () {
                if ($(this).val())
                    url += "&" + $(this).attr("name") + "=" + $(this).val();
            });
            $('.form-search :text').each(function () {
                if ($(this).val())
                    url += "&" + $(this).attr("name") + "=" + $(this).val();
            });
            if ($('#selectedAssetsType').val() === "secsys") {
                options = [{value: "未测评", text: '未测评'}, {value: "测评", text: '测评'}];
                $('input:radio[name=status]:eq(0)').val("未测评");
                $('input:radio[name=status]:eq(0)+span').text("未测评");
                $('input:radio[name=status]:eq(1)').val("测评");
                $('input:radio[name=status]:eq(1)+span').text("测评");
            } else {
                options = [{value: "停用", text: '停用'}, {value: "在用", text: '在用'}];
                $('input:radio[name=status]:eq(0)').val("停用");
                $('input:radio[name=status]:eq(0)+span').text("停用");
                $('input:radio[name=status]:eq(1)').val("在用");
                $('input:radio[name=status]:eq(1)+span').text("在用");
            }

            myTable.ajax.url(encodeURI(url)).load();
        }

        var ledForm = $('#ledForm');
        var saveUrl = "/assets/saveAssets.jspa";
        var validator = ledForm.validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            ignore: "",
            rules: {
                //name: {required: true},
                latitude: {max: 23.41208, min: 23.030213, required: true},
                longitude: {max: 113.607677, min: 113.398067, required: true}
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

        function editAssets(assetsType, assetsID) {
            $.getJSON("/assets/getAssets.jspa?assetsID=" + assetsID, function (ret) {
                saveUrl = "/assets/saveAssets.jspa";
                showAssetsDialog(ret);
            });
        }

        function deleteAssets(assetsType, assetsID, name, address, assetsText) {
            if (assetsID === undefined) return;
            $('#assetsText').text(assetsText);
            $('#locationForDelete').text(name);
            $('#addressForDelete').text(address);
            $("#dialog-delete").removeClass('hide').dialog({
                resizable: false,
                modal: true,
                title: "删除确认",
                //title_html: true,
                buttons: [
                    {
                        html: "<i class='ace-icon fa fa-trash bigger-110'></i>&nbsp;确定",
                        "class": "btn btn-danger btn-minier",
                        click: function () {
                            $.ajax({
                                type: "POST",
                                url: "/assets/deleteAssets.jspa?assets={0}&assetsID={1}".format(assetsType, assetsID),
                                //contentType: "application/x-www-form-urlencoded; charset=UTF-8",//http://www.cnblogs.com/yoyotl/p/5853206.html
                                cache: false,
                                success: function (response, textStatus) {
                                    var result = JSON.parse(response);
                                    if (result.succeed) {
                                        myTable.ajax.reload();
                                    } else
                                        bootbox.alert({message: "请求结果：" + result.succeed + "\n" + result.message});
                                    /*showDialog("请求结果：" + result.succeed, result.message);*/
                                    $('#dialog-delete').dialog('close');
                                },
                                error: function (response, textStatus) {/*能够接收404,500等错误*/
                                    //showDialog("请求状态码：" + response.status, response.responseText);
                                    console.log(response.responseText);
                                    bootbox.alert({
                                        message: response.responseText, callback: function () {
                                            $('#dialog-delete').dialog('close');
                                        }
                                    });
                                }
                            });

                        }
                    },
                    {
                        html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; 取消",
                        "class": "btn btn-minier",
                        click: function () {
                            $(this).dialog("close");
                        }
                    }
                ]
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
            let lastUrl = "https://restapi.amap.com/v3/staticmap?scale=2&size=200*200&key=b772bf606b75644e7c2f3dcda3639896&radius&markers="
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
                        $('#amap').html('位置标准名： <span style="color: #0288d1; ">{0}</span>'.format(ret.geocodes[0].formatted_address));
                        if (ret.geocodes[0].formatted_address.indexOf("广东省广州市黄埔区") === 0) {
                            var lnglat = ret.geocodes[0].location.split(",");
                            $('#longitude').val(lnglat[0]);
                            $('#latitude').val(lnglat[1]);

                            let markerparam = "large,{0},{1}:{2},{3}";
                            let lastUrl = "https://restapi.amap.com/v3/staticmap?scale=2&size=200*200&key=b772bf606b75644e7c2f3dcda3639896&radius&markers="
                            lastUrl += markerparam.format('0xE59866', '新', lnglat[0], lnglat[1]);
                            $('#img').attr("src", lastUrl);
                        } else {
                            $('#longitude').val(0);
                            $('#latitude').val(0);
                        }
                    }
                } else {
                    $('#amap').html('<div class="center red">查询位置失败！</div>');
                }
            });
        }

        $('#byName').on("click", function () {
            queryLnglat($('#name').val());
        });


        $('#byAddress').on("click", function () {
            queryLnglat($('#address').val());
        });

        function showAssetsDialog(loc) {
            validator.resetForm();//复位错误消息
            let title = (loc.assetsID === 0 ? "新增" : "编辑") + $('#assets option[value=' + $('#selectedAssetsType').val() + ']').text();
            if ($('#selectedAssetsType').val() === 'idc')
                $('#assetsName').text("机房名称");
            else
                $('#assetsName').text($('#assets option[value=' + $('#selectedAssetsType').val() + ']').text() + "名称");

            /*  if ($('#selectedAssetsType').val() === 'secsys') {
                  $('input:radio[name=status]:eq(0)').val("未测评");
                  $('input:radio[name=status]:eq(0)+span').text("未测评");
                  $('input:radio[name=status]:eq(1)').val("测评");
                  $('input:radio[name=status]:eq(1)+span').text("测评");
              } else {
                  $('input:radio[name=status]:eq(0)').val("停用");
                  $('input:radio[name=status]:eq(0)+span').text("停用");
                  $('input:radio[name=status]:eq(1)').val("在用");
                  $('input:radio[name=status]:eq(1)+span').text("在用");
              }*/

            $('input:radio[name=status]').filter('[value=' + loc.status + ']').prop('checked', true);

            $('#img').attr("src", "/components/jquery.easyui/themes/icons/blank.gif");
            //$('#img').attr("height", 350);
            $('#street').val(loc.street);
            $('#assetsType').val(loc.assetsType);
            $('#amap').html("纬度的小数点后第4位，每变化1，大约偏移11米。<br/>经度的小数点后第4位，每变化1，大约偏移9米。");
            $('#assetsID').val(loc.assetsID);
            $('#address').val(loc.address);
            $('#name').val(loc.name);
            $('#longitude').val(loc.longitude);
            $('#latitude').val(loc.latitude);
            $('#owner').val(loc.owner);
            $('#link').val(loc.link);
            $('#linkPhone').val(loc.linkPhone);
            $("#imageID").val(loc.imageID);
            $("#imageUrl").val(loc.imageUrl);
            runTimes = 0;
            markers = [];
            /* if (loc.name === null || loc.name === '') runTimes++;
             else*/
            p1(loc.name, "位", '0xFFFF00');
            p1(loc.address, "地", '0x00FF00');
            // "https://restapi.amap.com/v3/staticmap?markers=large,0xFF0000,%E4%BD%8D:116.37359,39.92437|large,0xFF0000,%E5%9C%B0:116.47359,39.92437&key=b772bf606b75644e7c2f3dcda3639896&radius");
            $("#dialog-edit").removeClass('hide').dialog({
                resizable: false, width: 860, height: 620, modal: true, title: title,
                //icon:'fa fa-key',
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
                title_html: true,
                close: function (event, ui) {
                    if (dz) {
                        dz.removeAllFiles(true);
                        dz.destroy();
                    }
                }
            });

            // var dz = $("div#dropzone").dropzone({
            var dz = new Dropzone("div#dropzone", {//https://blog.csdn.net/jinxhj2010/article/details/107683026
                url: '/upload/uploadImage.jspa',
                autoProcessQueue: true,// 如果为false，文件将被添加到队列中，但不会自动处理队列。
                uploadMultiple: false, // 是否在一个请求中发送多个文件。
                parallelUploads: 3, // 并行处理多少个文件上传
                maxFiles: 1, // 用于限制此Dropzone将处理的最大文件数
                maxFilesize: 20,
                acceptedFiles: ".jpg,.jpeg,.png",
                /*addRemoveLinks: true,
                 dictRemoveFile: '删除',*/
                dictDefaultMessage: "拖拉图片文件到这里或者点击",
                dictFallbackMessage: "你的浏览器不支持拖拉文件来上传",
                dictMaxFilesExceeded: "文件数量过多",
                dictFileTooBig: "可添加的最大文件大小为{{maxFilesize}}Mb，当前文件大小为{{filesize}}Mb ",
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
                },
                init: function () { // dropzone初始化时调用您可以在此处添加事件侦听器
                    let myDropzone = this;
                    myDropzone.removeAllFiles();//gzhhy发现的，负责会保留上一次加载的图片
                    this.on("addedfile", function (file) {
                        var removeButton = Dropzone.createElement("<button class='btn btn-sm btn-block'>移除</button>");
                        removeButton.addEventListener("click", function (e) {
                            e.preventDefault();
                            e.stopPropagation();
                            myDropzone.removeFile(file);
                            //todo 从服务器删除
                            $("#imageID").val(0);
                            $("#imageUrl").val('');
                        });
                        file.previewElement.appendChild(removeButton);
                    });

                    this.on("success", function (file, responseText) {
                        // Handle the responseText here. For example, add the text to the preview element:
                        $("#imageID").val(responseText.fileID);
                        $("#imageUrl").val(responseText.filename);
                    });

                    //https://github.com/dropzone/dropzone/wiki/FAQ
                    if (loc.imageID > 0) {
                        var url = "/upload/" + loc.imageUrl;

                        // If you only have access to the original image sizes on your server,
                        // and want to resize them in the browser:
                        var file = $.parseJSON(loc.imageJson);
                        //console.log(file.size);
                        let mockFile = {name: file.filename, size: file.size};//todo 读服务器
                        myDropzone.files.push(mockFile);//gzhhy发现的，负责会保留上一次加载的图片
                        myDropzone.displayExistingFile(mockFile, url);

                        // If you use the maxFiles option, make sure you adjust it to the
                        // correct amount:
                        let fileCountOnServer = 2; // The number of files already uploaded
                        myDropzone.options.maxFiles = 1;
                    }
                }
            });
        }

        $.fn.editable.defaults.mode = 'inline';

        let table;

        function showExpandDialog(assetsType, assetsID, assetsName) {
            $('#assetsName2').text(assetsName);
            let title = $('#assets option[value=' + $('#selectedAssetsType').val() + ']').text() + "扩展信息"
            if ($.fn.dataTable.isDataTable('#assets-expand-table')) {
                table = $('#assets-expand-table').DataTable();
            } else {
                table = $('#assets-expand-table').DataTable({
                    dom: "t", order: [[0, 'asc']], paging: false, searching: false,
                    columns: [{data: "expandID"}, {data: "key"}, {data: "value"}, {data: "expandID"}],
                    'columnDefs': [
                        {"orderable": true, "searchable": false, className: 'text-center', "targets": 0},
                        {
                            "orderable": false, "data": "key", className: 'text-center', title: '属性', "targets": 1, render: function (data, type, row, meta) {
                                return '<a href="#" data-pk="{0}" id="key" data-value="{1}" data-type="text" class="editable" data-url="/assets/saveExtKeyValue.jspa">{2}</a>'
                                    .format(assetsID + "-" + row["expandID"], data, data);
                            }
                        },
                        {
                            "orderable": false, "data": "value", className: 'text-center', "targets": 2, title: '值', render: function (data, type, row, meta) {
                                return '<a href="#" data-pk="{0}" id="value" data-value="{1}" data-type="text" class="editable" data-url="/assets/saveExtKeyValue.jspa">{2}</a>'
                                    .format(assetsID + "-" + row["expandID"], data, data);
                            }
                        },
                        {
                            "orderable": false, 'searchable': false, 'targets': 3, title: '操作',
                            render: function (data, type, row, meta) {
                                return '<div class="hidden-sm hidden-xs action-buttons">' +
                                    '<a class="hasLink" title="删除" href="#" data-Url="javascript:deleteAssetsExpand({0}, {1});">'.format(assetsID, data) +
                                    '<i class="ace-icon fa fa-trash brown bigger-120"></i></a>' +
                                    '</div>';
                            }
                        }
                    ],
                    select: {
                        style: 'os',
                        selector: 'td:first-child'
                    }
                });

            }
            table.ajax.url("/assets/getAssetsExpand.jspa?assetsID=" + assetsID).load();
            table.on('draw', function (e, setting) {
                $("#assets-expand-table tr").find(".editable").editable({
                    success: function (response, newValue) {
                        if (response.succeed !== 'false')
                            table.ajax.reload(null, false);//null为callback,false是是否回到第一页
                    }
                });
                $('#dialog-expand tr').find('.hasLink').click(function () {
                    if ($(this).attr("data-Url").indexOf('javascript:') >= 0) {
                        eval($(this).attr("data-Url"));
                    } else
                        window.open($(this).attr("data-Url"), "_blank");
                });
            });


            $("#dialog-expand").removeClass('hide').dialog({
                resizable: false, icon: 'fa fa-key', width: 500, height: 640, modal: true, title: title, title_html: true,
                close: function (event, ui) {
                    myTable.ajax.reload(null, false);//null为callback,false是是否回到第一页
                }
            });
        }

        function deleteAssetsExpand(assetsID, expandID) {
            bootbox.confirm({
                title: "删除资产扩展信息",
                message: "删除将不可恢复！",
                buttons: {
                    cancel: {
                        label: '<i class="fa fa-times"></i> 取消'
                    },
                    confirm: {
                        label: '<i class="fa fa-check"></i> 确认'
                    }
                },
                callback: function (result) {
                    // console.log('This was logged in the callback: ' + result);
                    if (result) //true or false
                        $.ajax({
                            type: "POST",
                            url: "/assets/deleteAssetsExpand.jspa?assetsID={0}&expandID={1}".format(assetsID, expandID),
                            //contentType: "application/x-www-form-urlencoded; charset=UTF-8",//http://www.cnblogs.com/yoyotl/p/5853206.html
                            cache: false,
                            success: function (response, textStatus) {
                                var result = JSON.parse(response);
                                if (result.succeed) {
                                    //bootbox.alert( result.message );
                                    table.ajax.reload();
                                } else
                                    bootbox.alert({message: "请求结果：" + result.succeed + "\n" + result.message});
                            },
                            error: function (response, textStatus) {/*能够接收404,500等错误*/
                                //showDialog("请求状态码：" + response.status, response.responseText);
                                //console.log(response.responseText);
                                bootbox.alert({message: response.responseText});
                            }
                        });
                }
            });
        }

        $('#addRow').on('click', function () {
            table.rows.add([{"expandID": table.page.info().recordsDisplay + 1, "key": "", "value": ""}]).draw();
        });
    });
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
    <%--   <div class="nav-search" id="nav-search">
        <form class="form-search">
  <span class="input-icon">
  <input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off"/>
  <i class="ace-icon fa fa-search nav-search-icon"></i>
  </span>
        </form>
    </div>--%><!-- /.nav-search -->

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
                    <option value="idc">IDC</option>
                    <option value="netbar">网吧</option>
                    <option value="secsys">等保系统</option>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;街道：
                <select id="byStreet" name="byStreet" class="nav-search-input">
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
                <input type="text" placeholder="地址 ..." name="address" autocomplete="off"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <label>显示删除 ：</label>
                <input type="checkbox" id="showDeleted">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
        删除 <span id="assetsText" class="brown"></span>： <span id="locationForDelete" class="red"></span> ！<br/>
        地址：<span id="addressForDelete" class="black"></span>
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
                    <label class="col-xs-2 control-label no-padding-right" for="name" id="assetsName">名称</label>
                    <div class="col-xs-8">
                        <input type="text" id="name" name="name" style="width: 100%" placeholder="名称"/>
                    </div>
                    <div class="col-xs-1">
                        <button type="button" class="btn btn-info btn-minier" id="byName" title="名称 -> 经纬度">
                            <i class="ace-icon fa fa-map-pin icon-on-right bigger-110"></i>
                        </button>
                    </div>
                </div>
            </div>
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
                    <div class="col-xs-4">
                        <input type="text" id="link" name="link" style="width: 100%" placeholder="联系人"/>
                    </div>
                    <label class="col-xs-2 control-label no-padding-right" for="linkPhone">电话</label>
                    <div class="col-xs-4">
                        <input type="text" id="linkPhone" name="linkPhone" style="width: 100%" placeholder="电话"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="address"> 地址</label>
                    <div class="col-xs-8">
                        <input type="text" id="address" name="address" style="width: 100%" placeholder="地址"/>
                    </div>
                    <div class="col-xs-1 pull-left">
                        <button type="button" class="btn btn-info btn-minier" id="byAddress" title="地址 -> 经纬度">
                            <i class="ace-icon fa fa-map-pin icon-on-right bigger-110"></i>
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
                    <label class="col-xs-2 control-label no-padding-right" for="latitude"> 纬度 </label>
                    <div class="col-xs-4">
                        <input type="text" id="latitude" name="latitude" style="width: 100%" placeholder="纬度"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="street"> 街道</label>
                    <div class="col-xs-4">
                        <select id="street" name="street" class="nav-search-input">
                            <option value="黄埔区" selected>不确定</option>
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
                    </div>
                    <label class="col-xs-2 control-label no-padding-right" for="longitude"> 状态 </label>
                    <div class="radio col-xs-4">
                        <label>
                            <input name="status" type="radio" class="ace" value="停用"/>
                            <span class="lbl">停用</span>
                        </label>
                        <label>
                            <input name="status" type="radio" class="ace" value="在用" checked/>
                            <span class="lbl">在用</span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="dropzone">图片</label>
                    <div class="col-xs-9 dropzone well-sm" id="dropzone" style="margin: 0 10px 0 10px">
                        <div class="am-text-success dz-message">
                            将文件拖拽到此处<br>或点此打开文件管理器选择文件
                        </div>
                    </div>
                </div>
            </div>
            <input type="hidden" id="imageID" name="imageID">
            <input type="hidden" id="imageUrl" name="imageUrl">
            <input type="hidden" id="assetsType" name="assetsType">
            <input type="hidden" id="assetsID" name="assetsID">
        </form>
    </div>

    <div class="col-xs-6">
        <label class="control-label no-padding-right" for="address"> 位置参考：红色是已保存的位置</label>
        <div id="container" class="map"><span class="border"><img id="img"/></span></div>
        <label class="control-label no-padding-right" style="height: 20px" for="address" id="amap"></label>
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
<input type="hidden" id="selectedAssetsType" value="led">
<div id="dialog-expand" class="hide">

    <div class="row" style="width: 470px;">
        <label class="col-xs-10 control-label no-padding-right" for="name" id="assetsName2"></label>
        <button id="addRow" class="col-xs-2 pull-right">添加</button>
        <div class="col-xs-12" style="padding: 0 0 0 0;margin: 0 0 0 0;font-weight: bold">
            <table id="assets-expand-table" class="display" cellspacing="0">
                <thead>
                <tr>
                    <th style="width: 10px"></th>
                    <th style="width: 150px">属性</th>
                    <th style="width: 100px">值</th>
                    <th style="width: 20px">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>