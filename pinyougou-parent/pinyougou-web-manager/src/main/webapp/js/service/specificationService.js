//服务层
app.service('specificationService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../specification/findAll.do');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../specification/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../specification/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../specification/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../specification/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../specification/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../specification/search.do?page='+page+"&rows="+rows, searchEntity);
	}  
	
	this.selectOptionList=function(){
		return $http.get("../specification/selectOptionList.do");
	}
    this.updateStatus = function(ids,status){
        return $http.get('../specification/updateStatus.do?ids='+ids+"&status="+status);
    }

    // 上传图片
    this.uploadFile = function(){

        // 向后台传递数据:
        var formData = new FormData();


        // 向formData中添加数据:
        formData.append("file",file.files[0]);

        //<form    encod="multipart/formData"> 更改请求头

        //</form>


        return $http({
            method:'post',
            url:'http://localhost:9101/specification/export.do',
            data:formData,
            headers:{'Content-Type':undefined} ,// Content-Type : text/html  text/plain
            transformRequest: angular.identity
        });
    }
});
