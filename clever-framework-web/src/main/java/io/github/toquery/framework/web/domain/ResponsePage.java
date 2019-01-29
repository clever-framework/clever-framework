package io.github.toquery.framework.web.domain;

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

    //    @JsonProperty(value = "#{app.page.param.pageSize:pageSize}")
    private Integer pageSize;

    //    @JsonProperty(value = "${app.page.param.pageNumber:pageNumber}")
    private Integer pageNumber;

    //    @JsonProperty(value = "${app.page.param.totalElements:totalElements}")
    private Long totalElements;

    //    @JsonProperty(value = "${app.page.param.totalPages:totalPages}")
    private Integer totalPages;
}
