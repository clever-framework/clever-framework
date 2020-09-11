package io.github.toquery.framework.core.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * 用户基础字段定义与 Spring UserDetails 相互辅助
 */
public interface AppUserDetails extends UserDetails {

    Date getLastPasswordResetDate();

    void setLastPasswordResetDate(Date lastPasswordResetDate);
}
