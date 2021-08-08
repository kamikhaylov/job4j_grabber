[![Build Status](https://travis-ci.com/kamikhaylov/job4j_grabber.svg?branch=master)](https://travis-ci.com/kamikhaylov/job4j_grabber)
[![codecov](https://codecov.io/gh/kamikhaylov/job4j_grabber/branch/main/graph/badge.svg?token=415ADKU381)](https://codecov.io/gh/kamikhaylov/job4j_grabber)


# job4j_grabber
## Проект - Агрегатор вакансий

Используемые технологии: Java, Maven, Travis, Junit, Jacoco, Checkstyle, Codecov, Quartz.
Приложение собирается в jar.

Описание.

Система запускается по расписанию. Период запуска указывается в настройках - app.properties.
Первый сайт sql.ru. В нем есть раздел job. Программа считывает все вакансии относящиеся к Java и записывает их в базу.
Доступ к интерфейсу через REST API.

Расширение.

1. В проект можно добавить новые сайты без изменения кода.
2. В проекте можно сделать параллельный парсинг сайтов.