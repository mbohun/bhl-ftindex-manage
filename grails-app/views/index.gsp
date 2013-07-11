<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>BHL Full Text Index Management</title>
		<style type="text/css" media="screen">
		</style>
        <r:require module="bootstrap" />
        <r:script>

            $(document).ready(function() {

                $("#btnClickIt").click(function(e) {
                    window.location = "${createLink(controller:'item', action:'index')}";
                });

            });

        </r:script>
	</head>
	<body>
        <div class="container">
            <div class="row">
                <div class="span12">
                    <div class="well">
                        <button id="btnClickIt" class="btn">Click It!</button>
                    </div>
                </div>
            </div>
        </div>
	</body>
</html>
