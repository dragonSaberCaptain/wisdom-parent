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
import java.util.Date;
/**
 *  实体类
 *
 * @author captain
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "OAUTH_APPROVALS")
@Table(appliesTo = "OAUTH_APPROVALS", comment = "")
@ApiModel(value = "OauthApprovals对象", description = "")
public class OauthApprovals extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String clientId;

    @Column(columnDefinition = "timestamp COMMENT ''")
    protected Date expiresAt;

    @Column(columnDefinition = "timestamp COMMENT ''")
    protected Date lastModifiedAt;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String scope;

    @Column(columnDefinition = "varchar(10) COMMENT ''")
    protected String status;

    @Column(columnDefinition = "varchar(128) COMMENT ''")
    protected String userId;
}
