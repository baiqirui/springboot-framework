package com.bqr.framework.sample.entity;

import javax.persistence.Table;

import com.bqr.framework.mybatis.entity.BaseEntity;

import lombok.Data;

/**
 * user
 *
 * @author
 * @Date 2017-10-13 13:51
 */
@Data
@Table(name = "test_bak")
public class User extends BaseEntity
{
    private String name;

    private Integer age;

    private Long classId;

    private Long groupId;
}
