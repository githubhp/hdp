package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.ConsumerBroker;

@Mapper
public interface ConsumerBrokerMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ConsumerBroker record);

    int insertSelective(ConsumerBroker record);

    ConsumerBroker selectSelective(ConsumerBroker record);

    List<ConsumerBroker> selectAllSelective(ConsumerBroker record);

    ConsumerBroker selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsumerBroker record);

    int updateByPrimaryKey(ConsumerBroker record);
}
