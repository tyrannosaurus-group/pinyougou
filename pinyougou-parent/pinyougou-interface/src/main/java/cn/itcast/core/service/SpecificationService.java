package cn.itcast.core.service;

import cn.itcast.core.pojo.specification.Specification;
import entity.PageResult;
import org.springframework.web.multipart.MultipartFile;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;

public interface SpecificationService {
    PageResult search(Integer page, Integer rows, Specification specification);

    void add(SpecificationVo specificationVo);

    SpecificationVo findOne(Long id);

    void update(SpecificationVo specificationVo);

    List<Map> selectOptionList();

    void updateStatus(Long[] ids, String status);

    void delete(Long[] ids);

    void importData( List<List<Object>>  rowlist);
}
