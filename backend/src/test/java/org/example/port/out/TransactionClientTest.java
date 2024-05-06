package org.example.port.out;

import org.assertj.core.api.Assertions;
import org.example.model.TransactionClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class TransactionClientTest {

    private TransactionClient sut;

    @BeforeEach
    void setUp() {
        var filepath = "classpath:transactionServiceData.json";
        sut = new TransactionClient(filepath);
    }

    @Test
    void getAllResponsesCouldNotReadFile() {
        var faultyFilepath = "classpath:transactionServiceData";
        var cli = new TransactionClient(faultyFilepath);

        Assertions.assertThat(cli.getTransactions()).isNullOrEmpty();
    }

    @Test
    void getAllResponsesTest() {
        var res = sut.getTransactions();
        var anyResponse = new TransactionClientResponse(
                "2023-06-10",
                "373685732",
                "Bauhaus Solna", -204
        );

        Assertions.assertThat(res).isNotEmpty();
        Assertions.assertThat(res).containsAnyOf(anyResponse);
    }
}
