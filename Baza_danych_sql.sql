CREATE TABLE `butelka` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `rodzaj` varchar(255),
  `pojemnosc` float,
  `urzadzenie_id` int,
  `czy_nalane` bool
);

CREATE TABLE `uzytkownicy` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `imie` varchar(255)
);

CREATE TABLE `urzadzenie` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `adres` varchar(255),
  `nazwa` varchar(255)
);

CREATE TABLE `zlozone_zamowienie` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `butelka_id` int,
  `zamowienie_id` int,
  `ilosc` float,
  `czy_wykonane` bool
);

CREATE TABLE `zamowienia` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `uzytkownicy_id` int
);

ALTER TABLE `zlozone_zamowienie` ADD FOREIGN KEY (`butelka_id`) REFERENCES `butelka` (`id`);

ALTER TABLE `butelka` ADD FOREIGN KEY (`urzadzenie_id`) REFERENCES `urzadzenie` (`id`);

ALTER TABLE `zamowienia` ADD FOREIGN KEY (`uzytkownicy_id`) REFERENCES `uzytkownicy` (`id`);

ALTER TABLE `zlozone_zamowienie` ADD FOREIGN KEY (`zamowienie_id`) REFERENCES `zamowienia` (`id`);
