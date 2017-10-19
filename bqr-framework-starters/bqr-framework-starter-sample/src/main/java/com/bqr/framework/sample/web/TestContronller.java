package com.bqr.framework.sample.web;

import com.bqr.framework.sample.entity.User;
import com.bqr.framework.sample.service.TestService;
import com.bqr.framework.web.exception.ArgumentException;
import com.bqr.framework.web.model.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redis.clients.jedis.JedisCluster;

@Slf4j
@RestController
@Api(tags = "测试接口")
public class TestContronller
{
    @Autowired
    private TestService testService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JedisCluster jedisCluster;


    @GetMapping("testRedisCluster")
    @ApiOperation(value = "测试RedisCluster", response = String.class)
    public String testRedisCluster(@RequestParam String key)
    {
        String value = jedisCluster.get(key);
        if (StringUtils.isBlank(value))
        {
            jedisCluster.set(key, "Hello Redis");
        }
        return value;
    }

    @GetMapping("testRedis")
    @ApiOperation(value = "测试Redis", response = String.class)
    public String testRedis(@RequestParam String key)
    {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String value = valueOps.get(key);
        if (StringUtils.isBlank(value))
        {
            valueOps.set(key, "Hello Redis111");
        }
        return value;
    }

    
    // @Transactional
    @PostMapping("test0")
    @ApiOperation(value = "测试0", response = User.class)
    public User test0(@Validated @RequestBody @ApiParam User user)
    {
        testService.insert(user);
        // String a = null;
        // a.length();
        return user;
    }
    
    @PostMapping("test3")
    @ApiOperation(value = "测试3", response = User.class)
    public User test3(@RequestBody @ApiParam("请求参数") @Validated User user)
    {
        throw ArgumentException.createNullOrBlankString("name");
        // return user;
    }
    
     @GetMapping("test2")
     @ApiOperation(value = "测试2", response = ResultBody.class)
     public ResultBody test1(@RequestParam Long id)
     {
         log.debug("参数：" + id);
         User user = testService.get(id);
         log.info("查询结果：" + user);
         return new ResultBody(user);
     }
    
}
