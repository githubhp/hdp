package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ConsumerOffsetMapper;
import com.br.mom.ms.model.ConsumerOffset;

@Service
public class ConsumerOffsetService {
	@Autowired
	private ConsumerOffsetMapper consumerOffsetMapper;

	public int insert(ConsumerOffset record) {
		consumerOffsetMapper.insert(record);
		return record.getId();
	}

	public int insertSelective(ConsumerOffset record) {
		consumerOffsetMapper.insertSelective(record);
		return record.getId();
	}

	public int deleteByPrimaryKey(Integer id) {
		return consumerOffsetMapper.deleteByPrimaryKey(id);
	}

	public int updateByPrimaryKey(ConsumerOffset record) {
		return consumerOffsetMapper.updateByPrimaryKey(record);
	}

	public int updateByUpdateTime(ConsumerOffset record) {
		return consumerOffsetMapper.updateByUpdateTime(record);
	}

	public int updateByPrimaryKeySelective(ConsumerOffset record) {
		return consumerOffsetMapper.updateByPrimaryKeySelective(record);
	}

	public List<ConsumerOffset> selectByConsumerId(Integer consumerId) {
		return consumerOffsetMapper.selectByConsumerId(consumerId);
	}

	public List<ConsumerOffset> selectByConsumerIdAndTime(ConsumerOffset consumerOffset) {
		return consumerOffsetMapper.selectByConsumerIdAndTime(consumerOffset);
	}

	public ConsumerOffset selectSelective(ConsumerOffset record) {
		return consumerOffsetMapper.selectSelective(record);
	}

	public List<ConsumerOffset> selectAllSelective(ConsumerOffset record) {
		return consumerOffsetMapper.selectAllSelective(record);
	}
}
