package com.sale.message.processor.util;


import com.sale.message.processor.model.Adjustment;
import com.sale.message.processor.model.Message;
import com.sale.message.processor.model.Operation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DataLoader {

    private static final String DELIMETER = ",";

    private static final String EMPTY = " ";
    private final static Logger logger = Logger.getLogger(DataLoader.class);

    private DataLoader() {
    }

    public static List<Message> loadSales(String fileName) {
        List<Message> messages = new ArrayList<>();
        String file = String.valueOf(DataLoader.class.getClassLoader().getResource(fileName).getFile());

        try (Stream<String> lines = Files.lines(Paths.get(file)).skip(1)) {
            lines.forEach(line -> {
                String sales[] = line.split(DELIMETER);

                String productType = sales[0];

                long quantity = Long.valueOf(sales[1]);

                double value = Double.valueOf(sales[2]);

                Optional<String> operationOptional = Optional.ofNullable(sales[3]);
                Optional<String> adjustmentValueOptional = Optional.ofNullable(sales[4]);

                Operation operation = null;
                Double adjustmentValue = 0.0;
                Adjustment adjustment = null;

                if (operationOptional.isPresent() && adjustmentValueOptional.isPresent() && !operationOptional.get().equals(EMPTY)) {
                    operation = Operation.valueOf(operationOptional.get());
                    adjustmentValue = Double.valueOf(adjustmentValueOptional.get());
                    adjustment = Adjustment.builder().operation(operation).value(adjustmentValue).build();
                }

                Message message = Message.builder()
                        .productType(productType)
                        .quantity(quantity)
                        .value(value)
                        .adjustment(adjustment)
                        .build();

                messages.add(message);
            });

        } catch (IOException e) {
            logger.error("Exception occurred while reading file: ", e);
            e.printStackTrace();
        }

        return messages;
    }
}
