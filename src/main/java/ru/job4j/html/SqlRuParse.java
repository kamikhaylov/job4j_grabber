package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.model.LoadingParts;
import ru.job4j.model.Post;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.SqlRuDateTimeParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private List<Post> posts = new ArrayList<>();
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public DateTimeParser getDateTimeParser() {
        return dateTimeParser;
    }

    @Override
    public List<Post> list(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            String linkPost = href.attr("href");
            posts.add(detail(linkPost));
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        LoadingParts load = new LoadingParts();
        return load.loading(link, getDateTimeParser());
    }

    public static void main(String[] args) throws Exception {
        int pages = 5;
        for (int i = 1; i <= pages; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            Elements dates = doc.select(".altCol");
            int index = 1;
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(dates.get(index).text());
                index = index + 2;
                System.out.println();
            }
        }
        DateTimeParser sqlParser = new SqlRuDateTimeParser();
        SqlRuParse parse = new SqlRuParse(sqlParser);
        List<Post> listPost = parse.list("https://www.sql.ru/forum/job-offers/");
        for (Post post : listPost) {
            System.out.println(post);
        }
    }
}