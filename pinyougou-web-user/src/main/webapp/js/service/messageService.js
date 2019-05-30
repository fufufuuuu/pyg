//服务层
app.service('messageService',function($http){
	//发送用户信息
	this.update=function(entity){
		return $http.post('/message/update.do?user='+user);
	}

	//查询回显用户信息
	this.findByOne=function(){
		return $http.post('/message/findByOne');
	}
	
});