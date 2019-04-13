//订单列表控制器
app.controller('ordersPageController',function($scope,ordersService){
	$controller('baseController',{$scope:$scope});
	$scope.findOrderList=function(page,rows){
		ordersService.findOrderList(page,rows).success(
					function(response){
						$scope.paginationConf.totalItems = response.total;
						$scope.list = response.data;
					}
			);
	}
});