package io.github.toquery.framework.mybatis.support;

import io.github.toquery.framework.mybatis.page.MybatisPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 可分页的mybatis操作类，需要在映射文件中创建id="select"的查询，<br>
 * &lt;select id="select" resultMap="BaseResultMap" parameterType="Map" ><br>
 * ......<br>
 * &lt;/select><br>
 * @param <T> 实体类
 */
@Deprecated
@MybatisRepository
public interface MybatisPageableRepository<T> {
	/**
	 * 查询list结果
	 * @param searchParam 查询参数map
	 * @return list对象
	 */
	List<T> findAll(@Param("param") Map<String, Object> searchParam) ;
	/**
	 * 分页查询
	 * @param searchParam 查询参数map
	 * @param pageable 分页对象，使用new MybatisPageRequest(pageNum, pageSize)创建
	 * @return 分页之后的记录，实现接口org.springframework.data.domain.Page
	 */
	MybatisPage<T> findAll(@Param("param") Map<String, Object> searchParam, Pageable pageable) ;
}
