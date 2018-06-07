package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ZookeeperMapper;
import com.br.mom.ms.model.Zookeeper;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ZookeeperService {

    @Autowired
    private ZookeeperMapper zookeeperMapper;

    public int deleteByPrimaryKey(Integer id) {
        return zookeeperMapper.deleteByPrimaryKey(id);
    }

    public int insert(Zookeeper record) {
        zookeeperMapper.insert(record);
        return record.getId();
    }

    public int insertSelective(Zookeeper record) {
        zookeeperMapper.insertSelective(record);
        return record.getId();
    }

    public Zookeeper selectByPrimaryKey(Integer id) {
        return zookeeperMapper.selectByPrimaryKey(id);
    }

    public Zookeeper selectSelective(Zookeeper record) {
        return zookeeperMapper.selectSelective(record);
    }

    public List<Zookeeper> selectAllSelective(Zookeeper record) {
        return zookeeperMapper.selectAllSelective(record);
    }

    public int updateByPrimaryKeySelective(Zookeeper record) {
        return zookeeperMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Zookeeper record) {
        return zookeeperMapper.updateByPrimaryKey(record);
    }

}
