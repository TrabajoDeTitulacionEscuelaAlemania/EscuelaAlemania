$(document).ready(function () {
    $(".nav li>ul").each(function(){ //UL que tiene todo
        if(!$(this).hasClass('open')){
            $(this).css('display', 'none'); // Hide all 2-level ul
        }
    });
    $(".dropdown").click(function () { //el li que tiuene menus
    $(this).siblings().find("ul").slideUp(); //se cierran los UL
    $(this).find("ul").slideToggle();
});
    $(".multiple").click(function () { //el li que tiuene menus
    $(this).siblings().find("ul").slideUp(); //se cierran los UL
    $(this).find("ul").slideToggle();
});

});
