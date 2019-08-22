package ar.com.spring.boot.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import ar.com.spring.boot.batch.listener.JobListener;
import ar.com.spring.boot.batch.model.Persona;
import ar.com.spring.boot.batch.processor.PersonaItemProcessor;




@Configuration
@EnableBatchProcessing
@Import({DataSourceConfig.class})
public class BatchConfiguration {
	

	
	

	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public FlatFileItemReader<Persona> reader(){
		return new FlatFileItemReaderBuilder<Persona>()
			   .name("personaItemReader")
			   .resource(new ClassPathResource("example-personas.csv"))
			   .delimited()
			   .names(new String[] {"nombre", "apellido", "telefono"})
			   .fieldSetMapper(new BeanWrapperFieldSetMapper<Persona>() {{
				   setTargetType(Persona.class);
			   }})
			   .build();
	}
	
	@Bean
	public PersonaItemProcessor processor() {
		return new PersonaItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<Persona> writer(@Qualifier("mmssqlDataSource")final DataSource dataSource){
		return new JdbcBatchItemWriterBuilder<Persona>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO persona (id, nombre, apellido, telefono) VALUES ((SELECT ISNULL(MAX(id)+1,0) FROM persona WITH(SERIALIZABLE, UPDLOCK)), :nombre, :apellido, :telefono)")
				.dataSource(dataSource)
				.build();
	}
	
	@Bean
	public Job importPersonaJob(JobListener listener, Step step1) {
		return jobBuilderFactory.get("importPersonaJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}
	
	@Bean
	public Step step1(JdbcBatchItemWriter<Persona> writer) {
		return stepBuilderFactory.get("step1")
				.<Persona, Persona> chunk(10)
				.reader(reader())
				.writer(writer)
				.build();
	}

}
