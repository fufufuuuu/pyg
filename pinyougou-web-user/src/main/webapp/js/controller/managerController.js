//用户中心个人信息设置
app.controller('managerController',function($scope,managerService){
	//查询用户设置的所有地址
	$scope.findAll(function () {
		managerService.findAll().success(function (response) {
			$scope.list=response;
		})
	})
});