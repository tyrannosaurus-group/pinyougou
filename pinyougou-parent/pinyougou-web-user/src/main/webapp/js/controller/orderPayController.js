//控制层
app.controller('orderPayController' ,function($scope,$controller,$location,$http,orderPayService){
    $controller('baseController',{$scope:$scope});//继承
    $controller('indexController',{$scope:$scope});
    //读取列表数据绑定到表单中index
    $scope.findAll=function(){
        orderPayService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    //分页
    $scope.findPage=function(page,rows){
        orderPayService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne=function(id){
        orderPayService.findOne(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }
    //查询实体
    $scope.showOrder=function(){
        var id=$location.search()['orderItemId'];
        orderPayService.findOrderItem(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }
    //查询实体
    $scope.findOrderById=function(){
        var id=$location.search()['orderId'];
        orderPayService.findOrderById(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }

    //保存
    $scope.save=function(){
        var serviceObject;//服务层对象
        if($scope.entity.id!=null){//如果有ID
            serviceObject=orderPayService.update( $scope.entity ); //修改
        }else{
            serviceObject=orderPayService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.flag){
                    //重新查询
                    $scope.reloadList();//重新加载
                }else{
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        orderPayService.dele( $scope.selectIds ).success(
            function(response){
                if(response.flag){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }
    //批量删除
    $scope.delPay=function(orderId){
        //获取选中的复选框
        alert(orderId)
        orderPayService.delPay(orderId).success(
            function(response){
                if(response.flag){
                    alert(response.message)
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }
    //付款
    $scope.payMoney = function(orderId){
        window.open("http://localhost:9103/pay.html#?orderId="+orderId);
        // location.href="http://localhost:9103/pay.html#?orderId="+orderId;
        /*orderPayService.payMoney(orderId).success(function(response){
            if(response.flag){
                alert(response.message);
                $scope.reloadList();//刷新列表
            }else{
                alert(response.message);
            }
        });*/
    }
    // 付款的方法:
    $scope.updateStatus = function(){
        orderPayService.updateStatus($scope.selectIds).success(function(response){
            if(response.flag){
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }
});
