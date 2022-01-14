package io.github.toquery.framework.webmvc.secret;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/**
 *
 */
@AllArgsConstructor
@NoArgsConstructor
public class AppSecretHttpMessage implements HttpInputMessage {
    private InputStream body;
    private HttpHeaders httpHeaders;

    @Override
    public InputStream getBody() {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }
}
