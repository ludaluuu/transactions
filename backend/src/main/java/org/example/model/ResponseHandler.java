package org.example.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ResponseHandler {

    private ResponseHandler() {
    }

    public static List<TransactionResponse> createTransactionResponse(final List<TransactionClientResponse> clientResponse,
                                                                      final Map<String, String> classifications) {

        return clientResponse.stream()
                .map(response -> createFromTransactionClientResponse(response, classifications))
                .toList();
    }

    public static TransactionResponse createFromTransactionClientResponse(final TransactionClientResponse clientResponse,
                                                                          final Map<String, String> classifications) {

        return TransactionResponse.builder()
                .withDate(LocalDate.parse(clientResponse.date()))
                .withRecipientId(clientResponse.recipientId())
                .withDescription(clientResponse.description())
                .withAmount(clientResponse.amount())
                .withClassification(classifications.get(clientResponse.recipientId()))
                .build();
    }
}
