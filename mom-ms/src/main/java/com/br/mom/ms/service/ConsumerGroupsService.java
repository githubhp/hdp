package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ConsumerGroupsMapper;
import com.br.mom.ms.model.ConsumerGroups;

@Service
public class ConsumerGroupsService {
	@Autowired
	private ConsumerGroupsMapper consumerGroupsMapper;

	public int deleteByPrimaryKey(Integer id) {
		return consumerGroupsMapper.deleteByPrimaryKey(id);
	}

	public int insertSelective(ConsumerGroups consumerGroups) {
		consumerGroupsMapper.insertSelective(consumerGroups);
		return consumerGroups.getId();
	}

	public List<ConsumerGroups> selectAllSelective(ConsumerGroups consumerGroups) {
		return consumerGroupsMapper.selectAllSelective(consumerGroups);
	}

	public ConsumerGroups selectByPrimaryKey(Integer id) {
		return consumerGroupsMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ConsumerGroups consumerGroups) {
		return consumerGroupsMapper.updateByPrimaryKeySelective(consumerGroups);
	}

	public List<ConsumerGroups> selectByPage(int startIndex, int pageSize) {
		return consumerGroupsMapper.selectByPage(startIndex, pageSize);
	}

	public int selectTotalRecords() {
		return consumerGroupsMapper.selectTotalRecords();
	}
}
