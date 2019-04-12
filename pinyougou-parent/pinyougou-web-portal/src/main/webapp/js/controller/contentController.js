app.controller("contentController",function($scope,contentService){

	//首页的所有广告
	$scope.contentList = [];

	// 根据分类ID查询广告的方法:  categoryId ： 1
	$scope.findByCategoryId = function(categoryId){

		contentService.findByCategoryId(categoryId).success(function(response){

			$scope.contentList[categoryId] = response;//List<Content> 广告结果集
		});
	}
	
	//搜索  （传递参数）
	$scope.search=function(){
		location.href="http://localhost:9103/search.html#?keywords="+$scope.keywords;
	}
	
});