package com.bqr.framework.jdbc.service;

import java.util.ArrayList;
import java.util.List;

import com.bqr.framework.jdbc.entity.BaseEntity;
import com.bqr.framework.jdbc.mapper.FrameworkBaseMapper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.Page;

import tk.mybatis.mapper.entity.Example;


/**
 * 基础服务Service，每个业务服务类建议都继承该类;
 * 
 * @author mealkey
 * @version [版本号, 2017年2月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseService<T>
{
    protected abstract FrameworkBaseMapper<T> getMapper();
    
    public T get(Long id)
    {
        return getMapper().selectByPrimaryKey(id);
    }
    
    public T selectOne(T record)
    {
        return getMapper().selectOne(record);
    }
    
    public List<T> select(T record)
    {
        return getMapper().select(record);
    }
    
    public List<T> select(Example example)
    {
        return getMapper().selectByExample(example);
    }
    
    public Page<T> selectPage(Example example, Integer pageNum, Integer pageSize)
    {
        Page<T> page = PageHelper.startPage(pageNum, pageSize);
        getMapper().selectByExample(example);
        return page;
    }
    
    
    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param ids
     * @return[参数、异常说明]
     * @return List<T> [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public List<T> selectByIds(String ids)
    {
        return getMapper().selectByIds(ids);
    }
    
    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     * 
     * @param ids
     * @return List<T>
     */
    public List<T> selectByIds(List<Long> ids)
    {
        if (null != ids && ids.size() > 0)
        {
            String idStr = StringUtils.join(ids, ",");
            return getMapper().selectByIds(idStr);
        }
        return new ArrayList<>();
    }
    
    public void deleteById(Long id)
    {
        getMapper().deleteByPrimaryKey(id);
    }
    
    public void deleteByExample(Example example)
    {
        getMapper().deleteByExample(example);
    }
    
    /**
     * 主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param ids
     * @return void [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteByIds(String ids)
    {
        getMapper().deleteByIds(ids);
    }
    
    /**
     * 主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     * 
     * @param ids
     */
    public void deleteByIds(List<Long> ids)
    {
        if (null != ids && ids.size() > 0)
        {
            String idStr = StringUtils.join(ids, ",");
            getMapper().deleteByIds(idStr);
        }
    }
    
    public int insert(T record)
    {
        ensureKey(record);
        return getMapper().insert(record);
    }
    
    public void save(T record)
    {
        BaseEntity entity = (BaseEntity)record;
        if (entity.getId() != null)
        {
            getMapper().updateByPrimaryKey(record);
        }
        else
        {
            entity.setId(getMapper().getKey(System.identityHashCode(entity)));
            getMapper().insert(record);
        }
    }
    
    protected void ensureKey(T record)
    {
        BaseEntity entity = (BaseEntity)record;
        if (entity.getId() == null)
        {
            entity.setId(getMapper().getKey(System.identityHashCode(entity)));
        }
    }
    
    /**
     * 批量新增
     *
     * @param recordList 【记录列表】
     * @param useGeneratedKeys 【是否使用自增主键】
     * @return[参数、异常说明]
     * @return int [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public int batchInsert(List<T> recordList, boolean useGeneratedKeys)
    {
        if (recordList == null || recordList.size() == 0)
        {
            return 0;
        }
        if (!useGeneratedKeys)
        {
            for (T record : recordList)
            {
                ensureKey(record);
            }
            return getMapper().insertListUnUseGeneratedKeys(recordList);
        }
        return getMapper().insertList(recordList);
    }
    
    /**
     * 批量新增（默认不是用自增序列）
     *
     * @param recordList
     * @return[参数、异常说明]
     * @return int [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public int batchInsert(List<T> recordList)
    {
        if (recordList == null || recordList.size() == 0)
        {
            return 0;
        }
        
        return batchInsert(recordList, false);
    }
    
    /**
     * 说明：根据主键更新属性不为null的值
     *
     * @param record
     * @return[参数、异常说明]
     * @return int [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public int update(T record)
    {
        return getMapper().updateByPrimaryKeySelective(record);
    }
    
    public int batchUpdate(List<T> recordList)
    {
        if (recordList == null || recordList.size() == 0)
        {
            return 0;
        }
        return getMapper().updateList(recordList);
    }
    
    /**
     * 分页查询
     * 
     * @param record
     * @param offset (从0开始)
     * @param limit （等于0不分页）
     * @return List<T>
     */
    public List<T> selectByRowBounds(T record, int offset, int limit)
    {
        RowBounds rowBounds = new RowBounds(offset, limit);
        return getMapper().selectByRowBounds(record, rowBounds);
    }
    
    public Long getKey()
    {
        // return idGenerator.nextId();
        return getMapper().getKey(1);
    }
    
}
