package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ProducerStatusMapper;
import com.br.mom.ms.model.ProducerStatus;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ProducerStatusService {

	@Autowired
	private ProducerStatusMapper producerStatusMapper;

	public int deleteByPrimaryKey(Integer id) {
		return producerStatusMapper.deleteByPrimaryKey(id);
	}

	public int insert(ProducerStatus record) {
		producerStatusMapper.insert(record);
		return record.getId();
	}

	public int insertSelective(ProducerStatus record) {
		producerStatusMapper.insertSelective(record);
		return record.getId();
	}

	public List<ProducerStatus> selectSelective(ProducerStatus record) {
		return producerStatusMapper.selectSelective(record);
	}

	public List<ProducerStatus> selectAllSelective(ProducerStatus record) {
		return producerStatusMapper.selectAllSelective(record);
	}

	public ProducerStatus selectByPrimaryKey(Integer id) {
		return producerStatusMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ProducerStatus record) {
		return producerStatusMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(ProducerStatus record) {
		return producerStatusMapper.updateByPrimaryKey(record);
	}

	public void addEnqueue(ProducerStatus record) {
		producerStatusMapper.addEnqueue(record);
	}

	public List<ProducerStatus> selectByPage(int startIndex, int pageSize) {
		return producerStatusMapper.selectByPage(startIndex, pageSize);
	}

	public int selectTotalRecords() {
		return producerStatusMapper.selectTotalRecords();
	}
}
