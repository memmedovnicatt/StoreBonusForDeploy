package com.nicat.storebonus.utility;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ThresholdsUtil {
    public BigDecimal calculateMiddleThreshold(BigDecimal a, BigDecimal b) {
        return a.add(b).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
    }
}