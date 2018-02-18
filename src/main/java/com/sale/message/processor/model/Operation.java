package com.sale.message.processor.model;

public enum Operation {
    ADD("Add"),
    SUBTRACT("Subtract"),
    MULTIPLY("Multiply"),
    DIVIDE("Divide");

    private String opDescription;

    Operation(String opDescription) {
        this.opDescription = opDescription;
    }

    public String getOpDescription() {
        return opDescription;
    }
}
