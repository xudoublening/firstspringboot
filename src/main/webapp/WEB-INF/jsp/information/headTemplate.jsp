<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<META http-equiv="X-UA-Compatible" content="IE=8">
<%
    response.setHeader("X-UA-Compatible", "IE=EmulateIE8");
%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/reset1.1.1.css">
    <link rel="stylesheet" type="text/css" href="../css/common.css">
    <link rel="stylesheet" type="text/css" href="../css/content.css">
    <script type="text/javascript" src="../js/zhcode.core.js"></script>
    <script type="text/javascript"
            src="../js/component/createScrollablePanel.js"></script>
</head>
<body>
<div class="app-container">
    <%@ include file="../common/header.jsp" %>
    <div class="app-body layoutcenter mt10">
        <div class="box sidebox boxface1">
            <div class="box-header header-face-figure">
                <h3 class="box-title">
                    <span class="ico ico-rhombus ml10"></span>
                    <b class="inlinebox">华翔资讯</b>
                </h3>
            </div>
            <div class="box-body body-face-gray">
                <ul class="subnav">
                    <li>
                        <a href="../zixunzhongxin/hotspot.jsp">华翔公告</a>
                    </li>
                    <li class="active">
                        <a href="../zixunzhongxin/information.jsp">公司新闻</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="mainbox">
            <div class="crumb">
                <a href="../index.jsp"><span>主页</span>
                </a> &gt;
                <a href="../zixunzhongxin/hotspot.jsp"><span>华翔资讯</span>
                </a> &gt;
                <span>公司新闻</span>
            </div>
            <h3 class="coltitle">
                <span class="ico ico-logo"></span>
                <span class="inlinebox f16">公司新闻</span>
            </h3>
            <div class="mainbody mt10 clearfix">


