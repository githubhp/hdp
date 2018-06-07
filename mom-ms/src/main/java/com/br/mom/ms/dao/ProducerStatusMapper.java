package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.br.mom.ms.model.ProducerStatus;

@Mapper
public interface ProducerStatusMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(ProducerStatus record);

	int insertSelective(ProducerStatus record);

	List<ProducerStatus> selectSelective(ProducerStatus record);

	List<ProducerStatus> selectAllSelective(ProducerStatus record);

	ProducerStatus selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ProducerStatus record);

	int updateByPrimaryKey(ProducerStatus record);

	void addEnqueue(ProducerStatus record);

	List<ProducerStatus> selectByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

	int selectTotalRecords();
}
