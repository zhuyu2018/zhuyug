function  mouseThing(obj) {
    $("#loginOut").find("img").attr("src","/image/lgo2.png");
}
function  mouseOutThing() {
    $("#loginOut").find("img").attr("src","/image/lgo2.png");
}
function  loginOut(obj) {
    $("#loginOut").find("img").attr("src","/image/lgo2.png");
    $.post("loginOut",{loginout:"out"},function (result) {
        if(result=="out"){
            location.reload();
        }
    })
}
function goParent(){
    window.history.go(-1);
}