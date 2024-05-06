package org.example.service;

import org.example.exception.InvalidInputException;
import org.example.model.ClassificationResponse;
import org.example.model.TransactionResponse;
import org.example.model.ResponseHandler;
import org.example.model.TransactionClientResponse;
import org.example.port.out.ClassificationClient;
import org.example.port.out.TransactionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {

    private final TransactionClient transactionClient;
    private final ClassificationClient classificationClient;

    @Autowired
    public ApplicationService(final TransactionClient transactionClient,
                              final ClassificationClient classificationClient) {
        this.transactionClient = transactionClient;
        this.classificationClient = classificationClient;
    }

    public List<TransactionResponse> getAllTransactions() {
        var allTransactions = transactionClient.getTransactions();
        var filteredTransactions = getTransactionsWithoutNullRecipient(allTransactions);
        var classifications = classificationClient.getClassifications();

        return ResponseHandler.createTransactionResponse(filteredTransactions, classifications);
    }

    public List<TransactionResponse> getAllTransactionsInInterval(final LocalDate dateFrom, final LocalDate dateTo) {
        validateInput(dateFrom, dateTo);
        var allTransactions = transactionClient.getTransactions();
        var classifications = classificationClient.getClassifications();
        var filteredTransactions = getTransactionsWithoutNullRecipient(allTransactions);
        var transactionsByDate = getTransactionsByDate(filteredTransactions, dateFrom, dateTo);

        return ResponseHandler.createTransactionResponse(transactionsByDate, classifications);
    }

    public ClassificationResponse updateClassification(final String recipientId, final String classification) {
        if (classificationClient.changeClassificationByRecipient(recipientId, classification)) {
            return new ClassificationResponse(classification, recipientId);
        } else {
            throw new InvalidInputException("Classification could not be updated");
        }
    }

    private void validateInput(final LocalDate dateFrom, final LocalDate dateTo) {
        if (dateFrom == null || dateTo == null) {
            throw new InvalidInputException("One or more date is missing");
        }
        if (dateFrom.isAfter(dateTo)) {
            throw new InvalidInputException("Date from can not be after date to");
        }
    }

    private List<TransactionClientResponse> getTransactionsWithoutNullRecipient(final List<TransactionClientResponse> res) {
        return res.stream()
                .filter(transaction -> transaction.recipientId() != null)
                .toList();
    }

    private List<TransactionClientResponse> getTransactionsByDate(final List<TransactionClientResponse> transactions,
                                                                  final LocalDate dateFrom,
                                                                  final LocalDate dateTo) {
        return transactions.stream()
                .filter(transaction -> LocalDate.parse(transaction.date()).isEqual(dateFrom) ||
                        LocalDate.parse(transaction.date()).isAfter(dateFrom) )
                .filter(transaction -> LocalDate.parse(transaction.date()).isEqual(dateTo) ||
                        LocalDate.parse(transaction.date()).isBefore(dateTo))
                .toList();
    }
}
