// 定义服务层:
app.service("user2Service",function($http){

    this.userAnalyze = function(){
        return $http.get("../user/userAnalyze.do");
    }
});