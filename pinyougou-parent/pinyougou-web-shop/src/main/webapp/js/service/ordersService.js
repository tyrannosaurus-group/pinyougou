//服务层
app.service('ordersService',function($http){

    //读取列表数据绑定到表单中
    this.findCount=function(){
        return $http.get('../orders/findCount.do');
    }
    //分页
    this.findPage=function(page,rows){
        return $http.get('../goods/findPage.do?page='+page+'&rows='+rows);
    }
});
