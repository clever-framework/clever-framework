package io.github.toquery.framework.id.snowflake;

public class SessionSnowflakeIDPart extends DockerSnowflakeIDPart {

    @Override
    public String getName() {
        return "session的id生成器";
    }

    /**
     * session的id默认使用1作为业务线标识
     * @param binaryLength
     * @param timeMills
     * @param object
     * @return
     */
    @Override
    public long getBusinessIndex(int binaryLength, long timeMills, Object object) {
        return 1 ;
    }
}
