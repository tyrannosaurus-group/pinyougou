//订单列表控制器
app.controller('ordersPageController',function($scope,ordersService){
	$scope.findOrderList=function(){
		ordersService.findOrderList().success(
					function(response){
						//下面进行返回数据处理 暂时还没有处理 因为还不清楚返回值 js也还没有引入
						$scope.loginName=response.loginName;
					}
			);
	}
});