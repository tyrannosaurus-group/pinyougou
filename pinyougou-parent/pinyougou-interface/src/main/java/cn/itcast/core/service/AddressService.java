package cn.itcast.core.service;

import cn.itcast.core.pojo.address.Address;

import java.util.List;

public interface AddressService {
    List<Address> findListByLoginUser(String name);

    void delete(Long id);

    void setDefault(String name,Long id);

    void update(Address tbAddress);

    void add(Address tbAddress,String name);
}
