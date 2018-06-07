package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.br.mom.ms.model.Producer;

@Mapper
public interface ProducerMapper {

	int insert(Producer record);

	int insertSelective(Producer record);

	int deleteByPrimaryKey(Integer id);

	int logicalDelete(Producer record);

	int updateByPrimaryKey(Producer record);

	int updateByPrimaryKeySelective(Producer record);

	Producer selectSelective(Producer record);

	List<Producer> selectAllSelective(Producer record);

	Producer selectByPrimaryKey(Integer id);

	List<Producer> selectByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

	int selectTotalRecords();

}
