// 定义控制器:
app.controller("orderCountController",function($scope,$controller,$http,orderCountService){
	// AngularJS中的继承:伪继承
	$controller('baseController',{$scope:$scope});

    // 查询所有的品牌列表的方法:
    $scope.orderCount = function(){
        // 向后台发送请求:
        orderCountService.orderCount().success(function(response){
            $scope.entity = response;
            $scope.entity.total = $scope.entity.total.toFixed(2);
        });
    }

});
