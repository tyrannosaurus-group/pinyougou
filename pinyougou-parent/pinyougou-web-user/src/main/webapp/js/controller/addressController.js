//地址控制器
app.controller('addressController',function($scope,loginService,addressService){
    $scope.showAddress=function(){
        addressService.showAddress().success(
            function(response){
                alert("1111")
                $scope.list=response;
            }
        );
    }
    $scope.delete=function(){
        addressService.delete(id).success(
            function(response){
                alert(response.message);
            }
        );
    }
    $scope.setDefault=function(){
        addressService.setDefault(id).success(
            function(response){
                alert(response.message);
            }
        );
    }
    $scope.findById=function(){
        addressService.findById(id).success(
            function(response){
                alert("idid")
                $scope.tbAddress=response;
            }
        );
    }
    $scope.save=function(){
        addressService.update($scope.tbAddress).success(
            function(response){
                alert(response.message);
            }
        );
    }
    // 保存地址的方法:
    $scope.save = function(){
        // 区分是保存还是修改
        var object;
        if($scope.tbAddress.id != null){
            // 更新
            object = addressService.update($scope.tbAddress);
        }else{
            // 保存
            object = addressService.add($scope.tbAddress);
        }
        object.success(function(response){
            // {flag:true,message:xxx}
            // 判断保存是否成功:
            if(response.flag){
                // 保存成功
                alert(response.message);
                location.reload();
            }else{
                // 保存失败
                alert(response.message);
            }
        });
    }
});