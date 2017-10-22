
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
    var bigbookname = $("#bigsearchbookname").val();
    if(bigbookname!=null && bigbookname!=""&&bigbookname!=undefined){
        self.location.href="/login/book/openBookSubject?page="+page+"&bookname="+bigbookname;
    }else{
        self.location.href="/login/book/openBookSubject?page="+page;
    }
}
function numberToChineseNumber(numstr) {
    var numb= ["1","2","3","4","5","6","7","8","9","0"];
    var chineseNumb = ["一","二","三","四","五","六","七","八","九","十","零"];

    for(var i in numb){
        if(numstr.indexOf(numb[i])>=0){
            numstr = numstr.replace()
        }
    }

}
//章节阅读
function openArticleDetail(articleid,bookid) {
    self.location.href="/login/article/openArticleDetail?articleid="+articleid+"&bookid="+bookid;
}