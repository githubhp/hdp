package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ConsumerThresholdMapper;
import com.br.mom.ms.model.ConsumerThreshold;

@Service
public class ConsumerThresholdService {
	@Autowired
	private ConsumerThresholdMapper ConsumerThresholdMapper;

	public int deleteByPrimaryKey(Integer id) {
		return ConsumerThresholdMapper.deleteByPrimaryKey(id);
	}

	public int insertSelective(ConsumerThreshold record) {
		ConsumerThresholdMapper.insertSelective(record);
		return record.getId();
	}

	public List<ConsumerThreshold> selectAllSelective(ConsumerThreshold record) {
		return ConsumerThresholdMapper.selectAllSelective(record);
	}

	public ConsumerThreshold selectByPrimaryKey(Integer id) {
		return ConsumerThresholdMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ConsumerThreshold record) {
		return ConsumerThresholdMapper.updateByPrimaryKeySelective(record);
	}

	public List<ConsumerThreshold> selectByPage(int startIndex, int pageSize) {
		return ConsumerThresholdMapper.selectByPage(startIndex, pageSize);
	}

	public int selectTotalRecords() {
		return ConsumerThresholdMapper.selectTotalRecords();
	}
}
