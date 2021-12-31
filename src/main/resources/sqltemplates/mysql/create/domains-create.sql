CREATE TABLE `${domains.table}` (
    `${domains.domainid}`     INT                           NOT NULL,
    `${domains.domain}`       VARCHAR(32)                   NOT NULL,
    `${domains.domainname}`   VARCHAR(32)                   NOT NULL,
    `${domains.posid}`        ENUM('n','v','a','r','s')     NOT NULL,

    PRIMARY KEY                                             (`${domains.domainid}`)
)
DEFAULT CHARSET=utf8mb3;
