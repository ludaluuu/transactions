package org.example.model;

import java.time.LocalDate;

public record TransactionResponse(LocalDate date,
                                  String recipientId,
                                  String description,
                                  int amount,
                                  String classification){

    public static TransactionResponseBuilder builder() {
        return new TransactionResponseBuilder();
    }

    public static class TransactionResponseBuilder {
        private LocalDate date;
        private String recipientId;
        private String description;
        private int amount;
        private String classification;

        public TransactionResponseBuilder withDate(final LocalDate date) {
            this.date = date;
            return this;
        }

        public TransactionResponseBuilder withRecipientId(final String recipientId) {
            this.recipientId = recipientId;
            return this;
        }

        public TransactionResponseBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public TransactionResponseBuilder withAmount(final int amount) {
            this.amount = amount;
            return this;
        }

        public TransactionResponseBuilder withClassification(final String classification) {
            this.classification = classification;
            return this;
        }

        public TransactionResponse build() {
            return new TransactionResponse(
                    this.date,
                    this.recipientId,
                    this.description,
                    this.amount,
                    this.classification
            );
        }
    }
}
