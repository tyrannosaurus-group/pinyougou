app.service('payService',function($http){
	//本地支付

	this.createNative=function(){
		return $http.get('pay/createNative.do');
	}
	this.createNativeById=function(orderId){
		return $http.get('pay/createNativeById.do?orderId='+orderId);
	}
	
	//查询支付状态
	this.queryPayStatus=function(out_trade_no){
		return $http.get('pay/queryPayStatus.do?out_trade_no='+out_trade_no);
	}
});