<%@ page import="grails.util.Environment" %>
%{--
  - ﻿Copyright (C) 2013 Atlas of Living Australia
  - All Rights Reserved.
  -
  - The contents of this file are subject to the Mozilla Public
  - License Version 1.1 (the "License"); you may not use this file
  - except in compliance with the License. You may obtain a copy of
  - the License at http://www.mozilla.org/MPL/
  -
  - Software distributed under the License is distributed on an "AS
  - IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
  - implied. See the License for the specific language governing
  - rights and limitations under the License.
  --}%

<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
    <head>
        <r:require module="jquery"/>
        <r:require module="jquery-ui" />

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title><g:layoutTitle default="BHL FTIndex Management"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="https://www.ala.org.au/wp-content/themes/ala2011/images/favicon.ico" type="image/x-icon">
        <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
        <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">

        <g:layoutHead/>
        <r:require module="bootstrap"/>
        <r:require module="application"/>
        <r:layoutResources/>
        <Style type="text/css">

        body {
            padding-top: 60px;
            padding-bottom: 70px;
        }

        /*.footer {*/
            /*height: 59px;*/
            /*bottom: 0px;*/
            /*position: fixed;*/
            /*width: 100%;*/
            /*background-color: #efefef;*/
        /*}*/

        /*.footer img {*/
            /*max-width: inherit;*/
        /*}*/

        #buttonBar .btn {
            margin-top: 0px;
        }

        </Style>

        <script type="text/javascript">

            $(document).ready(function (e) {

                $.ajaxSetup({ cache: false });

            });

        </script>

    </head>

    <body style="overflow: auto">
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">

                <div class="container-fluid">
                    <a class="brand" href="${createLink(uri:'/')}">
                        BHL Fulltext Index Management
                    </a>
                    <div class="nav-collapse collapse">
                        <div class="navbar-text pull-right">
                            <span id="buttonBar">
                                <g:pageProperty name="page.buttonBar"/>
                            </span>
                        </div>
                        <bhl:navbar active="${pageProperty(name: 'page.topLevelNav')}"/>
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </div>
        <g:layoutBody/>
        <div class="footer" role="contentinfo">
        </div>

        <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
        <g:javascript library="application"/>
        <r:layoutResources/>
    </body>
</html>