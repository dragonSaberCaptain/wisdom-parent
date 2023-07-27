INSERT INTO `wisdom`.`oauth_client_details` (`ID`, `CREATE_DATE_TIME`, `CREATE_ID`, `DEL_FLAG`, `REMARK`,
                                             `UP_DATE_TIME`, `UPDATE_ID`, `VERSION`, `ACCESS_TOKEN_VALIDITY`,
                                             `ADDITIONAL_INFORMATION`, `AUTHORITIES`, `AUTHORIZED_GRANT_TYPES`,
                                             `AUTOAPPROVE`, `CLIENT_ID`, `CLIENT_SECRET`, `REFRESH_TOKEN_VALIDITY`,
                                             `RESOURCE_IDS`, `SCOPE`, `WEB_SERVER_REDIRECT_URI`)
VALUES ('1', NULL, NULL, 0, NULL, NULL, NULL, 1, 300, NULL, NULL,
        'authorization_code,password,client_credentials,implicit,refresh_token', 'false', 'client',
        '$2a$10$YEpRG0cFXz5yfC/lKoCHJ.83r/K3vaXLas5zCeLc.EJsQ/gL5Jvum', 1500, 'resource', 'scope1,scope2',
        'http://www.baidu.com');


create table oauth_client_details
(
    client_id               VARCHAR(128) PRIMARY KEY,
    resource_ids            VARCHAR(128),
    client_secret           VARCHAR(128),
    scope                   VARCHAR(128),
    authorized_grant_types  VARCHAR(128),
    web_server_redirect_uri VARCHAR(128),
    authorities             VARCHAR(128),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(128)
);

create table oauth_client_token
(
    token_id          VARCHAR(128),
    token             BLOB,
    authentication_id VARCHAR(128) PRIMARY KEY,
    user_name         VARCHAR(128),
    client_id         VARCHAR(128)
);

create table oauth_access_token
(
    token_id          VARCHAR(128),
    token             BLOB,
    authentication_id VARCHAR(128) PRIMARY KEY,
    user_name         VARCHAR(128),
    client_id         VARCHAR(128),
    authentication    BLOB,
    refresh_token     VARCHAR(128)
);

create table oauth_refresh_token
(
    token_id       VARCHAR(128),
    token          BLOB,
    authentication BLOB
);

create table oauth_code
(
    code           VARCHAR(128),
    authentication BLOB
);

create table oauth_approvals
(
    userId         VARCHAR(128),
    clientId       VARCHAR(128),
    scope          VARCHAR(128),
    status         VARCHAR(10),
    expiresAt      TIMESTAMP,
    lastModifiedAt TIMESTAMP
);


-- customized oauth_client_details table
create table ClientDetails
(
    appId                  VARCHAR(128) PRIMARY KEY,
    resourceIds            VARCHAR(128),
    appSecret              VARCHAR(128),
    scope                  VARCHAR(128),
    grantTypes             VARCHAR(128),
    redirectUrl            VARCHAR(128),
    authorities            VARCHAR(128),
    access_token_validity  INTEGER,
    refresh_token_validity INTEGER,
    additionalInformation  VARCHAR(4096),
    autoApproveScopes      VARCHAR(128)
);
