package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.br.mom.ms.model.ConsumerStatus;

@Mapper
public interface ConsumerStatusMapper {

	int insert(ConsumerStatus record);

	int insertSelective(ConsumerStatus record);

	int deleteByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ConsumerStatus record);

	int updateByPrimaryKey(ConsumerStatus record);

	void addDequeue(ConsumerStatus record);

	ConsumerStatus selectByPrimaryKey(Integer id);

	List<ConsumerStatus> selectSelective(ConsumerStatus record);

	List<ConsumerStatus> selectAllSelective(ConsumerStatus record);

	List<ConsumerStatus> selectByConsumerId(Integer consumerId);

	ConsumerStatus selectByConsumerIdAndTime(ConsumerStatus record);

	List<ConsumerStatus> selectByConsumerIdAndDateTime(ConsumerStatus record);

	List<ConsumerStatus> selectByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

	int selectTotalRecords();
}
