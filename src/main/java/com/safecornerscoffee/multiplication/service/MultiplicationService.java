package com.safecornerscoffee.multiplication.service;

import com.safecornerscoffee.multiplication.domain.Multiplication;
import com.safecornerscoffee.multiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

    Multiplication createRandomMultiplication();

    boolean checkAttempt(final MultiplicationResultAttempt attempt);

    List<MultiplicationResultAttempt> getStatusForUser(String userAlias);
}
