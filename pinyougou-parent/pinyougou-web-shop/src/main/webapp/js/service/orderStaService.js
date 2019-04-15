//服务层
app.service('orderStaService', function ($http) {

    //读取列表数据绑定到表单中
    this.findAll = function () {
        return $http.get('../order/findAll.do');
    }

    //搜索
    /*this.searchSta = function (page, rows, searchDate,searchEntity) {
        return $http.post('../order/searchSta.do?page=' + page + "&rows=" + rows+"&searchDate="+searchDate, searchEntity);
    }*/

    //搜索
    this.searchStatistics = function (page, rows, searchEntity) {
        return $http.post('../order/searchStatistics.do?page=' + page + "&rows=" + rows,searchEntity);
    }
});
