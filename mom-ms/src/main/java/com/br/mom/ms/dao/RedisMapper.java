package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.Redis;

@Mapper
public interface RedisMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Redis record);

    int insertSelective(Redis record);

    Redis selectByPrimaryKey(Integer id);

    Redis selectSelective(Redis record);

    List<Redis> selectAllSelective(Redis record);

    int updateByPrimaryKeySelective(Redis record);

    int updateByPrimaryKey(Redis record);
}
