package com.wisdom.auth.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 *  实体类 扩展
 *
 * @author captain
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OauthCodeExt对象 扩展", description = "")
public class OauthCodeExt extends OauthCode {

}
