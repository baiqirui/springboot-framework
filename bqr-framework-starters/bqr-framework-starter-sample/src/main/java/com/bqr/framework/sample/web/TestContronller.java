package com.bqr.framework.sample.web;

import com.bqr.framework.sample.entity.User;
import com.bqr.framework.sample.service.TestService;
import com.bqr.framework.web.exception.ArgumentException;
import com.bqr.framework.web.model.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = "测试接口")
public class TestContronller
{
    @Autowired
    private TestService testService;
    
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
         return new ResultBody(testService.get(id));
     }
    
}
