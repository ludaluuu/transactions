package org.example.port.out;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TransactionClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class TransactionClient {
    private final List<TransactionClientResponse> transactionResponses;

    @Autowired
    public TransactionClient(@Value("classpath:responses/transactionServiceData.json") final String filepath) {
        List<TransactionClientResponse> tmpResponse;
        try {
            var mapper = new ObjectMapper();
            var file = ResourceUtils.getFile(filepath);
            tmpResponse = mapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            System.out.println("File not found...");
            tmpResponse = Collections.emptyList();
        }
        transactionResponses = tmpResponse;
    }

    public List<TransactionClientResponse> getTransactions() {
        return transactionResponses;
    }
}
