package cn.itcast.core.service;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.AddressQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址
 */
@Service
@Transactional
public class AddressServiceImpl implements  AddressService {

    @Autowired
    private AddressDao addressDao;
    @Override
    public List<Address> findListByLoginUser(String name) {
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(name);
        return addressDao.selectByExample(addressQuery);
    }

    @Override
    public void delete(Long id) {
        addressDao.deleteByPrimaryKey(id);
    }

    @Override
    public void setDefault(String name,Long id) {
        //全改为非默认状态
        Address address = new Address();
        address.setId(id);
        address.setIsDefault("0");
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(name).andIsDefaultEqualTo("1");
        addressDao.updateByExampleSelective(address,addressQuery);

        address.setIsDefault("1");
        addressDao.updateByPrimaryKeySelective(address);
    }

    @Override
    public void update(Address tbAddress) {
        addressDao.updateByPrimaryKeySelective(tbAddress);
    }

    @Override
    public void add(Address tbAddress,String name) {
        tbAddress.setUserId(name);
        addressDao.insertSelective(tbAddress);
    }
}
