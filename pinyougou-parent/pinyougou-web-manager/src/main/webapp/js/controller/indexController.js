app.controller("indexController",function($scope,loginService){
	//显示当前登陆人名称
	$scope.showName = function(){
		loginService.showName().success(function(response){
			$scope.loginName = response.username;// map
			$scope.time = response.curTime;
		});
	}
	
});