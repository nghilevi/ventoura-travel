function switchIndexDiv(divtag){
	if(divtag == "dashboardTitleDiv"){
		// change focus div title
		$("#dashboardTitleDiv").css("background","white").css("border-color","white");
		$("#softwareIntroTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		// change focus content to visible
		$("#dashboardContentDiv").css("display","block");
		$("#softwareIntroContentDiv").css("display","none");
	}else if(divtag == "softwareIntroTitleDiv"){
		$("#dashboardTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#softwareIntroTitleDiv").css("background","white").css("border-color","white");	
		
		$("#dashboardContentDiv").css("display","none");
		$("#softwareIntroContentDiv").css("display","block");
	}
}

function switchMaintenanceDiv(divtag){
	if(divtag == "maintenanceHelpTitleDiv"){
		// change focus div title
		$("#maintenanceHelpTitleDiv").css("background","white").css("border-color","white");
		$("#buildingsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#yellowPagesTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#newFunctionsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#bugsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		// change focus content to visible
		$("#maintenanceHelpContentDiv").css("display","block");
		$("#buildingsContentDiv").css("display","none");
		$("#yellowPagesContentDiv").css("display","none");
		$("#newFunctionsContentDiv").css("display","none");
		$("#bugsContentDiv").css("display","none");
	}else if(divtag == "buildingsTitleDiv"){
		$("#maintenanceHelpTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#buildingsTitleDiv").css("background","white").css("border-color","white");
		$("#yellowPagesTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#newFunctionsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#bugsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		
		$("#maintenanceHelpContentDiv").css("display","none");
		$("#buildingsContentDiv").css("display","block");
		$("#yellowPagesContentDiv").css("display","none");
		$("#newFunctionsContentDiv").css("display","none");
		$("#bugsContentDiv").css("display","none");
	}else if(divtag == "yellowPagesTitleDiv"){
		
		$("#maintenanceHelpTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#buildingsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#yellowPagesTitleDiv").css("background","white").css("border-color","white");
		$("#newFunctionsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#bugsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		$("#maintenanceHelpContentDiv").css("display","none");
		$("#buildingsContentDiv").css("display","none");
		$("#yellowPagesContentDiv").css("display","block");
		$("#newFunctionsContentDiv").css("display","none");
		$("#bugsContentDiv").css("display","none");
	}
	else if(divtag == "newFunctionsTitleDiv"){
		
		$("#maintenanceHelpTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#buildingsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#yellowPagesTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#newFunctionsTitleDiv").css("background","white").css("border-color","white");
		$("#bugsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		$("#maintenanceHelpContentDiv").css("display","none");
		$("#buildingsContentDiv").css("display","none");
		$("#yellowPagesContentDiv").css("display","none");
		$("#newFunctionsContentDiv").css("display","block");
		$("#bugsContentDiv").css("display","none");
	}
	else if(divtag == "bugsTitleDiv"){
	
		$("#maintenanceHelpTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#buildingsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#yellowPagesTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#newFunctionsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#bugsTitleDiv").css("background","white").css("border-color","white");
		
		$("#maintenanceHelpContentDiv").css("display","none");
		$("#buildingsContentDiv").css("display","none");
		$("#yellowPagesContentDiv").css("display","none");
		$("#newFunctionsContentDiv").css("display","none");
		$("#bugsContentDiv").css("display","block");
	}
}

function switchManualDiv(divtag){
	if(divtag == "informationPlatformTitleDiv"){
		// change focus div title
		$("#informationPlatformTitleDiv").css("background","white").css("border-color","white");
		$("#mapsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#callInMealTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#iScheduleTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#yellowPagesTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		// change focus content to visible
		$("#informationPlatformContentDiv").css("display","block");
		$("#mapsContentDiv").css("display","none");
		$("#callInMealContentDiv").css("display","none");
		$("#iScheduleContentDiv").css("display","none");
		$("#yellowPagesContentDiv").css("display","none");
	}else if(divtag == "mapsTitleDiv"){
		$("#informationPlatformTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#mapsTitleDiv").css("background","white").css("border-color","white");
		$("#callInMealTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#iScheduleTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#yellowPagesTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		
		$("#informationPlatformContentDiv").css("display","none");
		$("#mapsContentDiv").css("display","block");
		$("#callInMealContentDiv").css("display","none");
		$("#iScheduleContentDiv").css("display","none");
		$("#yellowPagesContentDiv").css("display","none");
	}else if(divtag == "callInMealTitleDiv"){
		
		$("#informationPlatformTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#mapsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#callInMealTitleDiv").css("background","white").css("border-color","white");
		$("#iScheduleTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#yellowPagesTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		$("#informationPlatformContentDiv").css("display","none");
		$("#mapsContentDiv").css("display","none");
		$("#callInMealContentDiv").css("display","block");
		$("#iScheduleContentDiv").css("display","none");
		$("#yellowPagesContentDiv").css("display","none");
	}
	else if(divtag == "iScheduleTitleDiv"){
		
		$("#informationPlatformTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#mapsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#callInMealTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#iScheduleTitleDiv").css("background","white").css("border-color","white");
		$("#yellowPagesTitleDiv").css("background","#9c0").css("border-color","#9c0");
		
		$("#informationPlatformContentDiv").css("display","none");
		$("#mapsContentDiv").css("display","none");
		$("#callInMealContentDiv").css("display","none");
		$("#iScheduleContentDiv").css("display","block");
		$("#yellowPagesContentDiv").css("display","none");
	}
	else if(divtag == "yellowPagesTitleDiv"){
	
		$("#informationPlatformTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#mapsTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#callInMealTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#iScheduleTitleDiv").css("background","#9c0").css("border-color","#9c0");
		$("#yellowPagesTitleDiv").css("background","white").css("border-color","white");
		
		$("#informationPlatformContentDiv").css("display","none");
		$("#mapsContentDiv").css("display","none");
		$("#callInMealContentDiv").css("display","none");
		$("#iScheduleContentDiv").css("display","none");
		$("#yellowPagesContentDiv").css("display","block");
	}
}

function showResourceForm(resourceForm){
	$(resourceForm).toggle();
}



/* Ajax: Login Form And Send To loginAction.jsp */
// in this function we used a XML to string function
function login(){
  $("#loginDialogDiv").dialog({
    autoOpen:false,
	modal:true,
	title:"Login",
	height:250,
	width:300,
	show:'fade',
	hide:'fade',
	open: function(){
	  $("#loginDialogDiv").load("/loginForm");
	},
	buttons: {
	  "Login": function(){
	    $("#loginErrorMessageDiv").empty();
	    var email = $("#login_email").val();
		var password = $("#login_password").val();
		var data = "email="+email+"&password="+password;
		var error_message = "";
		if(email.length == 0) error_message += "请输入Email <br>";
		if(password.length == 0) error_message += "请输入密码 <br>";
		if(error_message.length == 0)
			$.ajax({
			  url: "/loginAction",
			  type:"POST",
			  cache: false,
			  data: data,
			  async:false,
			  success: function(message){
				if( XMLtoString(message).indexOf("失败") > 0){
				  $("#loginErrorMessageDiv").append("邮箱或密码错误！");
				  $("#loginErrorMessageDiv").show();
				}
				else{
				  $("#loginDialogDiv").empty();
				  $("#loginDialogDiv").dialog("close"); 
				  window.open("/","_self");
				}
			  }
			});
		else{
		  $("#loginErrorMessageDiv").append(error_message);
		  $("#loginErrorMessageDiv").show();
		}
	  },
	  "Cancel": function(){
	    $("#loginDialogDiv").empty();
	    $(this).dialog("close"); 
	  }
	}
  });
  $("#loginDialogDiv").dialog("open");
}


function XMLtoString(elem){
	
	var serialized;
	
	try {
		// XMLSerializer exists in current Mozilla browsers
		serializer = new XMLSerializer();
		serialized = serializer.serializeToString(elem);
	} 
	catch (e) {
		// Internet Explorer has a different approach to serializing XML
		serialized = elem.xml;
	}
	
	return serialized;
}


$(document).ready(function() {
	$("#previewbtn").mousedown(function() {
		// 设置预览的标题
		var title = $("#portalPacketTitle").val();
		if (title.length != 0) {
			$("#PreVeiwHeader").html(title);
		}
		// 设置预览的标题的字体和字体颜色
		var font_color = "#" + $("#font_color").val();
		var font_family = $("#portalPacketTitleFontStyle").val();
		$("#PreVeiwHeader").css("background", font_color);
		$("#PreVeiwHeader").css("font-family", font_family);
		// 设置预览的图片
		var imageOrVideo = $("#videoOrImageType").val();
		var imageOrVideoSrc = $("#videoOrImageFileName").val();
		if (imageOrVideo == 'video') {
			if (imageOrVideoSrc.length != 0) {
				PreviewVideo();
			}

		} else {
			if (imageOrVideoSrc.length != 0) {
				PreviewImage();
			}
		}
		// 设置预览的描述性文字
		var description = $("#portalPacketContent").val();
		$("#description").html(description);
		// 设置预览的footer
		var description = $("#portalPacketFooter").val();
		$("#footerdescription").html(description);
	})
});