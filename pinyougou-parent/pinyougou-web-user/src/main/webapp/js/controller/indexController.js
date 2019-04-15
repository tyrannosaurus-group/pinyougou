//首页控制器
app.controller('indexController',function($scope,$controller,$http,loginService){

	$scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	}
	$scope.findList=function () {
		loginService.findList().success(
			function (response) {
				$scope.collectList=response;
            }
		)
    }
    $scope.findSeckilList=function () {
		loginService.findSeckilList().success(
			function (responese) {
				$scope.seckillList=responese;
                seckillList.getGoodsDesc.setCustomAttributeItems=JSON.parse(seckillList.getGoodsDesc.getCustomAttributeItems)
            }
		)

    }
});