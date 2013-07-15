<%@ page import="bhl.ftindex.manage.Item" %>
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
<html>
    <head>
        <meta name="layout" content="adminLayout"/>
        <title>Administration</title>
    </head>

    <body>
        <content tag="pageTitle">Items</content>

        <content tag="adminButtonBar">
            <button id="btnLoadSingle" class="btn">Load Single Item</button>
            <button id="btnLoad" class="btn">Load Items</button>
            <button id="btnDeleteAll" class="btn btn-danger">Delete all items</button>
        </content>

        <div class="row">

            <div class="span12">
                ${Item.count()} Items currently in database

                <h4>Items in Error</h4>
                <table class="table table-bordered table-striped">
                    <thread>
                        <th>ItemID</th>
                        <th>Internet Archive Id</th>
                        <th>Title</th>
                        <th>Volume</th>
                        <th>Status</th>
                        <th>Retry Count</th>
                        <th></th>
                    </thread>
                    <tbody>
                        <g:each in="${errorItems}" var="item">
                            <tr>
                            <td>${item.itemId}</td>
                            <td>${item.internetArchiveId}</td>
                            <td>${item.title}</td>
                            <td>${item.volume}</td>
                            <td>${item.status}</td>
                            <td>${item.retryCount}</td>
                            <td><a href="${createLink(action:'deleteItem', id: item.id)}" class="btn btn-mini btn-danger"><i class="icon-remove icon-white"></i></a></td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
                <g:paginate total="${totalCount ?: 0}" />
            </div>

        </div>
    </body>
</html>
<r:script type="text/javascript">

    $(document).ready(function() {

        $("#btnDeleteAll").click(function(e) {
            e.preventDefault();
            window.location = "${createLink(action:'deleteAllItems')}";
        });

        $("#btnLoad").click(function(e) {
            e.preventDefault();
            window.location = "${createLink(action:'loadItems')}";
        });

        $("#btnLoadSingle").click(function(e) {
            e.preventDefault();
            window.location = "${createLink(action:'loadSingleItem')}";
        });


    });

</r:script>


