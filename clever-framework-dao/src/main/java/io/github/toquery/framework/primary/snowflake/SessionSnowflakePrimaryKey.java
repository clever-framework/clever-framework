package io.github.toquery.framework.primary.snowflake;

public class SessionSnowflakePrimaryKey extends DockerSnowflakePrimaryKey {

    @Override
    public String getName() {
        return "session的id生成器";
    }

    /**
     * session的id默认使用1作为业务线标识
     */
    @Override
    public long getBusinessIndex(int binaryLength, long timeMills, Object object) {
        return 1 ;
    }
}
