package com.bqr.framework.web.exception;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bqr.framework.web.config.ResultCodeConfig;
import com.bqr.framework.web.model.ResultBody;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import com.bqr.framework.constant.ResultCodeConstant;


import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常拦截器;
 * 
 * @author baiqirui
 * @version [版本号, 2017年8月3日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ExceptionHandlerExceptionResolver
{
    
    @ExceptionHandler({Exception.class})
    public ResultBody processException(HttpServletRequest request, HttpServletResponse response, Exception e)
    {
        log.error("公共异常处理：", e);
        
        // 判断是否是系统内部自定义异常;
        if (e instanceof ExceptionBase)
        {
            ExceptionBase exb = (ExceptionBase)e;
            return new ResultBody(exb.getErrorCode(), exb.getMessage());
        }
        // 2.判断是否是spring的参数校验异常
        else if (e instanceof MethodArgumentNotValidException)
        {
            // 按需重新封装需要返回的错误信息
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException)e;
            // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            for (FieldError error : exception.getBindingResult().getFieldErrors())
            {
                String errorMsg = ResultCodeConfig.getResultMessage(ResultCodeConstant.PARAMETER_INVALID);
                errorMsg = MessageFormat.format(errorMsg, error.getField(), error.getDefaultMessage());
                return new ResultBody(ResultCodeConstant.PARAMETER_INVALID, errorMsg);
            }
        }
        return ResultBody.createUnKnowExceptionResultBody();
    }
}
