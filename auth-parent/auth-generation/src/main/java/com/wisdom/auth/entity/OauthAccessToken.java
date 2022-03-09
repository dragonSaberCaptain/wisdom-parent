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
import java.sql.Blob;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 *  实体类
 *
 * @author captain
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "OAUTH_ACCESS_TOKEN")
@Table(appliesTo = "OAUTH_ACCESS_TOKEN", comment = "")
@ApiModel(value = "OauthAccessToken对象", description = "")
public class OauthAccessToken extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Column(columnDefinition = "blob COMMENT ''")
    protected Blob authentication;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String authenticationId;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String clientId;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String refreshToken;

    @JsonIgnore
    @Column(columnDefinition = "blob COMMENT ''")
    protected Blob token;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String tokenId;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String userName;
}
