// 定义控制器:
app.controller("userController",function($scope,$controller,$http,userService){
	// AngularJS中的继承:伪继承
	$controller('baseController',{$scope:$scope});


    $scope.searchEntity={};


    // 假设定义一个查询的实体：searchEntity
    $scope.search = function(page,rows){
        // 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }

    // 审核的方法:
    $scope.updateStatus = function(status){
        userService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });

    }

    $scope.status=["","PC","H5","Android","IOS","WeChat"]
});
