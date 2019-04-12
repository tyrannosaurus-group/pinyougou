app.service('searchService',function($http){
	

	//搜索 按钮  执行方法
	this.search=function(searchMap){
		return $http.post('itemsearch/search.do',searchMap);
	}
	
});