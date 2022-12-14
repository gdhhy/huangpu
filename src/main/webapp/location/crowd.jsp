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
<style>
    table.dataTable tbody tr.deletedClass {
        background-color: #d7d5d5;
    }

    table.dataTable tbody tr.selected {
        color: white !important;
        background-color: #eeeeee !important;
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

    var editor;
    jQuery(function ($) {
        var myTable = $('#dynamic-table')
            //.wrap("<div class='dataTables_borderWrap' />") //if you are applying horizontal scrolling (sScrollX)
            .DataTable({
                bAutoWidth: false,
                "columns": [
                    {"data": "crowdID", "sClass": "center"},
                    {"data": "patient", "sClass": "center"},
                    {"data": "location", "sClass": "center", defaultContent: ''},
                    {"data": "address", "sClass": "center"},
                    {"data": "longitude", "sClass": "center"},
                    {"data": "stayTime", "sClass": "center"},
                    {"data": "street", "sClass": "center"},
                    {"data": "highRisk", "sClass": "center"},
                    {"data": "status", "sClass": "center"},
                    {"data": "crowdID", "sClass": "center"}
                ],

                'columnDefs': [
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 0},
                    {"orderable": false, className: 'text-center', "targets": 1, title: '??????', width: 60},//,width: 40
                    {"orderable": false, className: 'text-center', "targets": 2, title: '??????'},//,width: 40
                    {"orderable": false, className: 'text-center', "targets": 3, title: '????????????'},
                    {
                        "orderable": false, className: 'text-center', "targets": 4, title: '???????????????????????????', width: 120, render: function (data, type, row, meta) {
                            if (data > ${longitudeMax} || data < ${longitudeMin} || row.latitude > ${latitudeMax} || row.latitude < ${latitudeMin}) {
                                return "<span style='color:red;font-weight: bold'>{0},{1}</span>".format(data, row.latitude);
                            } else
                                return "<a href=\"\" style=' color:saddlebrown' onmouseout=\"hiddenPic();\" onmousemove=\"showPic(event,'https://restapi.amap.com/v3/staticmap?scale=2&markers=mid,0xFF0000,:{0},{1}&key=${key2}&radius&size=300*200');\">{2},{3}</a>".format(data, row.latitude, data, row.latitude);
                        }
                    },

                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 5, title: '????????????'},
                    {"orderable": false, "searchable": false, className: 'text-center', "targets": 6, title: '??????', width: 40},
                    {
                        "orderable": false, "searchable": false, className: 'text-center', "targets": 7, title: '????????????', width: 200, render: function (data, type, row, meta) {
                            return "????????????{0}????????????{1}<br/>????????????{2}????????????{3}".format(data, row["knit"], row["subknit"], row["important"]);

                        }
                    },
                    {
                        "orderable": false, "searchable": false, className: 'text-center', "targets": 8, title: '??????', width: 40, render: function (data, type, row, meta) {
                            let statusColor = "black";
                            if (data === "??????") statusColor = "silver";
                            return '<a href="#" data-pk="{0}" id="Status" data-value="{1}" data-type="select"  style="color:{2};" title="???????????????????????????" data-title="???????????????????????????" class="editable" data-url="/crowd/setCrowd.jspa">{3}</a>'
                                .format(row["crowdID"], data, statusColor, data === null ? "???" : data);
                        }
                    },
                    /* {
                         "orderable": false, "searchable": false, className: 'text-center', "targets": 9, title: '??????', width: 30,
                         render: function (data, type, row, meta) {
                             return '<select id="skin-colorpicker" class="hide" data-id="{0}" data-color="{1}">'.format(row["crowdID"], data) +
                                 '<option data-skin="no-skin" value="darkgray">darkgray</option>' +
                                 '<option data-skin="skin-1" value="red">red</option>' +
                                 '<option data-skin="skin-2" value="orange">orange</option>' +
                                 '<option data-skin="skin-3" value="green">green</option> ' +
                                 '<option data-skin="skin-4" value="purple">purple</option> ' +
                                 '</select>';
                         }
                     },*/
                    {
                        "orderable": false, 'searchable': false, 'targets': 9, title: '??????', width: 80,
                        render: function (data, type, row, meta) {
                            let deleteHtml = row["deleted"] === 0 ? '<a class="hasLink" title="??????" href="#" data-Url="javascript:deleteCrowd({0},\'{1}\',\'{2}\',\'{3}\');">'
                                    .format(row["crowdID"], row["location"], row["address"], $('#crowd option:selected').text()) +
                                '<i class="ace-icon glyphicon glyphicon-trash brown bigger-110"></i></a>' : "";

                            return '<div class="hidden-sm hidden-xs action-buttons">' +
                                '<a class="hasLink" title="??????" href="#" data-Url="javascript:editCrowd({0});">'.format(row["crowdID"]) +
                                '<i class="ace-icon glyphicon glyphicon-edit green bigger-120"></i>' +
                                '</a> ' +
                               /* '<a target="_blank" title="??????" href="/crowd/drap.jspa?crowdID={0}">'.format(row["crowdID"]) +
                                '<i class="ace-icon glyphicon glyphicon-map-marker blue bigger-120"></i>' +
                                '</a> ' +*/
                                '<a class="hasLink" title="???????????????" data-Url="javascript:drapWindow({0});">'.format(row["crowdID"]) +
                                '<i class="ace-icon glyphicon glyphicon-new-window purple bigger-110"></i>' +
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
                "aLengthMenu": [[15, 100], ["15", "100"]],//??????????????????????????????????????????????????????;
                "aaSorting": [],//"aaSorting": [[ 4, "desc" ]],//?????????5????????????????????????
                language: {
                    url: '../components/datatables/datatables.chinese.json'
                },
                searching: false,
                "ajax": {
                    url: "/crowd/listCrowd.jspa",
                    "data": function (d) {//????????????????????????
                        for (var key in d)
                            if (key.indexOf("columns") === 0 || key.indexOf("order") === 0 || key.indexOf("search") === 0) //???columns?????????????????????
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
                    "text": "<i class='ace-icon glyphicon glyphicon-plus red bigger-120'></i>&nbsp;&nbsp;??????",
                    "className": "btn btn-xs btn-white btn-primary "
                }
            ]
        });
        myTable.buttons().container().appendTo($('.tableTools-container'));
        myTable.button(0).action(function (e, dt, button, config) {
            e.preventDefault();
            showCrowdDialog({crowdID: 0, imageID: 0});
        });
        myTable.on('order.dt search.dt', function () {
            myTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });
        var options = [{value: "??????", text: '??????'}, {value: "??????", text: '??????'}];
        myTable.on('draw', function () {
            $('#dynamic-table tr').find('a:eq(1)').click(function () {
                $.cookie("data-crowdID", $(this).attr("data-crowdID"));
            });
            $('#dynamic-table tr').find('#skin-colorpicker').each(function () {
                $(this).val($(this).attr("data-color"));
            });
            $('#dynamic-table tr').find('#skin-colorpicker').ace_colorpicker().on('change', function () {
                var submitData = {objectID: $(this).attr("data-id"), color: this.value};
                $.ajax({
                    type: "POST",
                    url: "/crowd/saveColor.jspa",
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
                                    text: "??????", "class": "btn btn-primary btn-xs", click: function () {
                                        $(this).dialog("close");
                                        myTable.ajax.reload(null, false);//null???callback,false????????????????????????
                                    }
                                }]
                            });
                        } else {
                            myTable.ajax.reload(null, false);//null???callback,false????????????????????????
                        }
                    },
                    error: function (response, textStatus) {/*????????????404,500?????????*/
                        $("#errorText").html(response.responseText);
                        $("#dialog-error").removeClass('hide').dialog({
                            modal: true,
                            width: 600,
                            title: "??????????????????" + response.status,//404???500???
                            buttons: [{
                                text: "??????", "class": "btn btn-primary btn-xs", click: function () {
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
                        myTable.ajax.reload(null, false);//null???callback,false????????????????????????
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
            // console.log($('#crowd option:selected').val());
            var url = "/crowd/listCrowd.jspa?showDeleted={0}".format($('#showDeleted').is(':checked'));
            $('.form-search select').each(function () {
                if ($(this).val())
                    url += "&" + $(this).attr("name") + "=" + $(this).val();
            });
            $('.form-search :text').each(function () {
                if ($(this).val())
                    url += "&" + $(this).attr("name") + "=" + $(this).val();
            });

            options = [{value: "??????", text: '??????'}, {value: "??????", text: '??????'}];
            $('input:radio[name=status]:eq(0)').val("??????");
            $('input:radio[name=status]:eq(0)+span').text("??????");
            $('input:radio[name=status]:eq(1)').val("??????");
            $('input:radio[name=status]:eq(1)+span').text("??????");

            myTable.ajax.url(encodeURI(url)).load();
        }

        var crowdForm = $('#crowdForm');
        var saveUrl = "/crowd/saveCrowd.jspa";
        var validator = crowdForm.validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            ignore: "",
            rules: {
                //name: {required: true},
                latitude: {max: ${latitudeMax}, min: ${latitudeMin}, required: true},
                longitude: {max:  ${longitudeMax}, min:  ${longitudeMin}, required: true}
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
                    data: crowdForm.serialize(),
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",//http://www.cnblogs.com/yoyotl/p/5853206.html
                    cache: false,
                    success: function (response, textStatus) {
                        var result = JSON.parse(response);
                        if (!result.succeed) {
                            bootbox.alert({
                                message: result.message,
                                callback: function () {
                                    $("#dialog-edit").dialog("close");
                                }
                            });
                        } else {
                            myTable.ajax.reload(null, false);//null???callback,false????????????????????????
                            $("#dialog-edit").dialog("close");
                        }
                    },
                    error: function (response, textStatus) {/*????????????404,500?????????*/
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

        function editCrowd(crowdID) {
            $.getJSON("/crowd/getCrowd.jspa?crowdID=" + crowdID, function (ret) {
                saveUrl = "/crowd/saveCrowd.jspa";
                showCrowdDialog(ret);
            });
        }
        function drapWindow(crowdID) {
            var iWidth = window.screen.availWidth * .9; //?????????????????????;
            var iHeight = window.screen.availHeight * .85; //?????????????????????;
            var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //???????????????????????????;
            var iLeft = (window.screen.availWidth - 20 - iWidth) / 2; //???????????????????????????;
            let myWindow = window.open('/crowd/drap.jspa?crowdID=' + crowdID, 'crowd_' + crowdID, 'height=' + iHeight + ',innerHeight=' + iHeight + ',width=' + iWidth + ',innerWidth=' + iWidth + ',top=' + iTop + ',left=' + iLeft + ',toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no');
            myWindow.focus();

            var loop = setInterval(function () {
                if (myWindow.closed) {
                    clearInterval(loop);
                    myTable.ajax.reload(null, false);//null???callback,false????????????????????????
                }
            }, 500);
        }

        function deleteCrowd(crowdID, location, address, crowdText) {
            if (crowdID === undefined) return;
            $('#crowdText').text(crowdText);
            $('#locationForDelete').text(location);
            $('#addressForDelete').text(address);
            $("#dialog-delete").removeClass('hide').dialog({
                resizable: false,
                modal: true,
                title: "????????????",
                //title_html: true,
                buttons: [
                    {
                        html: "<i class='ace-icon fa fa-trash bigger-110'></i>&nbsp;??????",
                        "class": "btn btn-danger btn-minier",
                        click: function () {
                            $.ajax({
                                type: "POST",
                                url: "/crowd/deleteCrowd.jspa?crowdID={0}".format(crowdID),
                                //contentType: "application/x-www-form-urlencoded; charset=UTF-8",//http://www.cnblogs.com/yoyotl/p/5853206.html
                                cache: false,
                                success: function (response, textStatus) {
                                    var result = JSON.parse(response);
                                    if (result.succeed) {
                                        myTable.ajax.reload();
                                    } else
                                        bootbox.alert({message: "???????????????" + result.succeed + "\n" + result.message});
                                    /*showDialog("???????????????" + result.succeed, result.message);*/
                                    $('#dialog-delete').dialog('close');
                                },
                                error: function (response, textStatus) {/*????????????404,500?????????*/
                                    //showDialog("??????????????????" + response.status, response.responseText);
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
                        html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; ??????",
                        "class": "btn btn-minier",
                        click: function () {
                            $(this).dialog("close");
                        }
                    }
                ]
            });
        }

        var markers = [];
        var url = 'https://restapi.amap.com/v3/geocode/geo?address={0}&output=json&key=${key2}';
        var reg = /\d{2,}\.\d+/;

        function p1(addr, chinese, color) {
            ///console.log("addr:" + addr);
            ///console.log("reg.test(addr):" + reg.test(addr));
            if (addr && !reg.test(addr)) {
                if (addr.length > 2 && addr.indexOf("??????") < 0) //&&
                    addr = "?????????" + addr;
                $.getJSON(url.format(addr), function (ret) {
                    if (ret.status === "1" && ret.count === "1") {
                        if (ret.geocodes[0].formatted_address.indexOf("???????????????????????????") === 0) {
                            markers.push({label: chinese, color: color, lnglat: ret.geocodes[0].location.split(",")});
                        }
                    }
                    runTwice();
                });
            } else runTimes++;
        }

        var runTimes = 0;

//https://restapi.amap.com/v3/staticmap?markers=large,0xFF0000,%E4%BD%8D:116.37359,39.92437|large,0xFF0000,%E5%9C%B0:116.47359,39.92437&key=${key2}&radius
        function runTwice() {
            runTimes++;
            let markerparam = "large,{0},{1}:{2},{3}";
            let lastUrl = "https://restapi.amap.com/v3/staticmap?scale=2&size=200*200&key=${key2}&radius&markers=";
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
                $('#amap').html('<div class="center red">????????????????????????</div>');
                return;
            }
            if (addr.length > 2 && addr.indexOf("??????") < 0) //&&
                addr = "?????????" + addr;
            //https://restapi.amap.com/v3/staticmap?markers=large,0xFF0000,%E4%BD%8D:116.37359,39.92437|large,0xFF0000,%E5%9C%B0:116.47359,39.92437&key=${key2}&radius
            $.getJSON(url.format(addr), function (ret) {
                if (ret.status === "1") {
                    if (ret.geocodes.length === 1) {
                        $('#amap').html('?????????????????? <span style="color: #0288d1; ">{0}</span>'.format(ret.geocodes[0].formatted_address));
                        if (ret.geocodes[0].formatted_address.indexOf("???????????????????????????") === 0) {
                            var lnglat = ret.geocodes[0].location.split(",");
                            $('#longitude').val(lnglat[0]);
                            $('#latitude').val(lnglat[1]);

                            let markerparam = "large,{0},{1}:{2},{3}";
                            let lastUrl = "https://restapi.amap.com/v3/staticmap?scale=2&size=200*200&key=${key2}&radius&markers=";
                            lastUrl += markerparam.format('0xE59866', '???', lnglat[0], lnglat[1]);
                            $('#img').attr("src", lastUrl);
                        } else {
                            $('#longitude').val(0);
                            $('#latitude').val(0);
                        }
                    }
                } else {
                    $('#amap').html('<div class="center red">?????????????????????</div>');
                }
            });
        }

        $('#byLocation').on("click", function () {
            queryLnglat($('#location').val());
        });


        $('#byAddress').on("click", function () {
            queryLnglat($('#address').val());
        });

        function showCrowdDialog(crowd) {
            validator.resetForm();//??????????????????
            let title = (crowd.crowdID === 0 ? "??????" : "??????") + "??????";

            $('input:radio[name=status]').filter('[value=' + crowd.status + ']').prop('checked', true);

            $('#img').attr("src", "/components/jquery.easyui/themes/icons/blank.gif");
            //$('#img').attr("height", 350);
            $('#street').val(crowd.street);
            $('#amap').html("????????????????????????4???????????????1???????????????11??????<br/>????????????????????????4???????????????1???????????????9??????");
            $('#crowdID').val(crowd.crowdID);
            $('#address').val(crowd.address);
            $('#location').val(crowd.location);
            $('#longitude').val(crowd.longitude);
            $('#latitude').val(crowd.latitude);
            $('#stayTime').val(crowd.stayTime);

            $('#highRisk').val(crowd.highRisk);
            $('#knit').val(crowd.knit);
            $('#subknit').val(crowd.subknit);
            $('#important').val(crowd.important);
            $('#patient').val(crowd.patient);
            $('#linkPhone').val(crowd.linkPhone);
            runTimes = 0;
            markers = [];
            p1(crowd.location, "???", '0xFFFF00');
            p1(crowd.address, "???", '0x00FF00');
            // "https://restapi.amap.com/v3/staticmap?markers=large,0xFF0000,%E4%BD%8D:116.37359,39.92437|large,0xFF0000,%E5%9C%B0:116.47359,39.92437&key=${key2}&radius");
            $("#dialog-edit").removeClass('hide').dialog({
                resizable: false, width: 860, height: 620, modal: true, title: title,
                //icon:'fa fa-key',
                buttons: [
                    {
                        html: "<i class='ace-icon fa fa-floppy-o bigger-110'></i>&nbsp;??????",
                        "class": "btn btn-danger btn-minier",
                        click: function () {
                            if (crowdForm.valid())
                                crowdForm.submit();
                        }
                    }, {
                        html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp;??????",
                        "class": "btn btn-minier",
                        click: function () {
                            $('#dialog-edit').dialog('close');
                        }
                    }],
                title_html: true
            });
        }

        $.fn.editable.defaults.mode = 'inline';
    });
</script>
<!-- #section:basics/content.breadcrumbs -->
<div class="breadcrumbs ace-save-state" id="breadcrumbs">
    <ul class="breadcrumb">
        <li>
            <i class="ace-icon fa fa-home home-icon"></i>
            <a href="/index.jspa">??????</a>
        </li>
        <li class="active">????????????</li>
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
                ?????????
                <select id="byStreet" name="byStreet" class="nav-search-input">
                    <option value="" selected>??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                    <option value="??????">??????</option>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <label>????????? ???</label>
                <select id="coordinate" name="coordinate" class="nav-search-input">
                    <option value="all" selected>??????</option>
                    <option value="fixed">?????????</option>
                    <option value="unfixed">??????</option>
                </select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <label>?????? ???</label>
                <input type="text" placeholder="?????? ..." name="address" autocomplete="off"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <label>???????????? ???</label>
                <input type="checkbox" id="showDeleted">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-sm btn-success">
                    ??????
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
                        ????????????
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
        ?????? <span id="crowdText" class="brown"></span>??? <span id="locationForDelete" class="red"></span> ???<br/>
        ?????????<span id="addressForDelete" class="black"></span>
    </div>

    <div class="space-6"></div>

    <p class="bigger-110 bolder center grey">
        <i class="icon-hand-right blue bigger-120"></i>
        ????????????
    </p>
</div>
<div id="dialog-index" class="hide">
    <div class="alert alert-info bigger-110">
        ??????<br/> <span id="filename4" class="light-orange "></span> <br/>???????????????????????????????????????????????????????????????????????????
    </div>

    <div class="space-6"></div>

    <p class="bigger-110 bolder center grey">
        <i class="icon-hand-right blue bigger-120"></i>
        ????????????
    </p>
</div>

<div id="dialog-edit" class="hide">
    <div class="col-xs-6" style="padding-top: 10px">
        <!-- PAGE CONTENT BEGINS -->
        <form class="form-horizontal" role="form" id="crowdForm">
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="patient">????????????</label>
                    <div class="col-xs-4">
                        <input type="text" id="patient" name="patient" style="width: 100%" placeholder="????????????"/>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="location">????????????</label>
                    <div class="col-xs-8">
                        <input type="text" id="location" name="location" style="width: 100%" placeholder="????????????"/>
                    </div>
                    <div class="col-xs-1">
                        <button type="button" class="btn btn-info btn-minier" id="byLocation" title="???????????? -> ?????????">
                            <i class="ace-icon fa fa-map-pin icon-on-right bigger-110"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="address"> ??????</label>
                    <div class="col-xs-8">
                        <input type="text" id="address" name="address" style="width: 100%" placeholder="??????"/>
                    </div>
                    <div class="col-xs-1 pull-left">
                        <button type="button" class="btn btn-info btn-minier" id="byAddress" title="?????? -> ?????????">
                            <i class="ace-icon fa fa-map-pin icon-on-right bigger-110"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="longitude"> ?????? </label>
                    <div class="col-xs-4">
                        <input type="text" id="longitude" name="longitude" style="width: 100%" placeholder="??????"/>
                    </div>
                    <label class="col-xs-2 control-label no-padding-right" for="latitude"> ?????? </label>
                    <div class="col-xs-4">
                        <input type="text" id="latitude" name="latitude" style="width: 100%" placeholder="??????"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="stayTime">????????????</label>
                    <div class="col-xs-10">
                        <input rows="3" type="text" id="stayTime" name="stayTime" style="width: 100%" placeholder="????????????"/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="highRisk">?????????</label>
                    <div class="col-xs-4">
                        <input type="text" id="highRisk" name="highRisk" style="width: 100%" placeholder="?????????"/>
                    </div>
                    <label class="col-xs-2 control-label no-padding-right" for="knit">??????</label>
                    <div class="col-xs-4">
                        <input type="text" id="knit" name="knit" style="width: 100%" placeholder="??????"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="subknit">?????????</label>
                    <div class="col-xs-4">
                        <input type="text" id="subknit" name="subknit" style="width: 100%" placeholder="?????????"/>
                    </div>
                    <label class="col-xs-2 control-label no-padding-right" for="important">??????</label>
                    <div class="col-xs-4">
                        <input type="text" id="important" name="important" style="width: 100%" placeholder="??????"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="street"> ??????</label>
                    <div class="col-xs-4">
                        <select id="street" name="street" class="nav-search-input">
                            <option value="?????????" selected>?????????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                            <option value="??????">??????</option>
                        </select>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="form-group" style="margin-bottom: 3px;margin-top: 3px">
                    <label class="col-xs-2 control-label no-padding-right" for="longitude"> ?????? </label>
                    <div class="radio col-xs-4">
                        <label>
                            <input name="status" type="radio" class="ace" value="??????"/>
                            <span class="lbl">??????</span>
                        </label>
                        <label>
                            <input name="status" type="radio" class="ace" value="??????" checked/>
                            <span class="lbl">??????</span>
                        </label>
                    </div>
                </div>
            </div>

            <input type="hidden" id="crowdID" name="crowdID">
        </form>
    </div>

    <div class="col-xs-6">
        <label class="control-label no-padding-right" for="address"> ??????????????????????????????????????????</label>
        <div id="container" class="map"><span class="border"><img id="img"/></span></div>
        <label class="control-label no-padding-right" style="height: 20px" for="address" id="amap"></label>
    </div>
</div>
<div id="dialog-error" class="hide alert" title="??????">
    <p id="errorText">?????????????????????????????????????????????????????????</p>
</div>


<div id="Layer1" style="display: none; position: absolute; z-index: 100;">
</div>
<div id="dialog-drap" class="hide" style="z-index: 99999999">
    <iframe name="tabIframe" id="ifm80" src="/crowd/drap.jspa?crowdID=19" width="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes"
            allowtransparency="yes">
    </iframe>
</div>