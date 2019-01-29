package io.github.toquery.framework.security.jwt;

import io.github.toquery.framework.security.jwt.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by stephan on 20.03.16.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("from User where userName = :userName")
    User findByUserName(@Param("userName") String username);
}
