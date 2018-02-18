package com.sale.message.processor;

import com.sale.message.processor.calc.Calculator;
import com.sale.message.processor.model.Adjustment;
import com.sale.message.processor.model.Message;
import com.sale.message.processor.model.Operation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

    private static final String PRODUCT_TYPE = "Apple";
    private Map<String, List<Message>> productTypeSaleData;

    @Before
    public void init() {
        productTypeSaleData = new HashMap<>();
        List<Message> messages = new ArrayList<>();
        messages.add(prepareMessageData(PRODUCT_TYPE, 100, 25.0, null, 0));
        messages.add(prepareMessageData(PRODUCT_TYPE, 125, 26.5, Operation.ADD, 1.5));
        productTypeSaleData.put(PRODUCT_TYPE, messages);
    }

    @Test
    public void shouldCalculateTotalValueForAllProductType() throws Exception {
        Double expectedTotalValue = 6000.0;
        Map<String, Double> productTypeToTotalValue = Calculator.calculateTotalValueForAllProductType(productTypeSaleData);
        double actualTotalValue = productTypeToTotalValue.get(PRODUCT_TYPE);
        assertThat(actualTotalValue).isEqualTo(expectedTotalValue);

        productTypeSaleData.get(PRODUCT_TYPE).add(prepareMessageData(PRODUCT_TYPE, 50, 120, Operation.SUBTRACT, 5.0));

        Map<String, Double> productTypeToTotalValue1 = Calculator.calculateTotalValueForAllProductType(productTypeSaleData);
        double actualTotalValue1 = productTypeToTotalValue1.get(PRODUCT_TYPE);
        double expectedValueAfterSubtractAdjustment = 6000 + 50 * 115;
        assertThat(actualTotalValue1).isEqualTo(expectedValueAfterSubtractAdjustment);


    }

    private Message prepareMessageData(String productType, long quantity, double value, Operation op, double adjustValue) {
        return Message.builder()
                .productType(productType)
                .quantity(quantity)
                .value(value)
                .adjustment((op != null) ? Adjustment.builder().operation(op).value(adjustValue).build() : null)
                .build();
    }

}