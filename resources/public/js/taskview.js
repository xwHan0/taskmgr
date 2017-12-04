tinyMCE.init({
	mode : "textareas",
	plugins: [                             //插件，可自行根据现实内容删除
		"advlist autolink lists charmap print preview hr anchor pagebreak spellchecker",
		"searchreplace wordcount visualblocks visualchars fullscreen insertdatetime  nonbreaking",
		"save table contextmenu directionality emoticons paste textcolor"
	]
  });

$(document).ready(function() {
	$('#task_subtask_tbl').DataTable();
	
} );

// $("#Test_Status").onclick(function(){
// 	alert("Hello.")
// })

//$("#test").on('click',function(){
//	alert("Hello.");
//})


// goog.require("taskview.core");