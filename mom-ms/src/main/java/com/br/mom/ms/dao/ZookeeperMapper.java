package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.Zookeeper;

@Mapper
public interface ZookeeperMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Zookeeper record);

    int insertSelective(Zookeeper record);

    Zookeeper selectByPrimaryKey(Integer id);

    Zookeeper selectSelective(Zookeeper record);

    List<Zookeeper> selectAllSelective(Zookeeper record);

    int updateByPrimaryKeySelective(Zookeeper record);

    int updateByPrimaryKey(Zookeeper record);
}
