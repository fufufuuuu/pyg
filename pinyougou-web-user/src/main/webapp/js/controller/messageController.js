//用户中心个人信息设置
app.controller('messageController',function($scope,messageService){
	$scope.user={}
	//修改用户信息
	$scope.update(function () {
		setService.update($scope.user).success(function (response) {
			$scope.user=response;
		})
	})

	//查询用户的基本信息
	$scope.findByOne(function () {
		setService.findByOne().success(function (response) {
			$scope.user=response;
		})
	})
});