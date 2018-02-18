package com.sale.message.processor.calc;


import com.sale.message.processor.model.Adjustment;
import com.sale.message.processor.model.Message;
import com.sale.message.processor.model.Operation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Calculator {

    public static Map<String, Double> calculateTotalValueForAllProductType(Map<String, List<Message>> productTypeToSaleData) {

        return productTypeToSaleData.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey(),
                entry -> entry.getValue().stream().map(m -> calculateTotalValue(m)).collect(Collectors.toList())
                        .stream().reduce(0.0, Double::sum)
        ));
    }

    private static Double calculateTotalValue(Message message) {

        Optional<Adjustment> adjustmentOptional = Optional.ofNullable(message.getAdjustment());
        Adjustment adjustment = adjustmentOptional.isPresent() ? adjustmentOptional.get() : null;
        double valueAfterAdjustment = 0;
        if (null != adjustment) {
            if (adjustment.getOperation().equals(Operation.ADD)) {
                valueAfterAdjustment = message.getQuantity() * (message.getValue() + adjustment.getValue());
            } else if (adjustment.getOperation().equals(Operation.SUBTRACT)) {
                valueAfterAdjustment = message.getQuantity() * (message.getValue() - adjustment.getValue());
            } else if (adjustment.getOperation().equals(Operation.MULTIPLY)) {
                valueAfterAdjustment = message.getQuantity() * (message.getValue() * adjustment.getValue());
            } else if (adjustment.getOperation().equals(Operation.DIVIDE)) {
                valueAfterAdjustment = message.getQuantity() * (message.getValue() / adjustment.getValue());
            }
        } else
            valueAfterAdjustment = message.getValue() * message.getQuantity();

        return valueAfterAdjustment;
    }

}
