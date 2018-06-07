package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.br.mom.ms.model.Queue;

@Mapper
public interface QueueMapper {

	int insert(Queue record);

	int insertSelective(Queue record);

	int deleteByPrimaryKey(Integer id);

	int logicalDelete(Queue record);

	int updateByPrimaryKey(Queue record);

	int updateByPrimaryKeySelective(Queue record);

	Queue selectSelective(Queue record);

	List<Queue> selectAllSelective(Queue record);

	Queue selectByPrimaryKey(Integer id);

	List<Queue> selectByPage(@Param("startIndex") int startIndex,@Param("pageSize") int pageSize);

	int selectTotalRecords();
}
