package cn.itcast.core.service;

import cn.itcast.common.utils.ImportExcelUtil;
import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import cn.itcast.core.pojo.specification.Specification;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 品牌管理
 */
@Service
public class BrandServiceImpl implements BrandService{

    //test1
    //直接 Controller Service
    @Autowired
    private BrandDao brandDao;


    @Override
    public List<Brand> findAll() {
        // 5个字段
        // update tb_tb set name = #{name} where ... 选择性
        // update tb_tb set name = #{name} , age = null, where ... 选择性
        //
        return brandDao.selectByExample(null);
    }
    //查询分页对象
    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize) {

        //Mybatis分页插件
        PageHelper.startPage(pageNo,pageSize);
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);
        //PageInfo<Brand> info = new PageInfo<>(brandList);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //添加
    @Override
    public void add(Brand brand) {
        //默认刚添加都未审核
        brand.setStatus("0");
        brandDao.insertSelective(brand);
                //insert into tb_tb (id,name,98个都是null) values (1,haha,null,null 98个null           100个字段
                //insert into tb_tb (id,name) values (1,haha)
    }

    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    //修改
    @Override
    public void update(Brand brand) {
        //更改过后要重新审核
        brand.setStatus("0");
        brandDao.updateByPrimaryKeySelective(brand);
    }

    //删除
    @Override
    public void delete(Long[] ids) {
      /*  for (Long id : ids) {
            brandDao.deleteByPrimaryKey(id);
        }*/
        //delete from tb_brand where id = 5;

        BrandQuery brandQuery = new BrandQuery();//入参

        //条件对象中 内部对象  保存条件  放在  where id in (3,5,7,1)
        brandQuery.createCriteria().andIdIn(Arrays.asList(ids));
        brandDao.deleteByExample(brandQuery);

        //delete from tb_brand where id in (3,5,7,1)
    }

    //查询分页对象  有条件
    @Override
    public PageResult search(Integer pageNo, Integer pageSize, Brand brand) {
        //Mybatis分页插件
        PageHelper.startPage(pageNo,pageSize);
        //条件对象
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();

        //判断品牌中 名称是否为空
        if(null != brand.getName() && !"".equals(brand.getName().trim())){
            criteria.andNameLike("%"+brand.getName().trim()+"%");
        }
        //首字母
        if(null != brand.getFirstChar() && !"".equals(brand.getFirstChar().trim())){
            criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
        }

        //分页对象
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);


        return new PageResult(page.getTotal(),page.getResult());


    }

    //查询所有品牌
    @Override
    public List<Map> selectOptionList() {
        return brandDao.selectOptionList();
    }

    @Override
    public void updateStatus(Long[] ids,String status) {
        for (Long id : ids) {
            Brand brand = brandDao.selectByPrimaryKey(id);
            brand.setStatus(status);
            brandDao.updateByPrimaryKeySelective(brand);
        }
    }










}
