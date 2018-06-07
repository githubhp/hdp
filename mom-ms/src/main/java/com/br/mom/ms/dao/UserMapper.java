package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.User;

@Mapper
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectSelective(User record);

    List<User> selectAllSelective(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
