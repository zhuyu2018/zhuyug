
function  loginOut(obj) {
    $("#loginOut").find("img").attr("src","/image/lgo2.png");
    $.post("/login/loginOut",{loginout:"out"},function (result) {
        if(result=="out"){
            window.location.href="/";
        }
    })
}
function goParent(){
    window.history.go(-1);
}

function openBook(bookid,bookname) {
    self.location.href="/login/article/openBook?bookid=" + bookid + "&bookname="+bookname;
}