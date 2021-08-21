package ru.job4j.quartz;

import org.junit.Test;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AlertRabbitTest {
    @Test
    public void whenPropertiesThen5() {
        AlertRabbit rabbit = new AlertRabbit();
        rabbit.readSetting("rabbit.properties");
        int result = 600;
        assertThat(result, is(rabbit.getInterval()));
    }

    @Test
    public void whenPropertiesConnection() {
        AlertRabbit rabbit = new AlertRabbit();
        rabbit.readSetting("rabbit.properties");
        List<String> result = List.of("jdbc:postgresql://127.0.0.1:5432/rabbit_db",
                "postgres",
                "12345678",
                "org.postgresql.Driver");
        List<String> expected = List.of(rabbit.getConfig().getProperty("rabbit.url"),
                rabbit.getConfig().getProperty("rabbit.username"),
                rabbit.getConfig().getProperty("rabbit.password"),
                rabbit.getConfig().getProperty("rabbit.driver-class-name"));
        assertThat(result, is(expected));
    }
}