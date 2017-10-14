package com.bqr.framework.sample.mapper;

import com.bqr.framework.sample.entity.User;
import org.apache.ibatis.annotations.Mapper;

import com.bqr.framework.mybatis.mapper.FrameworkBaseMapper;

/**
 * test
 *
 * @author
 * @Date 2017-10-13 13:50
 */
@Mapper
public interface TestMapper extends FrameworkBaseMapper<User>
{
}
