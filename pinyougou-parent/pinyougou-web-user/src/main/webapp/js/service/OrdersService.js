//服务层
app.service('ordersService',function($http){
	//读取列表数据绑定到表单中
	this.findOrderList=function(page,rows){
		return $http.get("../order/findOrderList.do?pageNum="+page+"&pageSize="+rows);
	}
	
});