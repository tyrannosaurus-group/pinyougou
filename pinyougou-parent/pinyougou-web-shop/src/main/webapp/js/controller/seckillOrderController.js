//控制层
app.controller('seckillOrderController' ,function($scope,$controller,$location,typeTemplateService ,itemCatService,uploadService ,seckillOrderService){

    $controller('baseController',{$scope:$scope});//继承

    //搜索
    $scope.search=function(page,rows){
        seckillOrderService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    $scope.searchEntity={};//定义搜索对象

    $scope.status = ["未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];
});