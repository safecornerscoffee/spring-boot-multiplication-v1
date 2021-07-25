package com.safecornerscoffee.multiplication.repository;

import com.safecornerscoffee.multiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {
}
