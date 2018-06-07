package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ActivemqMapper;
import com.br.mom.ms.model.Activemq;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ActivemqService {

    @Autowired
    private ActivemqMapper activemqMapper;

    public int deleteByPrimaryKey(Integer id) {
        return activemqMapper.deleteByPrimaryKey(id);
    }

    public int insert(Activemq record) {
        activemqMapper.insert(record);
        return record.getId();
    }

    public int insertSelective(Activemq record) {
        activemqMapper.insertSelective(record);
        return record.getId();
    }

    public Activemq selectByPrimaryKey(Integer id) {
        return activemqMapper.selectByPrimaryKey(id);
    }

    public Activemq selectSelective(Activemq record) {
        return activemqMapper.selectSelective(record);
    }

    public List<Activemq> selectAllSelective(Activemq record) {
        return activemqMapper.selectAllSelective(record);
    }

    public int updateByPrimaryKeySelective(Activemq record) {
        return activemqMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Activemq record) {
        return activemqMapper.updateByPrimaryKey(record);
    }

}
