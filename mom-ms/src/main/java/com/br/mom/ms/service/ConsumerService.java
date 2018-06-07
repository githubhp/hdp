package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ConsumerMapper;
import com.br.mom.ms.model.Consumer;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ConsumerService {

	@Autowired
	private ConsumerMapper consumerMapper;

	public List<Consumer> getConsumerByName(String name) {
		Consumer consumer = new Consumer();
		consumer.setName(name);
		return selectAllSelective(consumer);
	}

	public int insert(Consumer record) {
		consumerMapper.insert(record);
		return record.getId();
	}

	public int insertSelective(Consumer record) {
		consumerMapper.insertSelective(record);
		return record.getId();
	}

	public int deleteByPrimaryKey(Integer id) {
		return consumerMapper.deleteByPrimaryKey(id);
	}

	public int logicalDelete(Consumer record) {
		return consumerMapper.logicalDelete(record);
	}

	public int updateByPrimaryKey(Consumer record) {
		return consumerMapper.updateByPrimaryKey(record);
	}

	public int updateByPrimaryKeySelective(Consumer record) {
		return consumerMapper.updateByPrimaryKeySelective(record);
	}

	public Consumer selectSelective(Consumer record) {
		return consumerMapper.selectSelective(record);
	}

	public List<Consumer> selectAllSelective(Consumer record) {
		return consumerMapper.selectAllSelective(record);
	}

	public Consumer selectByPrimaryKey(Integer id) {
		return consumerMapper.selectByPrimaryKey(id);
	}

	public List<Consumer> selectByPage(int startIndex, int pageSize) {
		return consumerMapper.selectByPage(startIndex, pageSize);
	}

	public int selectTotalRecords() {
		return consumerMapper.selectTotalRecords();
	}
}
