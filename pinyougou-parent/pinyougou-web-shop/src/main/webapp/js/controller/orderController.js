 //控制层 
app.controller('orderController' ,function($scope,$controller,$location,typeTemplateService ,itemCatService,uploadService ,orderService){

	$controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		orderService.findAll().success(
			function(response){
				$scope.list=response;
			}
		);
	}

	//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
    $scope.status = ["","未付款", "已付款", "未发货", "已发货", "交易成功", "交易关闭", "待评价"];
	//订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
    $scope.source = ["","app端", "pc端", "M端", "微信端", "手机qq端"];
	//分页
	$scope.findPage=function(page,rows){
        orderService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}

    $scope.searchDate="";
    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        orderService.search(page, rows,$scope.searchDate, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //更改状态  订单发货
    $scope.changeStatus = function (id) {
        orderService.changeStatus(id).success(
            function (response) {
                if (response.flag) {
                    $scope.reloadList();     //刷新列表
                    $scope.selectIds = [];
                    alert(response.message);
                }else {
                    alert(response.message);
                }
            }
        );
    }

	$scope.OrderList = [];


	// 显示分类:
	$scope.findOrderList = function(){

		orderService.findAll().success(function(response){// List<OrderItem>

            // List<OrderItem>
			for(var i=0;i<response.length;i++){

				//Order

				$scope.OrderList[response[i].id] = response[i].name;
			}

		});
	}
});	
