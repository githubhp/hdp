package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.br.mom.ms.model.Consumer;

@Mapper
public interface ConsumerMapper {

	int insert(Consumer record);

	int insertSelective(Consumer record);

	int deleteByPrimaryKey(Integer id);

	int logicalDelete(Consumer record);

	int updateByPrimaryKey(Consumer record);

	int updateByPrimaryKeySelective(Consumer record);

	Consumer selectSelective(Consumer record);

	List<Consumer> selectAllSelective(Consumer record);

	Consumer selectByPrimaryKey(Integer id);

	List<Consumer> selectByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

	int selectTotalRecords();

}
