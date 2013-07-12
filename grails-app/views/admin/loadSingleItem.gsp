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
        <content tag="pageTitle">Load Items</content>

        <content tag="adminButtonBar">
        </content>

        <h4>Stage an individual item for indexing</h4>
        <g:form action="uploadSingleItem" class="form-horizontal">

            <div class="control-group">
                <label class="control-label" for="itemId">Item ID</label>

                <div class="controls">
                    <input type="text" name="itemId" id="itemId" placeholder="Item ID">
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-small btn-primary">Stage Item for Indexing</button>
                </div>
            </div>
        </g:form>
    </body>
</html>
<r:script type="text/javascript">

    $(document).ready(function() {

        $("#btnDeleteAll").click(function(e) {
            e.preventDefault();
            window.location = "${createLink(action: 'deleteAllItems')}";
        });

        $("#btnLoad").click(function(e) {
            e.preventDefault();
            window.location = "${createLink(action: 'loadItems')}";
        });

    });

</r:script>

