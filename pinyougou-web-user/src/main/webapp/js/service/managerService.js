//服务层
app.service('managerService',function($http){
	//查询用户所有的地址信息
	this.findAll=function(){
		return $http.post('../manager/findByOne.do');
	}
});