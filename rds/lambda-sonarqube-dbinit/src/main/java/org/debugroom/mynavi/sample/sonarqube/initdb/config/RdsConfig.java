package org.debugroom.mynavi.sample.sonarqube.initdb.config;

import javax.sql.DataSource;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.aws.context.config.annotation.EnableStackConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.debugroom.mynavi.sample.sonarqube.initdb.app.CloudFormationStackResolver;

@Slf4j
@EnableStackConfiguration(stackName = "mynavi-sonarqube-rds")
@Configuration
public class RdsConfig {

    private static final String RDS_DB_NAME  = "debugroom-mynavi-sonarqube-cfn-vpc-RDS-DBName";
    private static final String RDS_ENDPOINT = "debugroom-mynavi-sonarqube-cfn-vpc-RDS-Endpoint";
    private static final String RDS_USERNAME = "debugroom-mynavi-sonarqube-cfn-vpc-RDS-UserName";
    private static final String RDS_PASSWORD = "mynavi-sonarqube-rds-postgres-master-password";

    @Autowired
    CloudFormationStackResolver cloudFormationStackResolver;

    @Bean
    AWSSimpleSystemsManagement awsSimpleSystemsManagement(){
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

    @Bean
    public DataSource dataSource(){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://"
                + cloudFormationStackResolver.getExportValue(RDS_ENDPOINT)
                + ":5432/"
                + cloudFormationStackResolver.getExportValue(RDS_DB_NAME));
        dataSourceBuilder.username(
                cloudFormationStackResolver.getExportValue(RDS_USERNAME));
        GetParameterRequest request = new GetParameterRequest();
        request.setName(RDS_PASSWORD);
        request.setWithDecryption(true);
        dataSourceBuilder.password(awsSimpleSystemsManagement().getParameter(request)
                .getParameter().getValue());
        return dataSourceBuilder.build();
    }
}
