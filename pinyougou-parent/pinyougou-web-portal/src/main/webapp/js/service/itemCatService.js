//商品分类服务层
app.service('itemCatService',function($http){
	//查询商品分类列表
	this.findItemCatList=function(){
		return $http.get('itemCat/findItemCatList.do');
	}
});