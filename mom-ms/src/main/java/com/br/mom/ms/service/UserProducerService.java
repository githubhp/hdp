package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.UserProducerMapper;
import com.br.mom.ms.model.UserProducer;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class UserProducerService {

    @Autowired
    private UserProducerMapper userProducerMapper;

    public int deleteByPrimaryKey(Integer id) {
        return userProducerMapper.deleteByPrimaryKey(id);
    }

    public int insert(UserProducer record) {
        userProducerMapper.insert(record);
        return record.getId();
    }

    public int insertSelective(UserProducer record) {
        userProducerMapper.insertSelective(record);
        return record.getId();
    }

    public UserProducer selectByPrimaryKey(Integer id) {
        return userProducerMapper.selectByPrimaryKey(id);
    }

    public UserProducer selectSelective(UserProducer record) {
        return userProducerMapper.selectSelective(record);
    }

    public List<UserProducer> selectAllSelective(UserProducer record) {
        return userProducerMapper.selectAllSelective(record);
    }

    public int updateByPrimaryKeySelective(UserProducer record) {
        return userProducerMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(UserProducer record) {
        return userProducerMapper.updateByPrimaryKey(record);
    }

}
