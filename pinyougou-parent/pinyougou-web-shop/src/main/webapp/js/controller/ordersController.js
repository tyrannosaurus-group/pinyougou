//控制层
app.controller('ordersController' ,function($scope,$controller,$location,typeTemplateService ,itemCatService,uploadService ,goodsService,ordersService){

    $controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中
    $scope.findCount=function(){
        ordersService.findCount().success(
            function(response){
                $scope.count=response;
            }
        );
    }

    //分页
    $scope.findPage=function(page,rows){
        goodsService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

});
