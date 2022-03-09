package com.wisdom.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wisdom.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
/**
 *  实体类
 *
 * @author captain
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "CLIENTDETAILS")
@Table(appliesTo = "CLIENTDETAILS", comment = "")
@ApiModel(value = "Clientdetails对象", description = "")
public class Clientdetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "int COMMENT ''")
    protected Integer accessTokenValidity;

    @Column(columnDefinition = "varchar(4096) COMMENT ''")
    protected String additionalInformation;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String appId;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String appSecret;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String authorities;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String autoApproveScopes;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String grantTypes;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String redirectUrl;

    @Column(columnDefinition = "int COMMENT ''")
    protected Integer refreshTokenValidity;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String resourceIds;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String scope;
}
