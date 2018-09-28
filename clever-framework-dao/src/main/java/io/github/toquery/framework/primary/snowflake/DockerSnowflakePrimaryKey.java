package io.github.toquery.framework.primary.snowflake;

public class DockerSnowflakePrimaryKey extends DefaultSnowflakePrimaryKey {

    @Override
    public String getName() {
        return "docker容器内id生成器";
    }
    /**
     * 获取机器的标识，默认收集第三部分，<b>在docker中宿主机的ip默认是第三部分</b>
     * @return
     */
    @Override
    public long getMachineIndex(int binaryLength , long timeMills , Object object){
        return IP[IP.length-2] ;
    }


}
