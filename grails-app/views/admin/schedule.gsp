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

    <r:script>

        $(document).ready(function() {
            setTimeout(reloadPage, 5 * 1000);
        });

        function reloadPage() {
            window.location ="${createLink(action:'schedule')}";
        }

    </r:script>

    <body>

        <content tag="pageTitle">Schedule</content>

        <content tag="adminButtonBar">
        </content>

        <div class="alert ${runningJobs.size() > 0 ? 'alert-info' : ''}">
            <g:if test="${runningJobs.size() > 0}">
                <div>
                    Indexing job is running
                </div>

            </g:if>
            <g:else>
                <div>
                    Indexing job is not running
                </div>
            </g:else>
        </div>

        <table class="table table-striped table-bordered">
            <tr>
                <td>
                    <button id="btnRunNow" class="btn">Run Indexing Job Now!</button>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="btnCancelJob" class="btn btn-warning">Stop and suspend Indexing Job</button>
                </td>
            </tr>

        </table>

    </body>
</html>
<r:script type="text/javascript">

    $(document).ready(function() {

        $("#btnRunNow").click(function(e) {
            e.preventDefault();
            window.location = "${createLink(action:'triggerIndexingJob')}";
        });

        $("#btnCancelJob").click(function(e) {

            e.preventDefault();
            window.location = "${createLink(action:'interruptIndexingJob')}";
        });

    });

</r:script>

