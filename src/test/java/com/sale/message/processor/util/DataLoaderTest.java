package com.sale.message.processor.util;


import com.sale.message.processor.model.Message;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DataLoaderTest {

    @Test
    public void shouldLoadSalesDataFromFile() {
        List<Message> messageList = DataLoader.loadSales("sample-sale.csv");
        assertThat(messageList).isNotNull();
        assertThat(messageList.size()).isGreaterThan(1);

    }
}