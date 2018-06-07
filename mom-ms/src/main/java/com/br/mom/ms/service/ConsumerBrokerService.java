package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ConsumerBrokerMapper;
import com.br.mom.ms.model.ConsumerBroker;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ConsumerBrokerService {

    @Autowired
    private ConsumerBrokerMapper consumerBrokerMapper;

    public int deleteByPrimaryKey(Integer id) {
        return this.consumerBrokerMapper.deleteByPrimaryKey(id);
    }

    public int insert(ConsumerBroker record) {
        this.consumerBrokerMapper.insert(record);
        return record.getId();
    }

    public int insertSelective(ConsumerBroker record) {
        this.consumerBrokerMapper.insertSelective(record);
        return record.getId();
    }

    public ConsumerBroker selectSelective(ConsumerBroker record) {
        return this.consumerBrokerMapper.selectSelective(record);
    }

    public List<ConsumerBroker> selectAllSelective(ConsumerBroker record) {
        return this.consumerBrokerMapper.selectAllSelective(record);
    }

    public ConsumerBroker selectByPrimaryKey(Integer id) {
        return this.consumerBrokerMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(ConsumerBroker record) {
        return this.consumerBrokerMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ConsumerBroker record) {
        return this.consumerBrokerMapper.updateByPrimaryKey(record);
    }

}
