 //控制层 
app.controller('orderStaController' ,function($scope,$controller,$location,orderStaService){

	$controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
        orderStaService.findAll().success(
			function(response){
				$scope.list=response;
			}
		);
	}


    $scope.searchDate="";
    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        orderStaService.search(page, rows,$scope.searchDate, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

	$scope.OrderList = [];

});	
