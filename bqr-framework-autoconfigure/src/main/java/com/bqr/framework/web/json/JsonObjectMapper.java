package com.bqr.framework.web.json;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON 转换。
 * @author yidi
 */
public class JsonObjectMapper extends ObjectMapper
{
    private static final long serialVersionUID = 1L;

    public JsonObjectMapper()
    {
        setSerializationInclusion(Include.ALWAYS);
        SimpleDateFormat dateFormat = new SimpleDateFormat
            ("yyyy-MM-dd HH:mm:ss");
        setDateFormat(dateFormat);
        configure(SerializationFeature.WRITE_NULL_MAP_VALUES,false);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);


    }
}
