<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div id="sidebar" class="sidebar responsive ace-save-state">
    <script type="text/javascript">
        try {
            ace.settings.loadState('sidebar')
        } catch (e) {
        }

        $(function () {
            function setActiveNav() {
                var content = $.cookie("firstContent");
                if (!content) {
                    $.removeCookie('firstContent');
                } else {
                    var lastContent = $.getReferrerUrlParam("content");
                    var thisContent = $.getUrlParam("content");


                    if (thisContent === null && lastContent !== null) {//need redirect
                        console.log("need redirect:" + lastContent);
                        $(window.location).attr('href', 'index.jspa?content=' + lastContent);
                    }
                }

                var menuID = $.getUrlParam("menuID");
                var liItem = $(".nav-list li");
                if (!menuID) {
                    liItem.eq(0).addClass("active");
                } else {
                    for (var i = 0; i < liItem.length; i++) {
                        if (liItem.eq(i).find("a").attr("id") === menuID) {
                            liItem.eq(i).addClass("active");

                            if (liItem.eq(i).parent().hasClass("submenu")) {
                                liItem.eq(i).parent().parent().addClass("open");
                                liItem.eq(i).parent().parent().addClass("active");
                            }

                            break;
                        }
                    }
                }
            }

            setActiveNav();
        });
    </script>

    <div class="sidebar-shortcuts" id="sidebar-shortcuts">
        <div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
            <button class="btn btn-success">
                <i class="ace-icon fa fa-signal"></i>
            </button>

            <button class="btn btn-info">
                <i class="ace-icon fa fa-pencil"></i>
            </button>

            <!-- #section:basics/sidebar.layout.shortcuts -->
            <button class="btn btn-warning">
                <i class="ace-icon fa fa-users"></i>
            </button>

            <button class="btn btn-danger">
                <i class="ace-icon fa fa-cogs"></i>
            </button>

            <!-- /section:basics/sidebar.layout.shortcuts -->
        </div>

        <div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
            <span class="btn btn-success"></span>

            <span class="btn btn-info"></span>

            <span class="btn btn-warning"></span>

            <span class="btn btn-danger"></span>
        </div>
    </div><!-- /.sidebar-shortcuts -->

    <ul class="nav nav-list">
        <li>
            <a href="index.jspa?content=/admin/hello.html" id="0">
                <i class="menu-icon fa fa-tachometer"></i>
                <span class="menu-text"> 控制台 </span>
            </a>
        </li>


        <li>
            <a href="#" class="dropdown-toggle">
                <i class="menu-icon fa fa-map-signs"></i>

                <span class="menu-text"> 地图数据 </span>

                <b class="arrow fa fa-angle-down"></b>
            </a>

            <b class="arrow"></b>

            <ul class="submenu">
                <li class="">
                    <a href="index.jspa?content=/location/upload.jsp&menuID=10" id="10">
                        <i class="menu-icon fa fa-caret-right"></i>
                        <i class="fa fa-cloud-upload"></i>
                        <span class="menu-text">数据上传</span>
                    </a>

                    <b class="arrow"></b>
                </li>
            </ul>

            <ul class="submenu">
                <li class="">
                    <a href="index.jspa?content=/assets/assets.jspa&menuID=11" id="11">
                        <i class="menu-icon fa fa-caret-right"></i>
                        <i class="fa fa-folder-o"></i>
                        <span class="menu-text">资产管理</span>
                    </a>

                    <b class="arrow"></b>
                </li>
            </ul>
        </li>
        <li>
            <a href="#" class="dropdown-toggle">
                <i class="menu-icon fa fa-cog"></i>
                <span class="menu-text"> 系统管理 </span>

                <b class="arrow fa fa-angle-down"></b>
            </a>

            <b class="arrow"></b>

            <ul class="submenu">
                <sec:authorize access="hasAuthority('ADMIN')">
                    <li class="">
                        <a href="index.jspa?content=users.jspa&menuID=4" id="4">
                            <i class="menu-icon fa fa-caret-right"></i>
                            <i class="fa fa-user"></i>
                            <span class="menu-text">用户管理</span>

                        </a>

                        <b class="arrow"></b>
                    </li>
                </sec:authorize>
                <li class="">
                    <a href="index.jspa?content=/assets/key.jspa&menuID=5" id="5">
                        <i class="menu-icon fa fa-caret-right"></i>
                        <i class="fa fa-key"></i>
                        <span class="menu-text">地图KEY</span>
                    </a>
                    <b class="arrow"></b>
                </li>
            </ul>
        </li>
        <li>

        </li>

    </ul><!-- /.nav-list -->

    <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
        <i id="sidebar-toggle-icon" class="ace-icon fa fa-angle-double-left ace-save-state"
           data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
    </div>
</div>