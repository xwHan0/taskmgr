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

var description_layer = function(tid){
	layer.open({
		type: 2,			//iframe
		title: 'Add Status',
		shadeClose: true,
		shade: 0.5,
		area: ['600px', '60%'],
		content: "/add_status?id=" + tid,
		btn: "Close",
		yes: function(index,layero){
			parent.location.reload(); 
		}
		}); 
}