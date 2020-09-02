package io.github.toquery.framework.core.security;

import java.util.Date;

/**
 * 用户基础字段定义与 Spring UserDetails 相互辅助
 */
public interface AppUserDetails {

    String getUsername();

    void setUsername(String username);

    Date getLastPasswordResetDate();

    void setLastPasswordResetDate(Date lastPasswordResetDate);
}
