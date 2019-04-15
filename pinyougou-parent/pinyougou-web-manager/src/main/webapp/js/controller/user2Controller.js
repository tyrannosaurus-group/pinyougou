// 定义控制器:
app.controller("user2Controller",function($scope,$controller,$http,user2Service){
	// AngularJS中的继承:伪继承
	$controller('baseController',{$scope:$scope});

    $scope.userAnalyze = function(){
        // 向后台发送请求:
        user2Service.userAnalyze().success(function(response){
            $scope.entity = response;
        });
    }

});
