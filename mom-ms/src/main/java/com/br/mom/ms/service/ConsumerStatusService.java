package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ConsumerStatusMapper;
import com.br.mom.ms.model.ConsumerStatus;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ConsumerStatusService {

	@Autowired
	private ConsumerStatusMapper consumerStatusMapper;

	public int deleteByPrimaryKey(Integer id) {
		return consumerStatusMapper.deleteByPrimaryKey(id);
	}

	public int insert(ConsumerStatus record) {
		consumerStatusMapper.insert(record);
		return record.getId();
	}

	public int insertSelective(ConsumerStatus record) {
		consumerStatusMapper.insertSelective(record);
		return record.getId();
	}

	public List<ConsumerStatus> selectSelective(ConsumerStatus record) {
		return consumerStatusMapper.selectSelective(record);
	}

	public List<ConsumerStatus> selectAllSelective(ConsumerStatus record) {
		return consumerStatusMapper.selectAllSelective(record);
	}

	public ConsumerStatus selectByPrimaryKey(Integer id) {
		return consumerStatusMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ConsumerStatus record) {
		return consumerStatusMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(ConsumerStatus record) {
		return consumerStatusMapper.updateByPrimaryKey(record);
	}

	public void addDequeue(ConsumerStatus record) {
		consumerStatusMapper.addDequeue(record);
	}

	public List<ConsumerStatus> selectByConsumerId(Integer consumerId) {
		return consumerStatusMapper.selectByConsumerId(consumerId);
	}

	public ConsumerStatus selectByConsumerIdAndTime(ConsumerStatus record) {
		return consumerStatusMapper.selectByConsumerIdAndTime(record);
	}

	public List<ConsumerStatus> selectByPage(int startIndex, int pageSize) {
		return consumerStatusMapper.selectByPage(startIndex, pageSize);
	}

	public List<ConsumerStatus> selectByConsumerIdAndDateTime(ConsumerStatus record) {
		return consumerStatusMapper.selectByConsumerIdAndDateTime(record);
	}

	public int selectTotalRecords() {
		return consumerStatusMapper.selectTotalRecords();
	}
}
