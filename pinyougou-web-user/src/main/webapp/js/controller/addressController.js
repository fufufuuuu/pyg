//控制层
app.controller('addressController' ,function($scope,$controller ,addressService){
    $scope.entity = {};
    $scope.add=function() {
            //新增
            addressService.add($scope.entity).success(
                function(response){
                    alert(response.message);
                }
            )
    }

    $scope.findAddr= function(){
        userService.findAddr().success(function (response) {
            $scope.addressList=response;
        })
    };
})