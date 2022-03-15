CREATE TABLE ${domains.table} (
${domains.domainid} INT NOT NULL,
${domains.domain} VARCHAR(32) NOT NULL,
${domains.domainname} VARCHAR(32) NOT NULL,
${domains.posid} CHARACTER (1) CHECK( ${domains.posid} IN ('n','v','a','r','s') ) NOT NULL
)
;
