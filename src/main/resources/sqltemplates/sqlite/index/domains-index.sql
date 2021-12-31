CREATE UNIQUE INDEX uk_${domains.table}_${domains.domain}_${domains.posid} ON ${domains.table}(${domains.domain},${domains.posid});
