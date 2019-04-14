// 定义服务层:
app.service("orderService",function($http){

    this.search = function(page,rows,searchEntity){
        return $http.post("../order/search.do?pageNo="+page+"&pageSize="+rows,searchEntity);
    }

});