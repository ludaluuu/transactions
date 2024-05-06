package org.example.port.in;

import org.assertj.core.api.Assertions;
import org.example.model.ClassificationResponse;
import org.example.model.TransactionResponse;
import org.example.model.UpdateClassificationRequest;
import org.example.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

public class ApplicationControllerTest {
    private ApplicationController sut;
    private ApplicationService mockSvc;

    @BeforeEach
    void setUp() {
        mockSvc = Mockito.mock(ApplicationService.class);
        sut = new ApplicationController(mockSvc);
    }

    @Test
    void getAllTransactionsTest() {
        var response = new TransactionResponse(LocalDate.parse("2023-01-01"), "1", "", -20, "UNKNOWN");

        Mockito.when(mockSvc.getAllTransactions()).thenReturn(List.of(response));

        Assertions.assertThat(sut.getAllTransactions())
                .containsExactlyElementsOf(List.of(response));
    }

    @Test
    void getAllTransactionsInIntervalTest() {
        var response = new TransactionResponse(LocalDate.parse("2023-01-01"), "1", "", -20, "UNKNOWN");
        var dateFrom = LocalDate.now();
        var dateTo = LocalDate.now().plusDays(1);
        Mockito.when(mockSvc.getAllTransactionsInInterval(dateFrom,dateTo)).thenReturn(List.of(response));

        Assertions.assertThat(sut.getAllTransactionsInInterval(dateFrom,dateTo))
                .containsExactlyElementsOf(List.of(response));
    }

    @Test
    void updateClassificationTest() {
        var request = new UpdateClassificationRequest("123", "FOOD");
        var response = new ClassificationResponse("FOOD", "123");
        Mockito.when(mockSvc.updateClassification("123", "FOOD")).thenReturn(response);

        Assertions.assertThat(sut.updateClassification(request))
                .isEqualTo(response);
    }
}
