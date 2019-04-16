//服务层
app.service('orderPayService',function($http){

    //读取列表数据绑定到表单中
    this.findAll=function(){
        return $http.get('../orderPay/findAll.do');
    }
    //分页
    this.findPage=function(page,rows){
        return $http.get('../orderPay/findPage.do?page='+page+'&rows='+rows);
    }
    //查询实体
    this.findOrderItem=function(id){
        return $http.get('../orderPay/findOrderItem.do?id='+id);
    }
    this.findOrderById=function(id){
        return $http.get('../orderPay/findOrderById.do?id='+id);
    }
    //增加
    this.add=function(entity){
        return  $http.post('../orderPay/add.do',entity );
    }
    //修改
    this.update=function(entity){
        return  $http.post('../orderPay/update.do',entity );
    }
    //删除
    this.dele=function(ids){
        return $http.get('../orderPay/delete.do?ids='+ids);
    }
    //删除
    this.delPay=function(orderId){
        return $http.get('../orderPay/delPay.do?orderId='+orderId);
    }
    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../orderPay/search.do?page='+page+"&rows="+rows, searchEntity);
    }

    this.updateStatus = function(ids,status){
        return $http.get('../orderPay/updateStatus.do?ids='+ids);
    }
    this.payMoney = function(orderItemId){
        return $http.get('../orderPay/payMoney.do?orderItemId='+orderItemId);
    }
});
