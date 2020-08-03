package org.debugroom.mynavi.sample.sonarqube.initdb.app;

import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.Export;
import com.amazonaws.services.cloudformation.model.ListExportsRequest;
import com.amazonaws.services.cloudformation.model.ListExportsResult;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CloudFormationStackResolver implements InitializingBean {

    @Autowired
    AmazonCloudFormationClient amazonCloudFormationClient;

    @Autowired(required = false)
    ResourceIdResolver resourceIdResolver;

    private List<Export> exportList = new ArrayList<>();

    public String getExportValue(String exportName){
        Optional<Export> targetExport = exportList.stream()
                .filter(export -> export.getName().equals(exportName))
                .findFirst();
        return targetExport.isPresent() ? targetExport.get().getValue() : null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ListExportsResult listExportsResult = amazonCloudFormationClient
                .listExports(new ListExportsRequest());
        List<Export> exportList = listExportsResult.getExports();
        while (100 == exportList.size()){
            this.exportList.addAll(exportList);
            listExportsResult = getNextExports(listExportsResult.getNextToken());
            exportList = listExportsResult.getExports();
        }
        this.exportList.addAll(exportList);
    }

    private ListExportsResult getNextExports(String nextToken){
        return amazonCloudFormationClient.listExports(
                new ListExportsRequest().withNextToken(nextToken));
    }

}
