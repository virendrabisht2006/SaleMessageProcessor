package com.sale.message.processor.model;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
public class Message {

    private String productType;

    private long quantity;

    private double value;

    private Adjustment adjustment;

    public Message(String productType, long quantity, double value, Adjustment adjustment) {
        this.productType = productType;
        this.quantity = quantity;
        this.value = value;
        this.adjustment = adjustment;
    }

    public String getProductType() {
        return productType;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getValue() {
        return value;
    }

    public Adjustment getAdjustment() {
        return adjustment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (quantity != message.quantity) return false;
        if (Double.compare(message.value, value) != 0) return false;
        if (!productType.equals(message.productType)) return false;
        return adjustment != null ? adjustment.equals(message.adjustment) : message.adjustment == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = productType.hashCode();
        result = 31 * result + (int) (quantity ^ (quantity >>> 32));
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (adjustment != null ? adjustment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "productType='" + productType + '\'' +
                ", quantity=" + quantity +
                ", value=" + value +
                ", adjustment=" + adjustment +
                '}';
    }
}
