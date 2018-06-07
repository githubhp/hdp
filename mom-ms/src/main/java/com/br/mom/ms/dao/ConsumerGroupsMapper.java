package com.br.mom.ms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.br.mom.ms.model.ConsumerGroups;

@Mapper
public interface ConsumerGroupsMapper {
	// 通过id删除一条记录
	int deleteByPrimaryKey(Integer id);

	// 增加一条记录
	int insertSelective(ConsumerGroups consumerGroups);

	// 查询所有记录
	List<ConsumerGroups> selectAllSelective(ConsumerGroups consumerGroups);

	// 通过id查询一条记录
	ConsumerGroups selectByPrimaryKey(Integer id);

	// 修改一条记录
	int updateByPrimaryKeySelective(ConsumerGroups consumerGroups);

	// 分页查询显示页面
	List<ConsumerGroups> selectByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

	// 查询数据总数
	int selectTotalRecords();
}
