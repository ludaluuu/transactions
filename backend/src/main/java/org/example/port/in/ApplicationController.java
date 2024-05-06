package org.example.port.in;

import org.example.model.ClassificationResponse;
import org.example.model.TransactionResponse;
import org.example.model.UpdateClassificationRequest;
import org.example.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(final ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/transactions/all")
    @ResponseBody
    public List<TransactionResponse> getAllTransactions() {
        return applicationService.getAllTransactions();
    }

    @GetMapping("/transactions/date")
    @ResponseBody
    public List<TransactionResponse> getAllTransactionsInInterval(@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate fromDate,
                                                                  @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate toDate) {

        return applicationService.getAllTransactionsInInterval(fromDate, toDate);
    }

    @PostMapping("/transactions/update")
    @ResponseBody
    public ClassificationResponse updateClassification(@RequestBody final UpdateClassificationRequest request) {
        return applicationService.updateClassification(request.recipientId(), request.classification());
    }
}
