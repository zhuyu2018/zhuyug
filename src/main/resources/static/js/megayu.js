
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
    var chineseNumb = ["零","一","二","三","四","五","六","七","八","九","十"];
    if(numstr.length==1){
        return chineseNumb[parseInt(numstr)];
    }else if(numstr.length==2){
        var numArr = numstr.split("");
        var lastNum = "";
        if(numArr[1]!="0"){
            lastNum =chineseNumb[parseInt(numArr[1])];
        }
        return chineseNumb[parseInt(numArr[0])]+"十"+lastNum;
    }else if (numstr.length==3){
        var numArr = numstr.split("");
        var lastNum = "";
        if(numArr[2]!="0"){
            lastNum =chineseNumb[parseInt(numArr[2])];
        }
        var msg = "";
        msg = msg + chineseNumb[parseInt(numstr[0])]+"百";
        if(numArr[1]=="0"&&numArr[2]=="0"){
            return msg;
        }else if(numArr[1]=="0"&&numArr[2]!="0"){
            return msg + chineseNumb[parseInt(numArr[2])];
        }else if(numArr[1]!="0"&&numArr[2]!="0"){
            return msg +chineseNumb[parseInt(numArr[1])] + "十" +chineseNumb[parseInt(numArr[2])];
        }else if(numArr[1]!="0"&&numArr[2]=="0"){
            return msg +chineseNumb[parseInt(numArr[1])] + "十";
        }
    }else if (numstr.length==4){
        var numArr = numstr.split("");
        var msg = "";
        msg = msg + chineseNumb[parseInt(numArr[0])]+"千";
        if(numArr[1]!="0"&&numArr[2]!="0"&&numArr[3]!="0"){
            return msg + chineseNumb[parseInt(numArr[1])]+"百"+chineseNumb[parseInt(numArr[2])]+"十"+chineseNumb[parseInt(numArr[3])];
        }else if (numArr[1]!="0"&&numArr[2]=="0"&&numArr[3]!="0"){
            return msg + chineseNumb[parseInt(numArr[1])]+"百零"+chineseNumb[parseInt(numArr[3])];
        }else if (numArr[1]=="0"&&numArr[2]=="0"&&numArr[3]!="0"){
            return msg + "零"+chineseNumb[parseInt(numArr[3])];
        }else if (numArr[1]=="0"&&numArr[2]!="0"&&numArr[3]!="0"){
            return msg + "零"+chineseNumb[parseInt(numArr[2])]+"十"+chineseNumb[parseInt(numArr[3])];
        }else if(numArr[1]!="0"&&numArr[2]!="0"&&numArr[3]=="0"){
            return msg + chineseNumb[parseInt(numArr[1])]+"百"+chineseNumb[parseInt(numArr[2])]+"十";
        }else if (numArr[1]!="0"&&numArr[2]=="0"&&numArr[3]=="0"){
            return msg + chineseNumb[parseInt(numArr[1])]+"百";
        }else if (numArr[1]=="0"&&numArr[2]=="0"&&numArr[3]=="0"){
            return msg;
        }else if (numArr[1]=="0"&&numArr[2]!="0"&&numArr[3]=="0"){
            return msg + "零"+chineseNumb[parseInt(numArr[2])]+"十";
        }
    }
}
//章节阅读
function openArticleDetail(articleid,bookid) {
    self.location.href="/login/article/openArticleDetail?articleid="+articleid+"&bookid="+bookid;
}