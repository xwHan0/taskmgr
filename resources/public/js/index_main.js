//alert("hellow, world!")


$(document).ready(function(){

    $("image").on("click", function(e){
        gele = $(e.target).parent()
        detailName = '#' + gele.attr('id') + '_detail'
        $(detailName).toggle()
        $(gele).children("image#expand_btn").toggle()
    })

})

