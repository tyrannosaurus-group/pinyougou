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

	//分页
	$scope.findPage=function(page,rows){
        orderService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}

	//查询实体
	//新增
	//修改
	$scope.findOne=function(){


		var id = $location.search()['id'];
		if(null == id){
			return;
		}


        orderService.findOne(id).success(//保存商品 GoodsVo  查询并回显 GoodsVo
			function(response){
				$scope.entity= response;

				// 调用处理富文本编辑器：
				editor.html($scope.entity.goodsDesc.introduction);

				// 处理图片列表，因为图片信息保存的是JSON的字符串，让前台识别为JSON格式对象
				$scope.entity.goodsDesc.itemImages = JSON.parse( $scope.entity.goodsDesc.itemImages );

				// 处理扩展属性:
				$scope.entity.goodsDesc.customAttributeItems = JSON.parse( $scope.entity.goodsDesc.customAttributeItems );

				// 处理规格
				$scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

				// 遍历SKU的集合:
				for(var i=0;i<$scope.entity.itemList.length;i++){
					$scope.entity.itemList[i].spec = JSON.parse( $scope.entity.itemList[i].spec );
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
	$scope.findSeckillOrderAll =function () {
		orderService.findSeckillOrderAll().success(function (response) {
			$scope.seckillOrderList=response;
        })
    }
});	
