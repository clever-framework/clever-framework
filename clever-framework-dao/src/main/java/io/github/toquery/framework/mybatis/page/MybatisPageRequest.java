package io.github.toquery.framework.mybatis.page;

import io.github.toquery.framework.mybatis.page.support.PageInterceptor;
import org.springframework.data.domain.PageRequest;

/**
 * mybatis分页请求，分页号由0开始
 * 通过通用的接口PageRequest将分页号等参数暂存起来。
 * 在后续的mybatis接口中并没有直接使用PageRequest类
 */
public class MybatisPageRequest extends PageRequest implements MybatisPageable {
	private static final long serialVersionUID = 1L;
	/**
	 * 创建mybatis分页请求，分页号（pageNum）由0开始
	 * @param pageNum
	 * @param pageSize
	 */
	private MybatisPageRequest(int pageNum, int pageSize) {
		super(pageNum, pageSize);
	}

	private MybatisPageRequest() {
		super(0, 1);
	}

	public static PageRequest of(int pageNum, int pageSize) {
		//暂存分页请求参数
		PageInterceptor.startPage(pageNum+1, pageSize) ;
		return PageRequest.of(pageNum , pageSize) ;
	}
}
