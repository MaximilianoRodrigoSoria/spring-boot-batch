package ar.com.spring.boot.batch.config;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class DataSourceConfig {

	@Value("classpath:schema-mysql.sql")
	private Resource schemaScript;
	  
	
	
	@Bean(name = "mmssqlDataSource")
	public DataSource mssqlDataSource() throws NamingException {
	    return DataSourceBuilder.create()
		      .url("jdbc:sqlserver://localhost:1433;databaseName=Pruebas")
		      .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
		      .username("mrsoria")
		      .password("Test2208")	 	    		
		.build();
	}	

	@Bean(name = "hsqldbDataSource")
	@Primary
	public DataSource hsqldbDataSource() throws NamingException {
	    return DataSourceBuilder.create()
		      .url("jdbc:hsqldb:mem:mydb")
		      .driverClassName("org.hsqldb.jdbc.JDBCDriver")
		      .username("sa")
		      .password("")	 	    		
		.build();
	}	
	
    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }



    @Bean
    public JdbcTemplate mssqlJdbcTemplate(@Qualifier("mmssqlDataSource") final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }}