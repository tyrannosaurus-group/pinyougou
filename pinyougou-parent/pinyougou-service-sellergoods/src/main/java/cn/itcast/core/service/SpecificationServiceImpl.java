package cn.itcast.core.service;

import cn.itcast.common.utils.ImportExcelUtil;
import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.pojo.good.BrandQuery;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;

/**
 * 规格管理
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationDao specificationDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;

    //查询分页 有条件
    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        //插件
        PageHelper.startPage(page,rows);
        SpecificationQuery specificationQuery = new SpecificationQuery();
        SpecificationQuery.Criteria criteria = specificationQuery.createCriteria();
        if (null !=specification.getSpecName() &&!"".equals(specification.getSpecName().trim())){
            criteria.andSpecNameLike("%"+specification.getSpecName().trim()+"%");
        }

        Page<Specification> p = (Page<Specification>) specificationDao.selectByExample(specificationQuery);


        return new PageResult(p.getTotal(),p.getResult());
    }

    //添加
    @Override
    public void add(SpecificationVo specificationVo) {
        specificationVo.getSpecification().setStatus("0");

        //规格表
        specificationDao.insertSelective(specificationVo.getSpecification());

        //规格选项表 多
        List<SpecificationOption> specificationOptionList = specificationVo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            //外键
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        }

    }

    //查询一个规格
    @Override
    public SpecificationVo findOne(Long id) {
        SpecificationVo vo = new SpecificationVo();
        //规格
        vo.setSpecification(specificationDao.selectByPrimaryKey(id));
        //规格选项结果集
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(id);
        vo.setSpecificationOptionList(specificationOptionDao.selectByExample(query));
        return vo;
    }

    //修改

    @Override
    public void update(SpecificationVo specificationVo) {
        specificationVo.getSpecification().setStatus("0");
        //规格表 修改
        specificationDao.updateByPrimaryKeySelective(specificationVo.getSpecification());
        //规格选项表
        //1:删除  通过外键 批量删除
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(specificationVo.getSpecification().getId());
        specificationOptionDao.deleteByExample(query);
        //2:添加
        List<SpecificationOption> specificationOptionList = specificationVo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            //外键
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        }

    }

    @Override
    public List<Map> selectOptionList() {
        return specificationDao.selectOptionList();
    }
    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            Specification specification = specificationDao.selectByPrimaryKey(id);
            specification.setStatus(status);
            specificationDao.updateByPrimaryKeySelective(specification);

        }
    }


    @Override
    public void importData( List<List<Object>>  rowlist) {
       // List<List<Object>> rowlist = null;
        try {
         //   rowlist = ImportExcelUtil.getBankListByExcel(file.getInputStream(), file.getOriginalFilename());

            if (null!=rowlist&&rowlist.size()>0){


                for (int i = 0; i < rowlist.size(); i++) {
                    //第i行的数据
                    List<Object> row = rowlist.get(i);
                    Specification specification = new Specification();
                    // 这里就是第i行第1列的数据了.
                    Object object0 = row.get(0);
                    if (object0!= null) {
                        String countString=String.valueOf(object0);
                        Long id=Long.valueOf(countString);
                        specification.setId(id);
                    }


                    Object object1 = row.get(1);
                    if (object1!= null) {

                        specification.setSpecName(String.valueOf(object1));
                    }




                    Object object2 = row.get(2);
                    if (object2!= null) {

                        specification.setStatus(String.valueOf(object2));
                    }
                       specificationDao.insertSelective(specification);
                    /*...执行数据库操作*/

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //批量删除规格
    @Override
    public void delete(Long[] ids) {
        //删除规格
        for (Long id : ids) {
            specificationDao.deleteByPrimaryKey(id);
            //删除规格属性
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            query.createCriteria().andSpecIdEqualTo(id);
            specificationOptionDao.deleteByExample(query);
        }



    }
}
