package io.github.toquery.framework.validate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 实体中属性的有效性验证
 * http://hibernate.org/validator/documentation/getting-started/
 * http://beanvalidation.org/
 */
public abstract class ValidateHelper {

    public static Validator VALIDATOR;

    public static final String INVALIDE_MSG_TEMPLATE = "%s %s %s";

    static {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory();

        //实例化验证器，由于是线程安全的，所以可以共用
        VALIDATOR = validatorFactory.getValidator();
    }

    /**
     * 验证实体是否负责规范，参考：
     *
     * @param object
     * @param <S>
     * @return
     */
    public static <S> List<String> validate(S object) {
        if (object == null) {
            return null;
        }
        return getInvalidMsg(VALIDATOR.validate(object));
    }

    /**
     * 验证指定实体属性是否符合规则
     *
     * @param object
     * @param properties
     * @param <S>
     * @return 返回错误消息
     */
    public static <S> List<String> validateProperties(S object, Collection<String> properties) {
        if (object == null || CollectionUtils.isEmpty(properties)) {
            return null;
        }
        Set<ConstraintViolation<S>> totalConstraintViolations = null;

        Set<ConstraintViolation<S>> propertyViolations = null;
        //对属性进行验证判断是否符合验证条件
        for (String property : properties) {
            //执行属性验证
            propertyViolations = VALIDATOR.validateProperty(object, property);
            if (CollectionUtils.isEmpty(propertyViolations)) {
                continue;
            }
            //将单个属性违反的规则加到列表中
            if (totalConstraintViolations == null) {
                totalConstraintViolations = Sets.newHashSet();
            }
            totalConstraintViolations.addAll(propertyViolations);
        }

        return getInvalidMsg(totalConstraintViolations);
    }

    /**
     * 验证方法参数
     *
     * @param object
     * @param method
     * @param args
     * @param <S>
     * @return
     */
    public static <S> List<String> validateParameters(S object, Method method, Object[] args) {
        if (object == null || method == null || args == null || args.length < 1) {
            return null;
        }

        return getInvalidMsg(VALIDATOR.forExecutables().validateParameters(object, method, args));
    }

    /**
     * 将限制转换为消息对象
     *
     * @param constraintViolations
     * @param <S>
     * @return
     */
    public static <S> List<String> getInvalidMsg(Set<ConstraintViolation<S>> constraintViolations) {
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return null;
        }

        List<String> invalidMsg = Lists.newArrayListWithCapacity(constraintViolations.size());
        for (ConstraintViolation<S> constraintViolation : constraintViolations) {
            invalidMsg.add(String.format(INVALIDE_MSG_TEMPLATE, constraintViolation.getRootBeanClass().getName(), constraintViolation.getPropertyPath(), constraintViolation.getMessage()));
        }

        return invalidMsg;
    }

}
