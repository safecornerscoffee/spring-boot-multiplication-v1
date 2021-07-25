package com.safecornerscoffee.multiplication.service;

import com.safecornerscoffee.multiplication.domain.Multiplication;
import com.safecornerscoffee.multiplication.domain.MultiplicationResultAttempt;
import com.safecornerscoffee.multiplication.domain.User;
import com.safecornerscoffee.multiplication.repository.MultiplicationResultAttemptRepository;
import com.safecornerscoffee.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class MultiplicationServiceImplTest{

    private MultiplicationService multiplicationService;

    @Mock
    RandomGeneratorService randomGeneratorService;

    @Mock
    MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        multiplicationService = new MultiplicationServiceImpl(
                randomGeneratorService, attemptRepository, userRepository);
    }

    @Test
    void createRandomMultiplicationTest() {
        //given
        given(randomGeneratorService.generateRandomFactor()).willReturn(30, 40);

        //when
        Multiplication multiplication = multiplicationService.createRandomMultiplication();

        //then
        assertThat(multiplication.getFactorA()).isEqualTo(30);
        assertThat(multiplication.getFactorB()).isEqualTo(40);
        assertThat(multiplication.getResult()).isEqualTo(1200);
    }

    @Test
    void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(30, 40);
        User user = new User("Emma Stone");
        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 1200, false);
        MultiplicationResultAttempt verifiedAttempt =
                new MultiplicationResultAttempt(user, multiplication, 1200, true);

        given(userRepository.findByAlias("Emma Stone")).willReturn(Optional.empty());

        //when
        boolean result = multiplicationService.checkAttempt(attempt);

        assertThat(result).isTrue();
        then(attemptRepository).should().save(verifiedAttempt);
    }

    @Test
    void checkWrongAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(30, 40);
        User user = new User("Emma Stone");
        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 1501, false);

        given(userRepository.findByAlias("Emma Stone")).willReturn(Optional.empty());

        //when
        boolean result = multiplicationService.checkAttempt(attempt);

        //then
        assertThat(result).isFalse();
        then(attemptRepository).should().save(attempt);

    }


    @Test
    void retrieveStatsTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Emma Stone");
        MultiplicationResultAttempt attempt1 =
                new MultiplicationResultAttempt(user, multiplication, 3001, false);
        MultiplicationResultAttempt attempt2 =
                new MultiplicationResultAttempt(user, multiplication, 3002, false);
        List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);

        given(userRepository.findByAlias("Emma Stone")).willReturn(Optional.of(user));
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("Emma Stone")).willReturn(latestAttempts);

        //when
        List<MultiplicationResultAttempt> latestAttemptsResult = multiplicationService.getStatusForUser("Emma Stone");

        //then
        assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
    }


}