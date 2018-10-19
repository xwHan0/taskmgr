





$(document).ready(function(){

    $("#newtask").on("click", function(){
   	     layer.open({
		           type: 2,			//iframe
		           title: 'Add Comment',
		           shadeClose: true,
		           shade: 0.5,
		           area: ['600px', '60%'],
		           content: '/new_task.html',
		           btn: ["Close", "Pop"],
		           yes: function(index,layero){
			              parent.location.reload(); 
			              layer.close(index);
		           }
		        //    btn2: function(index,layero){
		        //        window.open(href);
		        //        layer.close(index);
		        //    }
		       }); 
	})
	
    
})