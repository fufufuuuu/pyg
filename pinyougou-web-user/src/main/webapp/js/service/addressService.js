//服务层
app.service('addressService',function($http){
    this.addAddress=function(entity){
        return  $http.post('../add/addAddress.do',entity );
    }
    // 回显地址
    this.findAddr=function () {
        return $http.get('../addr/findAddr.do');
    }
})