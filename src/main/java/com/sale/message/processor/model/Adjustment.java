package com.sale.message.processor.model;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
public class Adjustment {

    private Operation operation;

    private double value;

    public Adjustment(Operation operation, double value) {
        this.operation = operation;
        this.value = value;
    }

    public Operation getOperation() {
        return operation;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Adjustment{" +
                "operation=" + operation +
                ", value=" + value +
                '}';
    }
}
