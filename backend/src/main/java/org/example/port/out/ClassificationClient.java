package org.example.port.out;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class ClassificationClient {

    private final Map<String,String> data;

    @Autowired
    public ClassificationClient(@Value("classpath:responses/classificationServiceData.json") final String filepath) {
        Map<String, String> tmpData;
        try {
            var mapper = new ObjectMapper();
            var file = ResourceUtils.getFile(filepath);
            tmpData = mapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            System.out.println("File not found...");
            tmpData = Collections.emptyMap();
        }
        data = tmpData;
    }

    public Map<String, String> getClassifications() {
        return data;
    }

    public boolean changeClassificationByRecipient(final String recipient, final String classification) {
        if (data.containsKey(recipient)) {
            data.put(recipient, classification);
            return true;
        }

        return false;
    }
}
