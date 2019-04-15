// 定义服务层:
app.service("orderCountService",function($http) {

    this.orderCount = function(){
        return $http.get("../order/orderCount.do");
    }
});