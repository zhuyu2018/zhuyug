
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
function openMiyu(miyuid,miyuname) {
    self.location.href="/login/article/openMiyu?miyuid=" + miyuid + "&miyuname="+miyuname;
}
function openArticle(articleid,articlename) {
    self.location.href="/login/article/openArticle?articleid=" + articleid + "&articlename="+articlename;
}

function compare(a,b,c) {
    if (parseInt(a) > parseInt(b)) {
        if (parseInt(a) > parseInt(c)) {
            return a;
        }
        return c;
    } else {
        if (parseInt(b) > parseInt(c)) {
            return b;
        }
        return c;
    }
}

function openBookSubject(page) {
    self.location.href="/login/book/openBookSubject?page="+page;
}