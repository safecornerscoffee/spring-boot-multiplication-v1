package com.safecornerscoffee.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Multiplication {

    @Id
    @GeneratedValue
    @Column(name = "multiplication_id")
    private Long id;

    private final int factorA;
    private final int factorB;

    private final int result;

    public Multiplication(int factorA, int factorB) {
        this.factorA = factorA;
        this.factorB = factorB;
        this.result = factorA * factorB;
    }

    protected Multiplication() {
        this(0, 0);
    }
}
