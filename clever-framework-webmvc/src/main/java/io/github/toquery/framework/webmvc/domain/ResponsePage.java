package io.github.toquery.framework.webmvc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author toquery
 * @version 1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePage {

    private int pageSize;

    private int pageNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long totalElements;

    private int totalPages;
}
