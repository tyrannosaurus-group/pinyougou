//首页控制器
app.controller('indexController',function($scope,loginService){
	$controller('baseController',{$scope:$scope});
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
});