ALTER TABLE ${domains.table} ADD CONSTRAINT `pk_@{domains.table}`                                    PRIMARY KEY (${domains.domainid});
ALTER TABLE ${domains.table} ADD CONSTRAINT `uk_@{domains.table}_@{domains.domain}_@{domains.posid}` UNIQUE KEY  (${domains.domain},${domains.posid});
