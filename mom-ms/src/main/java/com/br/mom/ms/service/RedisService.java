package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.RedisMapper;
import com.br.mom.ms.model.Redis;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class RedisService {

    @Autowired
    private RedisMapper redisMapper;

    public int deleteByPrimaryKey(Integer id) {
        return redisMapper.deleteByPrimaryKey(id);
    }

    public int insert(Redis record) {
        redisMapper.insert(record);
        return record.getId();
    }

    public int insertSelective(Redis record) {
        redisMapper.insertSelective(record);
        return record.getId();
    }

    public Redis selectByPrimaryKey(Integer id) {
        return redisMapper.selectByPrimaryKey(id);
    }

    public Redis selectSelective(Redis record) {
        return redisMapper.selectSelective(record);
    }

    public List<Redis> selectAllSelective(Redis record) {
        return redisMapper.selectAllSelective(record);
    }

    public int updateByPrimaryKeySelective(Redis record) {
        return redisMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Redis record) {
        return redisMapper.updateByPrimaryKey(record);
    }

}
