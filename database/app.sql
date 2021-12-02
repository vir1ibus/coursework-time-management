-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Дек 02 2021 г., 18:43
-- Версия сервера: 8.0.24
-- Версия PHP: 8.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `app`
--

-- --------------------------------------------------------

--
-- Структура таблицы `statuses`
--

CREATE TABLE `statuses` (
  `id` int NOT NULL,
  `name` varchar(45) NOT NULL,
  `tasks_list_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Дамп данных таблицы `statuses`
--

INSERT INTO `statuses` (`id`, `name`, `tasks_list_id`) VALUES
(5, 'Test\n', 43),
(8, 'Test_2\n', 43),
(9, 'test_3\n', 43),
(10, 'test_4\n', 43),
(11, 'Test_5\n', 43),
(12, 'te\n', 43),
(13, 'dfy\n', 43);

-- --------------------------------------------------------

--
-- Структура таблицы `task`
--

CREATE TABLE `task` (
  `id` int NOT NULL,
  `tasks_list_id` int NOT NULL,
  `name` varchar(90) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `status_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Дамп данных таблицы `task`
--

INSERT INTO `task` (`id`, `tasks_list_id`, `name`, `description`, `status_id`) VALUES
(1, 43, 'Test', 'Test', 11);

-- --------------------------------------------------------

--
-- Структура таблицы `tasks_list`
--

CREATE TABLE `tasks_list` (
  `id` int NOT NULL,
  `name` varchar(90) NOT NULL,
  `everyone` tinyint(1) NOT NULL DEFAULT '0',
  `owner` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Дамп данных таблицы `tasks_list`
--

INSERT INTO `tasks_list` (`id`, `name`, `everyone`, `owner`) VALUES
(43, 'test', 1, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `tasks_list_has_users`
--

CREATE TABLE `tasks_list_has_users` (
  `tasks_list_id` int NOT NULL,
  `users_id` int NOT NULL,
  `privileges` varchar(10) NOT NULL DEFAULT 'viewer'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `login` varchar(45) NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `login`, `password`, `email`) VALUES
(0, 'root', '99adc231b045331e514a516b4b7680f588e3823213abe901738bc3ad67b2f6fcb3c64efb93d18002588d3ccc1a49efbae1ce20cb43df36b38651f11fa75678e8', '');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `statuses`
--
ALTER TABLE `statuses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_statuses_tasks_list1_idx` (`tasks_list_id`);

--
-- Индексы таблицы `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_task_statuses1_idx` (`status_id`),
  ADD KEY `fk_task_tasks_list1_idx` (`tasks_list_id`);

--
-- Индексы таблицы `tasks_list`
--
ALTER TABLE `tasks_list`
  ADD PRIMARY KEY (`id`),
  ADD KEY `owner` (`owner`);

--
-- Индексы таблицы `tasks_list_has_users`
--
ALTER TABLE `tasks_list_has_users`
  ADD PRIMARY KEY (`tasks_list_id`,`users_id`),
  ADD KEY `fk_tasks_list_has_users_users1_idx` (`users_id`),
  ADD KEY `fk_tasks_list_has_users_tasks_list_idx` (`tasks_list_id`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login_UNIQUE` (`login`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `statuses`
--
ALTER TABLE `statuses`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT для таблицы `task`
--
ALTER TABLE `task`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT для таблицы `tasks_list`
--
ALTER TABLE `tasks_list`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `statuses`
--
ALTER TABLE `statuses`
  ADD CONSTRAINT `fk_statuses_tasks_list1` FOREIGN KEY (`tasks_list_id`) REFERENCES `tasks_list` (`id`);

--
-- Ограничения внешнего ключа таблицы `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `fk_task_statuses1` FOREIGN KEY (`status_id`) REFERENCES `statuses` (`id`),
  ADD CONSTRAINT `fk_task_tasks_list1` FOREIGN KEY (`tasks_list_id`) REFERENCES `tasks_list` (`id`);

--
-- Ограничения внешнего ключа таблицы `tasks_list`
--
ALTER TABLE `tasks_list`
  ADD CONSTRAINT `tasks_list_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `users` (`id`);

--
-- Ограничения внешнего ключа таблицы `tasks_list_has_users`
--
ALTER TABLE `tasks_list_has_users`
  ADD CONSTRAINT `fk_tasks_list_has_users_tasks_list` FOREIGN KEY (`tasks_list_id`) REFERENCES `tasks_list` (`id`),
  ADD CONSTRAINT `fk_tasks_list_has_users_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
