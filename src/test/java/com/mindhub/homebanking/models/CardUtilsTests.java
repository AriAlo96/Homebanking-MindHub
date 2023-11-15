package com.mindhub.homebanking.models;

import com.mindhub.homebanking.utils.CardUtils;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CardUtilsTests {
    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.generateNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }
    @Test
    public void cardNumberDigits(){
        String cardNumber = CardUtils.generateNumber();
        assertThat(cardNumber.length(), equalTo(19));
    }
    @Test
    public void cardNCvvIsCreated(){
        int cardCvv = CardUtils.generateCvv();
        assertThat(cardCvv,is(notNullValue()));
    }
    @Test
    public void cardCvvIsANumber(){
        int cardCvv = CardUtils.generateCvv();
        assertThat(cardCvv,(instanceOf(Integer.class)));
    }
}
