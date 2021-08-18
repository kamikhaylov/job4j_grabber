package ru.job4j.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.job4j.utils.SqlRuDateTimeParser;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoadingParts {
    public Post loading(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements titles = doc.select(".messageHeader");
        Elements msgs = doc.select(".msgBody");
        Elements dates = doc.select(".msgFooter");
        SqlRuDateTimeParser sqlRuParser = new SqlRuDateTimeParser();
        String title = titles.get(0).text().substring(0, titles.get(0).text().indexOf(" ["));
        String description = msgs.get(1).text();
        String date = dates.get(0).text().substring(0, dates.get(0).text().indexOf(" ["));
        LocalDateTime created = sqlRuParser.parse(date);
        return new Post(title, link, description, created);
    }
}