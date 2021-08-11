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
    private Connection connection;
    private String driver;
    private String url;
    private String username;
    private String password;
    private int interval;

    public Connection getConnection() {
        return connection;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getInterval() {
        return interval;
    }

    public void init() {
        try {
            Class.forName(getDriver());
            connection = DriverManager.getConnection(
                    getUrl(),
                    getUsername(),
                    getPassword()
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
            Properties config = new Properties();
            config.load(in);
            driver = config.getProperty("rabbit.driver-class-name");
            url = config.getProperty("rabbit.url");
            username = config.getProperty("rabbit.username");
            password = config.getProperty("rabbit.password");
            interval = Integer.parseInt(config.getProperty("rabbit.interval"));
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