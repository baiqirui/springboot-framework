package com.bqr.framework.web.util;

import java.io.IOException;
import java.util.Map;

import com.bqr.framework.web.exception.BusinessException;
import com.bqr.framework.web.json.JsonObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * JSON工具类
 *
 * @author cc.peng
 */
public class JsonUtil
{
    private static Logger logger = Logger.getLogger(JsonUtil.class);
    
    private static JsonObjectMapper objectMapper = new JsonObjectMapper();
    
    public static <T> T readValue(String content, Class<T> valueType)
    {
        
        try
        {
            return objectMapper.readValue(content, valueType);
        }
        catch (Exception e)
        {
            logger.error("JSON 格式错误：" + content);
            throw new BusinessException(1, "JSON 格式错误，无法解析。");
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static <T> T readValue(String content, TypeReference valueTypeRef)
    {
        try
        {
            return objectMapper.readValue(content, valueTypeRef);
        }
        catch (Exception e)
        {
            logger.error("JSON 格式错误：" + content);
            throw new BusinessException(1, "JSON 格式错误，无法解析。");
        }
    }
    
    /**
     * 对象转JSON字符串
     *
     * @param obj
     * @return
     */
    public static String toJsonString(Object obj)
    {
        String json = null;
        try
        {
            json = objectMapper.writeValueAsString(obj);
            if (StringUtils.equals(json, "null"))
            {
                return null;
            }
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return json;
    }
    
    /**
     * Json转Map对象
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String json)
    {
        if (StringUtils.isBlank(json))
        {
            return null;
        }
        Map<String, Object> jsonMap = null;
        try
        {
            jsonMap = objectMapper.readValue(json, Map.class);
        }
        catch (IOException e)
        {
            throw new BusinessException(1, "JSON字符串转Map失败。");
        }
        return jsonMap;
    }
    
    /**
     * Json转对象
     *
     * @param json
     * @return
     */
    public static <T> T mapperObject(String json, Class<T> obj)
    {
        if (StringUtils.isBlank(json))
        {
            return null;
        }
        T object = null;
        try
        {
            object = objectMapper.readValue(json, obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException(1, "JSON转换对象失败。");
        }
        return object;
    }
    
}
