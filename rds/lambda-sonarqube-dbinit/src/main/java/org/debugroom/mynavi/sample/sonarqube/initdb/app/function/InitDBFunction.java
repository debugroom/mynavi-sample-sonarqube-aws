package org.debugroom.mynavi.sample.sonarqube.initdb.app.function;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.function.Function;

@Slf4j
public class InitDBFunction implements Function<Map<String, Object>, Flux<String>> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    AWSSimpleSystemsManagement awsSimpleSystemsManagement;

    @Override
    public Flux<String> apply(Map<String, Object> stringObjectMap) {
        GetParameterRequest request = new GetParameterRequest();
        request.setName("mynavi-sonarqube-rds-sonar-password");
        request.setWithDecryption(true);
        jdbcTemplate.execute("CREATE ROLE sonar WITH LOGIN PASSWORD '"
                + awsSimpleSystemsManagement.getParameter(request).getParameter().getValue()
                + "';");
        return Flux.just("Complete!");
    }

}
