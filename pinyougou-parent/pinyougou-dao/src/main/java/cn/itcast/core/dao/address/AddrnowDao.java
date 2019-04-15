package cn.itcast.core.dao.address;

import cn.itcast.core.pojo.address.Addrnow;
import cn.itcast.core.pojo.address.AddrnowQuery;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AddrnowDao {
    int countByExample(AddrnowQuery example);

    int deleteByExample(AddrnowQuery example);

    int deleteByPrimaryKey(BigDecimal id);

    int insert(Addrnow record);

    int insertSelective(Addrnow record);

    List<Addrnow> selectByExample(AddrnowQuery example);

    Addrnow selectByPrimaryKey(BigDecimal id);

    int updateByExampleSelective(@Param("record") Addrnow record, @Param("example") AddrnowQuery example);

    int updateByExample(@Param("record") Addrnow record, @Param("example") AddrnowQuery example);

    int updateByPrimaryKeySelective(Addrnow record);

    int updateByPrimaryKey(Addrnow record);
}