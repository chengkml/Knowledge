package org.springframework.lang;

import org.junit.Test;

import javax.annotation.Nonnull;

public class NullableTest {

    @Test
    public void test(){
        returnNotNull(null);
    }

    @Nonnull
    private String returnNotNull(@Nonnull String a){
        return null;
    }
}
