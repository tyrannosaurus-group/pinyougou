//地址控制器
app.controller('addressController',function($scope,$controller,loginService,addressService){
    $controller('indexController',{$scope:$scope});//继承


    $scope.showAddress=function(){
        addressService.showAddress().success(
            function(response){
                $scope.list=response;
            }
        );
    }
    $scope.delete=function(id){
        addressService.delete(id).success(
            function(response){
                alert(response.message);
                location.reload();
            }
        );
    }
    $scope.setDefault=function(id){
        addressService.setDefault(id).success(
            function(response){
                alert(response.message);
                location.reload();
            }
        );
    }
    $scope.findById=function(id){
        addressService.findById(id).success(
            function(response){
                $scope.entity=response;
            }
        );

    }
    /*$scope.showCitys=function(){
        addressService.update($scope.entity).success(
            function(response){
                alert(response.message);
            }
        );
    }*/
    $scope.add=function(){
        addressService.add($scope.tbAddress).success(
            function(response){
                if(response.flag){
                    // 保存成功
                    alert(response.message);
                    location.reload();
                }else{
                    // 保存失败
                    alert(response.message);
                }
            }
        );
    }
    $scope.update=function() {
        addressService.update($scope.entity).success(
            function (response) {
                if (response.flag) {
                    // 保存成功
                    alert(response.message);
                    location.reload();
                } else {
                    // 保存失败
                    alert(response.message);
                }
            }
        );
    }
});