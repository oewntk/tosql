CREATE TABLE ${domains.table}
(
    ${domains.domainid}   INTEGER      NOT NULL,
    ${domains.domainname} VARCHAR(32)  NOT NULL,
    ${domains.domain}     VARCHAR(32)  NOT NULL,
    ${domains.posid}      CHARACTER(1) NOT NULL,
    CONSTRAINT pk_${domains.table} PRIMARY KEY (${domains.domainid})
);
