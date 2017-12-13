$(function() {
$( "#dialog-1" ).dialog({

width: 500,
autoOpen: false,
modal: true,
draggable:false,
resizable:false,
show: 'fold', hide: 'fold' 
});
$( "#uploadDialog" ).click(function() {
$( "#dialog-1" ).dialog( "open" );
});
});
$( document ).ready(function() {
		
$("#closeDialog").on('click',function(){
	$('#dialog-1').dialog('close');
});	
	
$("#fileUpload").on('change', function () {

     //Get count of selected files
     var countFiles = $(this)[0].files.length;

     var imgPath = $(this)[0].value;
     var extn = imgPath.substring(imgPath.lastIndexOf('.') + 1).toLowerCase();
     var image_holder = $("#image-holder");
     image_holder.empty();

     if (extn == "gif" || extn == "png" || extn == "jpg" || extn == "jpeg") {
         if (typeof (FileReader) != "undefined") {

             //loop for each file selected for uploaded.
             for (var i = 0; i < countFiles; i++) {

                 var reader = new FileReader();
                 reader.onload = function (e) {
                     $("<img />", {
                         "src": e.target.result,
                             "class": "floor-image",
							 "id" : "scream"
                     }).appendTo(image_holder);
                 }

                 image_holder.show();
                 reader.readAsDataURL($(this)[0].files[i]);
             }

         } else {
             alert("This browser does not support FileReader.");
         }
     } else {
         alert("Pls select only images");
     }
	 //$('#dialog-1').dialog('close');
 });

 
 
 var counts = [0];
    var resizeOpts = {
      handles: "all" ,autoHide:true
    };
   $(".apImg").draggable({
                         helper: "clone",
                         //Create counter
                         start: function() { counts[0]++; }
                        });
 $("#image-holder").droppable({
       drop: function(e, ui){
               if(ui.draggable.hasClass("apImg")) {
     $(this).append($(ui.helper).clone());
   //Pointing to the apImg class in image-holder and add new class.
         $("#image-holder .apImg").addClass("item-"+counts[0]);
            $("#image-holder .img").addClass("imgSize-"+counts[0]);

   //Remove the current class (ui-draggable and apImg)
         $("#image-holder .item-"+counts[0]).removeClass("apImg ui-draggable ui-draggable-dragging");

$(".item-"+counts[0]).dblclick(function() {
$(this).remove();
});
make_draggable($(".item-"+counts[0]));
      //$(".imgSize-"+counts[0]).resizable(resizeOpts);
       }

       }
      });

 var zIndex = 0;
function make_draggable(elements)
{
elements.draggable({
containment:'parent',
start:function(e,ui){ ui.helper.css('z-index',++zIndex); },
stop:function(e,ui){
}
});
}

 });
 
 function sendHeatmapRequest()
 {
	 $('#loadingDiv').show();
	 var factor = $("#scale").val();
	 var floorSize = getPixelInfo();
	 var apNum = 0;
	 var ap = new Array();
	 var containerOffset = $('#image-holder').offset();
	 var place = $("#place").val();
	$("#image-holder").find('.apImgR').each(function( e ){
		apNum++;
		ap[apNum] = {};
		ap[apNum].x =  Math.round( $(this).offset().top - containerOffset.top + 15);
		ap[apNum].y = Math.round( $(this).offset().left - containerOffset.left + 15);
		ap[apNum].freq = $(this).data('freq');
		var freq= ap[apNum].freq;
		var lossCoff;
		switch(place)
		{
		case 'a': if(freq == 2.4)
					{
						lossCoff = 28;
					}
					else
					{
						lossCoff = 30;
					}
					break;		
		case 'h': if(freq == 2.4)
		{
			lossCoff = 28;
		}
		else
		{
			lossCoff = 28;
		}
		break;
		case 'o': if(freq == 2.4)
		{
			lossCoff = 30;
		}
		else
		{
			lossCoff = 31;
		}
		break;
		}
		ap[apNum].lossCoff = lossCoff;
		
	});
	var fsize= floorSize.toString();
	var http = new XMLHttpRequest();
	//var url = "generateHeatMap?factor="+factor+"&floorSize="+fsize;
	var url = "generateHeatMap";
	var params = "factor="+factor+"&floorSize="+fsize; 
	console.log(factor);
	for(var i=1;i<ap.length;i++)
	{
		var myArray = [];
		myArray[0] = ap[i].x;
		myArray[1] = ap[i].y;
		myArray[2] = ap[i].freq;
		myArray[3] = ap[i].lossCoff;
		params += "&ap"+i+"="+myArray;
	}
	
	http.open("POST", url, true);

	//Send the proper header information along with the request
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	http.onreadystatechange = function() {//Call a function when the state changes.
	    if(http.readyState == 4 && http.status == 200) {
	        generateHeatMap(http.responseText);
	    }
	}
	http.send(params);
 }
 
 
function getPixelInfo() {
	var floorSize = new Array();
	var c = document.getElementById("myCanvas");
    var ctx = c.getContext("2d");
    var img = document.getElementById("scream");
    ctx.drawImage(img, 0, 0);
    var imgData = ctx.getImageData(0, 0, c.width, c.height);
    // invert colors
    var i;
	var myCount=0;
	var black =0;
    for (i = 0; i < imgData.data.length; i += 4) {
       var deltaR = 255 - imgData.data[i];
       var deltaG = 255 - imgData.data[i+1];
       var deltaB = 255 - imgData.data[i+2];
       var a = 255;
	
	    if(!(deltaR<5 && deltaG<5 && deltaB<5))
		{
			floorSize[myCount] = 1;
			black++;
			//console.log("black");
		}
		else
		{
			floorSize[myCount] = 0;
			//	console.log("white");
		}
	    myCount++;
    }
    return floorSize;
}

function generateHeatMap(data){
	var respData = JSON.parse(data);
	var sig = respData.SignalStrength;
	var heatmapInstance = h337.create({
		  // only container is required, the rest will be defaults
		  container: document.getElementById('heatmapContainer'),
		  gradient: {
			    // enter n keys between 0 and 1 here
			    // for gradient color customization
			  	'.3': '#a8e9ff',
			    '.4': '#14d92b',
			    '.6': '#ff2b00',
			    '.8': '#ff2b00'
			  }
		});

		// now generate some random data
		var points = [];
		var max = 0;
		var width = 640;
		var height = 480;
		var len = 200;

		var max = 0;
		for(var k=0;k<sig[0].length;k++)
		{
		var count = 0;
		for(var i=0;i<480;i++)
		{
			for(var j=0;j<640;j++)
			{
				
				var val = sig[0][k][count];
				max = Math.max(max, val);
				var point = {
					x : j,
					y: i,
					value: val,
					radius: 1
				}
				points.push(point);
					
				count++;
				
			}
			
		}
		}
		
		// heatmap data format
		var data = { 
		  max: max, 
		  data: points 
		};
		
		// if you have a set of datapoints always use setData instead of addData
		// for data initialization
		console.log("done");
		heatmapInstance.setData(data);
		$('#loadingDiv').hide();
}