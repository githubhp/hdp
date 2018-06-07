package com.br.mom.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.dao.ConsoleMapper;
import com.br.mom.ms.model.Console;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ConsoleService {

    @Autowired
    private ConsoleMapper consoleMapper;

    public int deleteByPrimaryKey(Integer id) {
        return consoleMapper.deleteByPrimaryKey(id);
    }

    public int insert(Console record) {
        consoleMapper.insert(record);
        return record.getId();
    }

    public int insertSelective(Console record) {
        consoleMapper.insertSelective(record);
        return record.getId();
    }

    public Console selectByPrimaryKey(Integer id) {
        return consoleMapper.selectByPrimaryKey(id);
    }

    public Console selectSelective(Console record) {
        return consoleMapper.selectSelective(record);
    }

    public List<Console> selectAllSelective(Console record) {
        return consoleMapper.selectAllSelective(record);
    }

    public int updateByPrimaryKeySelective(Console record) {
        return consoleMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Console record) {
        return consoleMapper.updateByPrimaryKey(record);
    }

}
