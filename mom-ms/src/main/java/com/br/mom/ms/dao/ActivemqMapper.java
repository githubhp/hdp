package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.Activemq;

@Mapper
public interface ActivemqMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Activemq record);

    int insertSelective(Activemq record);

    Activemq selectByPrimaryKey(Integer id);

    Activemq selectSelective(Activemq record);

    List<Activemq> selectAllSelective(Activemq record);

    int updateByPrimaryKeySelective(Activemq record);

    int updateByPrimaryKey(Activemq record);
}
