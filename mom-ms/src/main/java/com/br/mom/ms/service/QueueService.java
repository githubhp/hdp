package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.QueueMapper;
import com.br.mom.ms.model.Queue;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class QueueService {

	@Autowired
	private QueueMapper queueMapper;

	public int insert(Queue record) {
		queueMapper.insert(record);
		return record.getId();
	}

	public int insertSelective(Queue record) {
		queueMapper.insertSelective(record);
		return record.getId();
	}

	public int deleteByPrimaryKey(Integer id) {
		return queueMapper.deleteByPrimaryKey(id);
	}

	public int logicalDelete(Queue record) {
		return queueMapper.logicalDelete(record);
	}

	public int updateByPrimaryKey(Queue record) {
		return queueMapper.updateByPrimaryKey(record);
	}

	public int updateByPrimaryKeySelective(Queue record) {
		return queueMapper.updateByPrimaryKeySelective(record);
	}

	public Queue selectSelective(Queue record) {
		return queueMapper.selectSelective(record);
	}

	public List<Queue> selectAllSelective(Queue record) {
		return queueMapper.selectAllSelective(record);
	}

	public Queue selectByPrimaryKey(Integer id) {
		return queueMapper.selectByPrimaryKey(id);
	}

	public List<Queue> selectByPage(int startIndex, int pageSize) {
		return queueMapper.selectByPage(startIndex, pageSize);
	}

	public int selectTotalRecords() {
		return queueMapper.selectTotalRecords();
	}
}
