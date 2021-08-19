package ru.job4j.grabber;

import ru.job4j.html.SqlRuParse;
import ru.job4j.model.Post;
import ru.job4j.quartz.AlertRabbit;
import ru.job4j.utils.SqlRuDateTimeParser;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("rabbit.driver-class-name"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("rabbit.url"),
                    cfg.getProperty("rabbit.username"),
                    cfg.getProperty("rabbit.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     cnn.prepareStatement(
                             "insert into posts(title, link, description, created_date)"
                                     + "values (?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement("select * from posts")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Timestamp timestamp = resultSet.getTimestamp("created_date");
                    posts.add(new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("link"),
                            resultSet.getString("description"),
                            timestamp.toLocalDateTime()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement =
                     cnn.prepareStatement("select * from posts where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp timestamp = resultSet.getTimestamp("created_date");
                    post = new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("link"),
                            resultSet.getString("description"),
                            timestamp.toLocalDateTime()
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Properties config;
        try (InputStream in = AlertRabbit.class
                .getClassLoader()
                .getResourceAsStream("rabbit.properties")) {
            config = new Properties();
            config.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        PsqlStore psqlStore = new PsqlStore(config);

        SqlRuDateTimeParser sqlParser = new SqlRuDateTimeParser();
        SqlRuParse parse = new SqlRuParse(sqlParser);
        List<Post> listPost = parse.list("https://www.sql.ru/forum/job-offers/");
        for (Post post : listPost) {
            psqlStore.save(post);
        }
        List<Post> posts = psqlStore.getAll();
        for (Post post : posts) {
            System.out.println(post);
            System.out.println();
        }
        System.out.println("findById 52: ");
        System.out.println(psqlStore.findById(52));
    }
}