// 定义控制器:
app.controller("userOrderController",function($scope,$controller,$http,userOrderService){
	// AngularJS中的继承:伪继承
	$controller('baseController',{$scope:$scope});

    $scope.searchEntity={};


    // 假设定义一个查询的实体：searchEntity
    $scope.search = function(page,rows){
        $scope.page=page;
        $scope.rows=rows;
        // 向后台发送请求获取数据:
        userOrderService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
            //alert($scope.list[0].orderList[0].orderId)
        });
    }

    $scope.export = function () {
        window.open("../order/download.do?");
    };


    // 显示状态
    $scope.status = ["","未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];
});
