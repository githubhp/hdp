package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.Console;

@Mapper
public interface ConsoleMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Console record);

    int insertSelective(Console record);

    Console selectByPrimaryKey(Integer id);

    Console selectSelective(Console record);

    List<Console> selectAllSelective(Console record);

    int updateByPrimaryKeySelective(Console record);

    int updateByPrimaryKey(Console record);
}
