app.controller('payController' ,function($scope ,$location,payService){
	
	//生成二维码
	$scope.createNative=function(){
		//商家后台 系统
		//2:调统一下单API
		//返回值：code_url
		payService.createNative().success(
			function(response){
				//Map  三个值
				//总金额 分
				// 订单号
				//code_url
				
				//显示订单号和金额
				$scope.money= (response.total_fee/100).toFixed(2);
				$scope.out_trade_no=response.out_trade_no;
				
				//生成二维码
				 var qr=new QRious({
					    element:document.getElementById('qrious'),
						size:250,
						value:response.code_url,// value ： 常量  静态二维码   现在是变量 动态码 金额是固定
						level:'H'
			     });
				 
				 queryPayStatus();//调用查询
				
			}	
		);	
	}
	
	//调用查询  支付订单ID
	queryPayStatus=function(){
		payService.queryPayStatus($scope.out_trade_no).success(
			function(response){
				if(response.flag){
					location.href="paysuccess.html#?money="+$scope.money;
				}else{
					if(response.message=='二维码超时'){
						$scope.createNative();//重新生成二维码
					}else{
						location.href="payfail.html";
					}
				}				
			}		
		);		
	}
	
	//获取金额
	$scope.getMoney=function(){
		return $location.search()['money'];
	}
	
});