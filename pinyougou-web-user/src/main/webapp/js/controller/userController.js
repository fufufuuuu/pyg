 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){

	$scope.entity = {};
	// 表单数据
    $scope.formData = {};
    $scope.processForm = function() {
        userService.addAddress($scope.formData).success(function (response) {
            if (response.flag == 'true') {
                alert(response.message)
            }else {
                alert(response.message)
            }
        })
    };
	//注册用户
	$scope.reg=function(){
		
		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				alert(response.message);
			}		
		);
	}

    $scope.changePassword=function(){

		userService.updatePassword($scope.entity.OldPassword,$scope.entity.password).success(
			function(response){
                    //比较两次输入的密码是否一致
                    if($scope.password!=$scope.entity.password){
                        alert("两次输入密码不一致，请重新输入");
                        $scope.entity.password="";
                        $scope.password="";
                        return ;
                    }
                alert(response.message);
			}

		);
    }
    //刷新图形验证码
	$scope.refresh=function () {
        $scope.regUrl = "user/verifyImage.do?gen=" + Math.random()
    }

	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		
		userService.sendCode($scope.entity.phone ).success(
			function(response){
				alert(response.message);
			}
		);		
	};

	$scope.findAddr= function(){
		userService.findAddr().success(function (response) {
			$scope.addressList=response;
		})
	};
});	
