//服务层
app.service('seckillOrderService',function($http){

    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../seckillOrder/search.do?page='+page+"&rows="+rows, searchEntity);
    };


});
