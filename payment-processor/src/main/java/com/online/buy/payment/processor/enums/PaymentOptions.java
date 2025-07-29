package com.online.buy.payment.processor.enums;

public enum PaymentOptions {

    FULL_PAYMENT("Full Payment"),
    HALF_YEAR("Half Year"),
    QUARTERLY_PAYMENT("Quarterly Payment"),;

    private String option;

    PaymentOptions(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
