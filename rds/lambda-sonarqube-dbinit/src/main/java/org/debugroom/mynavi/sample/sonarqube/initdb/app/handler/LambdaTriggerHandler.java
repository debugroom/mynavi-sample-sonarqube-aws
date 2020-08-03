package org.debugroom.mynavi.sample.sonarqube.initdb.app.handler;

import com.amazonaws.services.lambda.runtime.Context;
import lombok.extern.slf4j.Slf4j;
import org.debugroom.mynavi.sample.sonarqube.initdb.app.CloudFormationResponseSender;
import org.debugroom.mynavi.sample.sonarqube.initdb.app.model.Status;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class LambdaTriggerHandler extends
        SpringBootRequestHandler<Map<String, Object>, String> {

    @Override
    public Object handleRequest(Map<String, Object> event, Context context) {
        for(String key : event.keySet()){
            log.info("[Key]" + key + " [Value]" + event.get(key).toString());
        }
        Object requestType = event.get("RequestType");
        if(requestType != null && Objects.equals(requestType.toString(), "Delete")){
            CloudFormationResponseSender.send(event, context, Status.SUCCESS,
                    event.get("ResourceProperties"), event.get("PhysicalResourceId").toString(), false);
            return Flux.just("Complete!");
        }
        Object result = super.handleRequest(event, context);
        if(requestType != null && result instanceof Flux){
            CloudFormationResponseSender.send(event, context, Status.SUCCESS,
                    event.get("ResourceProperties"), null, false);
        }
        return result;
    }

}
