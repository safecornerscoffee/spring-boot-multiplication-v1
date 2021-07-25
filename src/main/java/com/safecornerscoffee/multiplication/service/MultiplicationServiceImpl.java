package com.safecornerscoffee.multiplication.service;

import com.safecornerscoffee.multiplication.domain.Multiplication;
import com.safecornerscoffee.multiplication.domain.MultiplicationResultAttempt;
import com.safecornerscoffee.multiplication.domain.User;
import com.safecornerscoffee.multiplication.repository.MultiplicationResultAttemptRepository;
import com.safecornerscoffee.multiplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MultiplicationServiceImpl implements MultiplicationService {

    private final RandomGeneratorService randomGeneratorService;
    private final MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;
    private final UserRepository userRepository;

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Override
    @Transactional
    public boolean checkAttempt(MultiplicationResultAttempt attempt) {
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

        Assert.isTrue(!attempt.isCorrect(), "Bad Request");

        Multiplication multiplication = attempt.getMultiplication();
        boolean correct = attempt.getResultAttempt() == multiplication.getFactorA() * multiplication.getFactorB();

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
                user.orElse(attempt.getUser()),
                attempt.getMultiplication(),
                attempt.getResultAttempt(),
                correct
        );

        multiplicationResultAttemptRepository.save(checkedAttempt);

        return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatusForUser(String userAlias) {
        return multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }
}
