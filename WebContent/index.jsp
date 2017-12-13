<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>3 Pixels</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/js/jquery-ui-1.12.1/jquery-ui.css">
<script src="js/jquery-3.1.1.js"></script>
<script src="js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script src="js/heatmap.js"></script>
<script src="js/myScript.js"></script>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<script>var ctx = "${pageContext.request.contextPath}";console.log(ctx);</script>
<body>
<div id="loadingDiv"><img src="img/loader.gif" width="100px" height="100px"/><div style="font-weight:700;">Please wait as we are studying your floor plan..</div></div>
<div id = "dialog-1" title = "Start by uploading a floor plan...">
Upload Floor Plan:<br>
<input id="fileUpload" type="file"/><br><br>
Select scaling for the floor plan:
<label>1px = </label>
<select id="scale">
  <option value="0.01">1 cm</option>
  <option value="0.1">10 cm</option>
  <option value="1">1 m</option>
  <option value="10">10m</option>
  <option value="100">100m</option>
</select>
<br><br>
Type of place:
<select id="place">
  <option value="a">Apartment</option>
  <option value="h">House</option>
  <option value="o">Office</option>
</select>
<br><br>
<button id="closeDialog" type="button">Ok</button>
</div>
<div class="topBar"><img src="img/aricent.png" style="float: right;width: 208px;"/>
<h1 style="padding-left: 30px;margin: 10px 0px;font-size: 38px;">3 Pixels</h1>
<h2 style="padding-left: 30px;">Providing solutions pixel by pixel...</h2>
</div>
<div class="centerHeader">
<h1>Enterprise WiFi RF Planning and Visualization</h1>
</div>
<div class="section1">
<button id="uploadDialog" type="button">Get Started!</button>
</div>

<div class="floorPlanEditor">
<div class="floorPlan" >
<div id="image-holder">

</div>
<div id="heatmapContainer" class="floor-image" style="top:-480px"></div>
</div>
<div class="sidePanel">
<div class="apSelection">
<div class="apImgCover"><img class="apImg apImgR" src="img/router1.png" data-freq="2.4"/><br><span class="apDesc" >2.4 GHz</span></div>
<div class="apImgCover"><img class="apImg apImgR" src="img/router3.png" data-freq="5.2"/><br><span class="apDesc">5.2 GHz</span></div>
<div class="apImgCover apDisb"><img class="apImgR" src="img/router2.png"/><br><span class="apDesc">x.x GHz</span></div>
<div class="apImgCover apDisb"><img class="apImgR" src="img/router4.png"/><br><span class="apDesc">x.x GHz</span></div>
<div class="apImgCover apDisb"><img class="apImgR" src="img/router5.png"/><br><span class="apDesc">x.x GHz</span></div>
</div>
<button id="generateHeatmapBtn" type="button" onClick="sendHeatmapRequest()">Generate Heatmap</button>
<div>

</div>
</div>
</div>
<canvas id="myCanvas" width="640" height="480" style="border:1px solid #d3d3d3;" hidden>
</body>
</html>