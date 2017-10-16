package com.bqr.framework.web.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局拦截器，用于一些全局设置 Created by lvjj on 2016/11/17
 */

public class GlobalInterceptor implements HandlerInterceptor
{
    
    private Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);
    
    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @return 有返回true才会继续向下执行，返回false取消当前请求
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception
    {
        // response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Max-Age", "2592000");
        response.setHeader("Access-Control-Allow-Headers",
            "Content-Type,sessionId,appKey,timestamp,v,sign");
        
        
        // 2xx 状态码
        switch (request.getMethod())
        {
            case "GET":
                response.setStatus(HttpStatus.OK.value());
                break;
            case "PUT":
                response.setStatus(HttpStatus.OK.value());
                break;
            case "POST":
                response.setStatus(HttpStatus.CREATED.value());
                break;
            case "DELETE":
                response.setStatus(HttpStatus.NO_CONTENT.value());
                break;
            default:
                break;
        }
        return true;
    }
    
    
    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     * 
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception[参数说明] 请参见：@see
     *             org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
     *             javax.servlet.http.HttpServletResponse, java.lang.Object,
     *             org.springframework.web.servlet.ModelAndView)
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView)
        throws Exception
    {
        String accept = request.getHeader("Accept");
        if (MediaType.APPLICATION_JSON_VALUE.equals(accept) || MediaType.ALL_VALUE.equals(accept))
        {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
        
        // 2.4，add by yidi，API 执行时间
        Long apiStart = (Long)request.getAttribute("api_start");
        Long apiRuntime = (null != apiStart) ? new Date().getTime() - apiStart : 0;
        // 应陈闻一要求，改成秒级，毫秒单位为小数位 by lvjj 20161109
        response.addHeader("api_runtime", String.valueOf((apiRuntime / 1000)));
        
        // 2.5，add by yidi，打印 api 访问日志
        StringBuffer requestUrl = request.getRequestURL();
        if (null != request.getQueryString())
        {
            requestUrl.append("/").append(request.getQueryString());
        }
        logger.info("api runtime " + (apiRuntime / 1000.0) + " url=" + requestUrl.toString());
    }
    
    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception
    {
        
    }
    
}