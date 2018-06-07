package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.br.mom.ms.model.ConsumerOffset;

@Mapper
public interface ConsumerOffsetMapper {

	int insert(ConsumerOffset record);

	int insertSelective(ConsumerOffset record);

	int deleteByPrimaryKey(Integer id);

	int updateByPrimaryKey(ConsumerOffset record);

	int updateByUpdateTime(ConsumerOffset record);

	int updateByPrimaryKeySelective(ConsumerOffset record);

	List<ConsumerOffset> selectByConsumerId(Integer consumerId);

	List<ConsumerOffset> selectByConsumerIdAndTime(ConsumerOffset consumerOffset);

	ConsumerOffset selectSelective(ConsumerOffset record);

	List<ConsumerOffset> selectAllSelective(ConsumerOffset record);

}
