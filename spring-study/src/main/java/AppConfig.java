import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Kevin
 * @date 2024/12/21 10:47
 */
@Configuration // 声明这是一个配置类
//@EnableAspectJAutoProxy //启用AOP
@EnableTransactionManagement //启用事务管理
@ComponentScan(basePackages = {"aspect", "service"}) // 扫描指定包下的组件
public class AppConfig {



    @Bean
    public JdbcTemplate jbbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    @Bean
    public DataSource dataSource() {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // MySQL驱动
        dataSource.setUrl("jdbc:mysql://localhost:3306/saas?useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setInitialSize(5); // 初始连接池大小
        dataSource.setMaxTotal(10);   // 最大连接数
        return dataSource;
    }



}
