package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.Kafka;

@Mapper
public interface KafkaMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Kafka record);

    int insertSelective(Kafka record);

    Kafka selectByPrimaryKey(Integer id);

    Kafka selectSelective(Kafka record);

    List<Kafka> selectAllSelective(Kafka record);

    int updateByPrimaryKeySelective(Kafka record);

    int updateByPrimaryKey(Kafka record);
}
