//首页控制器
app.controller('indexController',function($scope,$controller,$http,loginService){
	$controller('baseController',{$scope:$scope});
    $controller('baseController',{$scope:$scope});//继承

	$scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	}
});