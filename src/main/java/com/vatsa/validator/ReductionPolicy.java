package com.vatsa.validator;

import java.util.Objects;

public enum ReductionPolicy {

    /**
     * PROPAGATE Passes the Counter value as it is
     */
    PROPAGATE,

    /**
     * BINARY_AT_LEAST_ONE : The final result is binary, with total =1
     * the value of the counter is 1 if the org.value is greater than 0
     */
    BINARY_AT_LEAST_ONE,

    /**
     * BINARY_MAJORITY : The final result is binary with total = 1
     * the value of the counter is 1 if org.value*2 >= orb.total
     */
    BINARY_MAJORITY,

    /**
     * BINARY_ALL : the final result is binary, with total  = 1
     * the value of the counter is 1 if  orb.value == org.total
     */
    BINARY_ALL;


    public static boolean isBinary( ReductionPolicy reductionPolicy ){
        return (Objects.nonNull(reductionPolicy) && reductionPolicy != PROPAGATE );
    }
}
