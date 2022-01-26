package com.wisdom.example.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * 测试表 实体类 扩展
 *
 * @author captain
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TestExt对象 扩展", description = "测试表")
public class TestExt extends Test {

}
