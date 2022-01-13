package com.technogise.interns.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    public void testTwoPlusTwoEqualFour() {
        int FIRST_NUMBER = 2, SECOND_NUMBER = 2, EXPECTED_SUM = 4;
        Calculator calculator = new Calculator();
        int actualSum = calculator.sum(FIRST_NUMBER,SECOND_NUMBER);
        Assertions.assertEquals(EXPECTED_SUM,actualSum);
    }

}