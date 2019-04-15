// 定义服务层:
app.service("userCountService",function($http) {

    this.userCount = function(){
        return $http.get("../user/userCount.do");
    }
});