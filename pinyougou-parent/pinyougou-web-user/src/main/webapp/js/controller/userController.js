 //控制层 
app.controller('userController' ,function($scope,$controller ,uploadService ,userService){
	$controller('baseController',{$scope:$scope});
	$scope.entity = {};
	//注册用户
	$scope.reg=function(){
		
		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				alert(response.message);
			}		
		);
	}
    
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}//
		//商家后台
		//运营商后台 能前端就前端判断 后端不判断  员工用
		//网站前台  消费者 前端 美  后端 安全
		
		userService.sendCode($scope.entity.phone  ).success(
			function(response){
				alert(response.message);
			}
		);		
	}
	$scope.info={};


	//上传图片
	$scope.uploadFile = function () {
		// 调用uploadService的方法完成文件的上传
		uploadService.uploadFile().success(function (response) {
			if (response.flag) {
				// 获得url  image_entity = {color:黑色,url:http://192.......jpg}
				$scope.info.headPic = response.message;
			} else {
				alert(response.message);
			}
		});
	}
	//传数据
	$scope.sendInfo = function () {
		// 调用uploadService的方法完成文件的上传
		$scope.info.birthday = $scope.year+"-"+$scope.month+"-"+$scope.day;
		userService.sendInfo($scope.info).success(function (response) {
			if (response.flag) {
				alert(response.message);
			} else {
				alert(response.message);
			}
		});
	}
	
});	
