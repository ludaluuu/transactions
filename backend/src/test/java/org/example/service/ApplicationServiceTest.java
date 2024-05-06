package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.exception.InvalidInputException;
import org.example.model.ClassificationResponse;
import org.example.model.TransactionResponse;
import org.example.model.TransactionClientResponse;
import org.example.port.out.ClassificationClient;
import org.example.port.out.TransactionClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ApplicationServiceTest {
    private ApplicationService sut;
    private TransactionClient mockTransactionClient;
    private ClassificationClient mockClassificationClient;

    @BeforeEach
    void setUp() {
        mockTransactionClient = Mockito.mock(TransactionClient.class);
        mockClassificationClient = Mockito.mock(ClassificationClient.class);

        sut = new ApplicationService(mockTransactionClient, mockClassificationClient);
    }

    @Test
    void getAllTransactionsTest() {
        var transaction1 = new TransactionClientResponse("2023-01-04", "136622415", "Filmstaden MoS", -62);
        var transaction2 = new TransactionClientResponse("2023-01-07", "455654468", "SL AB STHLM", -238);
        var classifications = Map.of(
                "136622415","ENTARTAINMENT",
                "455654468", "TRANSPORT"
        );
        var expectedResponse = List.of(
                new TransactionResponse(LocalDate.parse("2023-01-04"), "136622415", "Filmstaden MoS", -62, "ENTARTAINMENT"),
                new TransactionResponse(LocalDate.parse("2023-01-07"), "455654468", "SL AB STHLM", -238, "TRANSPORT")
        );
        Mockito.when(mockTransactionClient.getTransactions()).thenReturn(List.of(transaction1,transaction2));
        Mockito.when(mockClassificationClient.getClassifications()).thenReturn(classifications);

        var res = sut.getAllTransactions();

        Assertions.assertThat(res)
                .hasSize(2)
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    void getAllTransactionsFilterNullTest() {
        var transaction1 = new TransactionClientResponse("2023-01-04", "136622415", "Filmstaden MoS", -62);
        var transaction2 = new TransactionClientResponse("2023-01-07", "455654468", "SL AB STHLM", -238);
        var transaction3 = new TransactionClientResponse("2023-01-14", null, "0709947189", 238);
        var classifications = Map.of(
                "136622415","ENTARTAINMENT",
                "455654468", "TRANSPORT"
        );
        var expectedResponse = List.of(
                new TransactionResponse(LocalDate.parse("2023-01-04"), "136622415", "Filmstaden MoS", -62, "ENTARTAINMENT"),
                new TransactionResponse(LocalDate.parse("2023-01-07"), "455654468", "SL AB STHLM", -238, "TRANSPORT")
        );
        Mockito.when(mockTransactionClient.getTransactions()).thenReturn(List.of(transaction1,transaction2, transaction3));
        Mockito.when(mockClassificationClient.getClassifications()).thenReturn(classifications);

        var res = sut.getAllTransactions();

        Assertions.assertThat(res)
                .hasSize(2)
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    void getAllTransactionsInIntervalTest() {
        var transaction1 = new TransactionClientResponse("2023-01-04", "136622415", "Filmstaden MoS", -62);
        var transaction2 = new TransactionClientResponse("2023-01-07", "455654468", "SL AB STHLM", -238);
        var transaction3 = new TransactionClientResponse("2023-01-14", null, "0709947189", 238);
        var classifications = Map.of(
                "136622415","ENTARTAINMENT",
                "455654468", "TRANSPORT"
        );
        var expectedResponse = List.of(new TransactionResponse(LocalDate.parse("2023-01-04"), "136622415", "Filmstaden MoS", -62, "ENTARTAINMENT"));
        Mockito.when(mockTransactionClient.getTransactions()).thenReturn(List.of(transaction1,transaction2, transaction3));
        Mockito.when(mockClassificationClient.getClassifications()).thenReturn(classifications);

        var res = sut.getAllTransactionsInInterval(LocalDate.parse("2023-01-04"), LocalDate.parse("2023-01-05"));

        Assertions.assertThat(res)
                .hasSize(1)
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    void updateClassificationTest() {
        Mockito.when(mockClassificationClient.changeClassificationByRecipient("123", "FOOD")).thenReturn(true);

        Assertions.assertThat(sut.updateClassification("123", "FOOD"))
                .isEqualTo(new ClassificationResponse("FOOD", "123"));
    }

    @Test
    void updateClassificationFailTest() {
        Mockito.when(mockClassificationClient.changeClassificationByRecipient("123", "FOOD")).thenReturn(false);

        Assertions.assertThatThrownBy(() -> sut.updateClassification("123", "FOOD"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Classification could not be updated");
    }
}
