package org.example.model;

public record TransactionClientResponse(String date,
                                        String recipientId,
                                        String description,
                                        int amount) {
}
