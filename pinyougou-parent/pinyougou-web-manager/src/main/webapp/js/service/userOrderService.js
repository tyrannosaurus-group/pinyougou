// 定义服务层:
app.service("userOrderService",function($http){

    this.search = function(page,rows,searchEntity){
        return $http.post("../order/searchOrder.do?pageNo="+page+"&pageSize="+rows,searchEntity);
    }


});