package org.example.port.out;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ClassificationClientTest {

    private ClassificationClient sut;

    @BeforeEach
    void setUp() {
        var filePath = "classpath:classificationServiceData.json";
        sut = new ClassificationClient(filePath);
    }

    @Test
    void getClassificationByRecipientTest() {
        Assertions.assertThat(sut.getClassifications()).isNotEmpty();
    }

    @Test
    void getClassificationByRecipientFileNotFoundTest() {
        var faultyFilepath = "classpath:classificationServiceData";
        var client = new ClassificationClient(faultyFilepath);

        Assertions.assertThat(client.getClassifications()).isNullOrEmpty();
    }

    @Test
    void changeClassificationTest() {
        var before = Map.copyOf(sut.getClassifications());

        sut.changeClassificationByRecipient("207177053", "UNKNOWN");

        var after = sut.getClassifications();

        Assertions.assertThat(before).isNotEqualTo(after);
    }
}
