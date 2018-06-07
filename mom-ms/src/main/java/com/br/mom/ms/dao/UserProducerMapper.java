package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.UserProducer;

@Mapper
public interface UserProducerMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserProducer record);

    int insertSelective(UserProducer record);

    UserProducer selectByPrimaryKey(Integer id);

    UserProducer selectSelective(UserProducer record);

    List<UserProducer> selectAllSelective(UserProducer record);

    int updateByPrimaryKeySelective(UserProducer record);

    int updateByPrimaryKey(UserProducer record);
}
