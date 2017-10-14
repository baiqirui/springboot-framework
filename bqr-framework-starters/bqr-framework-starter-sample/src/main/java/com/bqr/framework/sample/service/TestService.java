package com.bqr.framework.sample.service;

import com.bqr.framework.sample.entity.User;
import com.bqr.framework.sample.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bqr.framework.mybatis.mapper.FrameworkBaseMapper;
import com.bqr.framework.mybatis.service.BaseService;

/**
 * test
 *
 * @author
 * @Date 2017-10-13 13:48
 */
@Service
public class TestService extends BaseService<User>
{
    @Autowired
    private TestMapper testMapper;
    
    @Override
    protected FrameworkBaseMapper<User> getMapper()
    {
        return testMapper;
    }
}
