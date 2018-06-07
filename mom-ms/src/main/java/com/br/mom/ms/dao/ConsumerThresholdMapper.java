package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.br.mom.ms.model.ConsumerThreshold;

@Mapper
public interface ConsumerThresholdMapper {
	// 通过id删除一条记录
	int deleteByPrimaryKey(Integer id);

	// 新增一条记录
	int insertSelective(ConsumerThreshold consumerThreshold);

	// 查询所有的记录
	List<ConsumerThreshold> selectAllSelective(ConsumerThreshold consumerThreshold);

	// 通过id查询一条记录
	ConsumerThreshold selectByPrimaryKey(Integer id);

	// 修改一条记录信息
	int updateByPrimaryKeySelective(ConsumerThreshold consumerThreshold);

	// 分页查询
	List<ConsumerThreshold> selectByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

	// 查询用于分页展示的总记录条数
	int selectTotalRecords();
}
