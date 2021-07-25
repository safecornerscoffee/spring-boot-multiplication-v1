package com.safecornerscoffee.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safecornerscoffee.multiplication.domain.Multiplication;
import com.safecornerscoffee.multiplication.domain.MultiplicationResultAttempt;
import com.safecornerscoffee.multiplication.domain.User;
import com.safecornerscoffee.multiplication.service.MultiplicationService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    MultiplicationService multiplicationService;

    @Autowired
    MockMvc mockMvc;

    JacksonTester<MultiplicationResultAttempt> jsonResult;
    JacksonTester<MultiplicationResultAttempt> jsonResponse;

    JacksonTester<MultiplicationResultAttempt> jsonResultAttempt;
    JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getUserStatsTest() throws Exception {
        //given
        User user = new User("Emma Stone");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 3500, true);
        List<MultiplicationResultAttempt> recentAttempt = Lists.newArrayList(attempt, attempt);

        given(multiplicationService.getStatusForUser("Emma Stone")).willReturn(recentAttempt);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                get("/results").param("alias", "Emma Stone")
        ).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResultAttemptList.write(recentAttempt).getJson()
        );
    }

    @Test
    public void postResultReturnNotCorrect() throws Exception {
        genericParameterizedTest(false);
    }

    private void genericParameterizedTest(final boolean correct) throws Exception {
        //given
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class))).willReturn(correct);

        User user = new User("Emma Stone");
        Multiplication multiplication = new Multiplication(30, 40);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 1200, false);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                post("/results")
                        .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonResult.write(attempt).getJson())
        ).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResponse.write(
                        new MultiplicationResultAttempt(
                                attempt.getUser(),
                                attempt.getMultiplication(),
                                attempt.getResultAttempt(),
                                correct
                        )
                ).getJson()
        );
    }
}