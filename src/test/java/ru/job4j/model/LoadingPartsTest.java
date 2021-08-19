package ru.job4j.model;

import org.junit.Test;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoadingPartsTest {
    @Test
    public void whenLinkThenPost() throws IOException {
        LoadingParts load = new LoadingParts();
        DateTimeParser dateTimeParser = new SqlRuDateTimeParser();
        String link = "https://www.sql.ru/forum/1325330/"
                + "lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
        Post expected = load.loading(link, dateTimeParser);
        Post result = new Post(
                "Лиды BE/FE/senior cистемные аналитики/QA и DevOps, Москва, до 200т.",
                link,
                "Ищем команды разработчиков на аутстафф в офис в Москве, а также"
                        + " тимлидов и ведущих системных аналитиков, QA и DevOps Зп до 200т."
                        + " Наши приоритеты стаффинга: 1. Команды, микрокоманды: - команда -"
                        + " 7-9 человек (типовой состав - 2 аналитика, 3 Java, 2 React, 2 QA);"
                        + " - микрокоманда - 3-4 человека (специальность - BE/FE/QA/QA"
                        + " автотестирование). Ключевые специалисты команды должны сделать"
                        + " минимум 1 проект вместе. 2. Лиды BE/FE/senior cистемные"
                        + " аналитики/QA и DevOps. Наш стек: Backend:"
                        + " java/kotlin, spring Frontend: react Mobile: kotlin/Android, Swift/iOS"
                        + " Предложения и резюме присылайте на contact.softlantic@gmail.com",
                LocalDateTime.of(2020, 05, 13, 21, 58)
        );
        assertThat(result, is(expected));
    }
}