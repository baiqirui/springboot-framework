package com.bqr.framework.sample.web;

import com.bqr.framework.sample.rabbitmq.HelloSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bqr.framework.sample.entity.User;
import com.bqr.framework.sample.service.TestService;
import com.bqr.framework.web.exception.ArgumentException;
import com.bqr.framework.web.model.ResultBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "测试接口")
public class TestContronller
{
    @Autowired
    private TestService testService;

    @Autowired
    private HelloSender helloSender;

//    @Autowired
//    private ComputeClient computeClient;

//    @Autowired
//    private RedisService redisService;


//    @GetMapping("testRedisCluster")
//    @ApiOperation(value = "测试RedisCluster", response = String.class)
//    public String testRedisCluster(@RequestParam String key)
//    {
//        String value = redisService.get(key);
//        if (StringUtils.isBlank(value))
//        {
//            redisService.set(key, "Hello RedisSerivce");
//        }
//        return value;
//    }

    @GetMapping("testRedisAnonation")
    @ApiOperation(value = "测试Redis", response = String.class)
    public User testRedisAnonation(@RequestParam Long id)
    {
        return testService.getUser(id, "testRedisAnonation");
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


//    @PostMapping("testFeign")
//    @ApiOperation(value = "testFeign", response = User.class)
//    public ResultBody test3(@ApiParam("加数A") @RequestParam(value = "a") Integer a,
//                      @ApiParam("加数B") @RequestParam(value = "b") Integer b)
//    {
//        ResultBody a1 = computeClient.getA(a, "1234567899");
//        return a1;
//    }

    @PostMapping("testRabbit")
    @ApiOperation(value = "测试2", response = ResultBody.class)
    public ResultBody testRabbit(@RequestBody User user)
    {
        helloSender.sendUser(user);
        return ResultBody.success();
    }

}
