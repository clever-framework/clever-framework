package io.github.toquery.framework.security.authentication;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class AppWebAuthenticationDetails extends WebAuthenticationDetails {
    /**
     *
     */
    private static final long serialVersionUID = 6975601077710753878L;

    public AppWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
