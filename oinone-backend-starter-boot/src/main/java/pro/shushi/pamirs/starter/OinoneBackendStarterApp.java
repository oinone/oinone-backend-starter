package pro.shushi.pamirs.starter;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StopWatch;
import pro.shushi.pamirs.framework.connectors.data.kv.RedisClusterConfig;
import pro.shushi.pamirs.framework.connectors.data.kv.RedisConfig;
import pro.shushi.pamirs.meta.annotation.fun.extern.Slf4j;

import java.util.Arrays;

/**
 * Oinone Backend Starter
 *
 * @author yakir on 2022/05/01 11:24.
 */
@ComponentScan(
        basePackages = {
                "pro.shushi.pamirs"
        },
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                RedisConfig.class
                        }
                )
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                RedisAutoConfiguration.class,
                                RedisRepositoriesAutoConfiguration.class,
                                RedisReactiveAutoConfiguration.class,
                                RedisClusterConfig.class,
                                pro.shushi.pamirs.framework.connectors.data.kv.RedisClusterConfig.class
                        }
                ),
        }
)
@EnableDubbo
@Slf4j
@EnableTransactionManagement
@MapperScan(value = "pro.shushi.pamirs", annotationClass = Mapper.class)
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        FreeMarkerAutoConfiguration.class
})
public class OinoneBackendStarterApp {

    public static void main(String[] args) {
        log.info(Arrays.toString(args));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String info = "Oinone Backend Starter App Starting ...";
        log.info(info);

        System.setProperty("dubbo.application.logger", "slf4j");
        //System.setProperty("druid.filters", "slf4j");

        new SpringApplicationBuilder(pro.shushi.pamirs.starter.OinoneBackendStarterApp.class)
                .web(WebApplicationType.SERVLET)
                .listeners(
                        new ApplicationPidFileWriter("oinone_backend_starter.pid")
                )
                .run(args);

        stopWatch.stop();

        log.info("Oinone Backend Starter App 启动耗时 {} s", stopWatch.getTotalTimeSeconds());
    }

}

