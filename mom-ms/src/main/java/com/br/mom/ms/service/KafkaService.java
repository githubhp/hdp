package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.KafkaMapper;
import com.br.mom.ms.model.Kafka;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class KafkaService {

    @Autowired
    private KafkaMapper kafkaMapper;

    public int deleteByPrimaryKey(Integer id) {
        return kafkaMapper.deleteByPrimaryKey(id);
    }

    public int insert(Kafka record) {
        kafkaMapper.insert(record);
        return record.getId();
    }

    public int insertSelective(Kafka record) {
        kafkaMapper.insertSelective(record);
        return record.getId();
    }

    public Kafka selectByPrimaryKey(Integer id) {
        return kafkaMapper.selectByPrimaryKey(id);
    }

    public Kafka selectSelective(Kafka record) {
        return kafkaMapper.selectSelective(record);
    }

    public List<Kafka> selectAllSelective(Kafka record) {
        return kafkaMapper.selectAllSelective(record);
    }

    public int updateByPrimaryKeySelective(Kafka record) {
        return kafkaMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Kafka record) {
        return kafkaMapper.updateByPrimaryKey(record);
    }

}
