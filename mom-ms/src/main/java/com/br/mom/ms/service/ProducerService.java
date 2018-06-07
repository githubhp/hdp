package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ProducerMapper;
import com.br.mom.ms.model.Producer;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ProducerService {

	@Autowired
	private ProducerMapper producerMapper;

	public List<Producer> getProducerByName(String name) {
		Producer producer = new Producer();
		producer.setName(name);
		return selectAllSelective(producer);
	}

	public int insert(Producer record) {
		producerMapper.insert(record);
		return record.getId();
	}

	public int insertSelective(Producer record) {
		producerMapper.insertSelective(record);
		return record.getId();
	}

	public int deleteByPrimaryKey(Integer id) {
		return producerMapper.deleteByPrimaryKey(id);
	}

	public int logicalDelete(Producer record) {
		return producerMapper.logicalDelete(record);
	}

	public int updateByPrimaryKey(Producer record) {
		return producerMapper.updateByPrimaryKey(record);
	}

	public int updateByPrimaryKeySelective(Producer record) {
		return producerMapper.updateByPrimaryKeySelective(record);
	}

	public Producer selectSelective(Producer record) {
		return producerMapper.selectSelective(record);
	}

	public List<Producer> selectAllSelective(Producer record) {
		return producerMapper.selectAllSelective(record);
	}

	public Producer selectByPrimaryKey(Integer id) {
		return producerMapper.selectByPrimaryKey(id);
	}

	public List<Producer> selectByPage(int startIndex, int pageSize) {
		return producerMapper.selectByPage(startIndex, pageSize);
	}

	public int selectTotalRecords() {
		return producerMapper.selectTotalRecords();
	}
}
