package com.safecornerscoffee.multiplication.controller;

import com.safecornerscoffee.multiplication.domain.MultiplicationResultAttempt;
import com.safecornerscoffee.multiplication.service.MultiplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public class MultiplicationResultAttemptController {

    private final MultiplicationService multiplicationService;

    public MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody final MultiplicationResultAttempt attempt) {
        boolean isCorrect = multiplicationService.checkAttempt(attempt);

        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt(
                attempt.getUser(),
                attempt.getMultiplication(),
                attempt.getResultAttempt(),
                isCorrect
        );

        return ResponseEntity.ok(resultAttempt);
    }

    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(multiplicationService.getStatusForUser(alias));
    }

}
