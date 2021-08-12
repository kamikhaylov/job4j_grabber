package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit implements AutoCloseable {
    private Properties config;
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public Properties getConfig() {
        return config;
    }

    public int getInterval() {
        return Integer.parseInt(config.getProperty("rabbit.interval"));
    }

    public void init() {
        try {
            Class.forName(config.getProperty("rabbit.driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("rabbit.url"),
                    config.getProperty("rabbit.username"),
                    config.getProperty("rabbit.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static class Rabbit implements Job {
        public Rabbit() {
            System.out.println("hashCode " + hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail()
                    .getJobDataMap()
                    .get("store");
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into rabbit(created_date) values (?)")) {
                statement.setLong(1, System.currentTimeMillis());
                statement.execute();
                System.out.println("currentTimeMillis " + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readSetting(String properties) {
        try (InputStream in = AlertRabbit.class
                .getClassLoader()
                .getResourceAsStream(properties)) {
            config = new Properties();
            config.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        AlertRabbit rabbit = new AlertRabbit();
        rabbit.readSetting("rabbit.properties");
        try {
            rabbit.init();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("store", rabbit.getConnection());
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(rabbit.getInterval())
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}