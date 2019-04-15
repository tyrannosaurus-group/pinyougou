//服务层
app.service('orderService', function ($http) {

    //读取列表数据绑定到表单中
    this.findAll = function () {
        return $http.get('../order/findAll.do');
    }
    //分页
    this.findPage = function (page, rows) {
        return $http.get('../order/findPage.do?page=' + page + '&rows=' + rows);
    }
    //查询实体
    this.findOne = function (id) {
        return $http.get('../order/findOne.do?id=' + id);
    }

    //搜索
    this.search = function (page, rows, searchDate,searchEntity) {
        return $http.post('../order/search.do?page=' + page + "&rows=" + rows+"&searchDate="+searchDate, searchEntity);
    }

    //更改状态  订单发货
    this.changeStatus = function (id) {
        return $http.get('../order/changeStatus.do?id='+id);
    }
});
