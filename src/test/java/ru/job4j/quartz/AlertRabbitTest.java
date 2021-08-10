package ru.job4j.quartz;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AlertRabbitTest {
    @Test
    public void whenPropertiesThen10() {
        int result = 5;
        assertThat(result, is(AlertRabbit.getInterval("rabbit.properties")));
    }
}