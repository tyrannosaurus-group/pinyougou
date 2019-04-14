// 定义服务层:
app.service("userService",function($http){

//搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../user/search.do?page='+page+"&rows="+rows,searchEntity);
    }

    this.updateStatus = function(ids,status){
        return $http.get('../user/updateStatus.do?ids='+ids+"&status="+status);
    }
});