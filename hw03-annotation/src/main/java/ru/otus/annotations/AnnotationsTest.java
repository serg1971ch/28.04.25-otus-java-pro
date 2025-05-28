package ru.otus.annotations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.annotation.After;
import ru.otus.annotations.annotation.Before;
import ru.otus.annotations.annotation.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class AnnotationsTest {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationsTest.class);

    Calc calc;

    private int a;
    private int b;

    @Before
    public void setUpA(){
        a = 2;
        logger.info("------Set up. Start a value: {}", a);
    }

    @Before
    public void setUpB(){
        b = 3;
        logger.info("------Set up. Start b value: {}", b);
    }

    @Before
    public void setUp() {
        calc = new Calc(a, b);
    }

    @After
    public void tearDownA(){
        a = -1;
        logger.info("------Teardown. a end value: {}", a);
    }

    @After
    public void tearDownB(){
        b = -1;
        logger.info("------Teardown. b end value: {}\n", b);
    }

    @Test
    public void testNotEqual(){
        logger.info("------Starting test: testNotEqual");
        assertThat(calc.add()).isNotEqualTo(b);
        logger.info("------Result - OK");
    }


    @Test
    public void testEqual(){
        logger.info("------Starting test: testEqual");
        assertThat(a).isEqualTo(b);
        logger.info("------Result - OK");
    }



    @Test
    public void testException(){
        logger.info("------Starting test: testException");
        throw new NullPointerException("NPE test");
    }
}
