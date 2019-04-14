//服务层
app.service('addressService',function($http){
    //读取列表数据绑定到表单中
    this.showAddress=function(){
        return $http.get('../address/findAddressList.do');
    }
    this.delete=function(id){
        return $http.get('../address/delete.do?id='+id);
    }
    this.setDefault=function(id){
        return $http.get('../address/setDefault.do?id='+id);
    }
    this.findById=function(id){
        return $http.get('../address/findById.do?id='+id);
    }
    this.update=function(entity){
        return $http.post('../address/update.do',entity);
    }
    this.add=function(tbAddress){
        return $http.post('../address/add.do',tbAddress);
    }
});