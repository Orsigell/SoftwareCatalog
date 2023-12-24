-- Вставка реалистичных данных для Категорий
INSERT INTO categories (category_name) VALUES
('Среда разработки'),
('Графический дизайн'),
('Проектирование'),
('test1'),
('Видеомонтаж'),
('test4'),
('Работа с данными'),
('3D-моделирование'),
('Текстовый редактор'),
('Видеоэффекты'),
('test2'),
('test3'),
('Звукозапись'),
('Научные расчеты'),
('test7'),
('Веб-браузер'),
('Игровая разработка'),
('test5'),
('test6'),
('test8'),
('test9'),
('test10'),
('test11'),
('Бесплатное ПО'),
('test12'),
('test13');


-- Вставка реалистичных данных для Комментариев
INSERT INTO comments (comment) VALUES
    ('Я просто не могу нарадоваться на программу!'),
    ('Visual Studio - отличная среда разработки!'),
    ('Adobe Illustrator - мощный инструмент для дизайна.'),
    ('Autodesk AutoCAD помогает мне создавать проекты.'),
    ('Mozilla Firefox - самый быстрый браузер!'),
    ('IntelliJ IDEA - мой выбор для разработки на Java.'),
    ('Final Cut Pro - профессиональная программа для видеомонтажа.'),
    ('Unity - отличная платформа для разработки игр.'),
    ('Adobe Premiere Pro - профессиональная программа для видеомонтажа.'),
    ('Microsoft Excel помогает мне в работе с данными.'),
    ('Blender - мощный инструмент для создания 3D-графики.'),
    ('Sublime Text - легкий и гибкий текстовый редактор.'),
    ('Adobe After Effects - отличный инструмент для создания видеоэффектов.'),
    ('Xcode - мой выбор для разработки под iOS.'),
    ('PyCharm - лучшая среда разработки для Python.'),
    ('Audacity - простое в использовании программное обеспечение для звукозаписи.'),
    ('MATLAB - мощный инструмент для научных расчетов и визуализации данных.'),
    ('Atom - открытый текстовый редактор, разработанный компанией GitHub.'),
    ('test'),
    ('Лучшая среда разработки'),
    ('t');


INSERT INTO screens (screen) VALUES
  ('1.jpg'),
  ('2.jpg'),
  ('Снимок экрана 2023-05-30 125011.jpg'),
  ('Снимок экрана 2023-05-30 125146.jpg'),
  ('cpuid-hwmonitor-download.jpg'),
  ('softwares-hwpro13.png'),
  ('3.jpg'),
  ('Снимок экрана 2023-05-30 125011.jpg'),
  ('Снимок экрана 2023-05-30 125103.jpg'),
  ('Снимок экрана 2023-05-30 125146.jpg'),
  ('Снимок экрана 2023-05-30 170936.jpg');

-- Вставка реалистичных данных для Программного обеспечения
INSERT INTO software (name, description, image, link, system_requirements, license_name, license_type, license_price, license_duration)
VALUES
('StellarStudio', 'StellarStudio - это интегрированная среда разработки для создания красочных и впечатляющих графических проектов. Она предлагает широкий спектр инструментов и эффектов, чтобы воплотить в жизнь ваше творчество.', 'softwares-hwpro13.png', 'www.stellarstudio.com', 'Windows 10, процессор Intel Core i5, 8 ГБ оперативной памяти, 500 ГБ свободного места на жестком диске.', 'Pro License', 'Полная', 199, 0),
('Visual Studio', 'Visual Studio — интегрированная среда разработки программного обеспечения от Microsoft.', 'visualstudio.jpg', 'http://www.visualstudio.com', 'Windows 10, 8 ГБ ОЗУ, 20 ГБ свободного места на диске', 'Microsoft', 'Подписка', 1499, 12),
('Adobe Illustrator', 'Adobe Illustrator — профессиональное программное обеспечение для создания и редактирования векторной графики.', 'illustrator.jpg', 'http://www.adobe.com/products/illustrator.html', 'Windows 10, 4 ГБ ОЗУ, 2 ГБ свободного места на диске', 'Adobe Creative Cloud', 'Подписка', 999, 12),
('Autodesk AutoCAD', 'Autodesk AutoCAD — программное обеспечение для 2D и 3D проектирования и черчения.', 'autocad.jpg', 'http://www.autodesk.com/autocad', 'Windows 10, 8 ГБ ОЗУ, 10 ГБ свободного места на диске', 'Autodesk', 'Подписка', 1999, 12),
('Mozilla Firefox', 'Mozilla Firefox — популярный веб-браузер с открытым исходным кодом.', 'firefox.jpg', 'http://www.mozilla.org/firefox', 'Windows 10, 2 ГБ ОЗУ, 200 МБ свободного места на диске', 'Mozilla', 'Бесплатная', 0, 0),
('IntelliJ IDEA', 'IntelliJ IDEA — интегрированная среда разработки программного обеспечения для языков Java, Kotlin, Groovy и других.', 'intellij.jpg', 'http://www.jetbrains.com/idea', 'Windows 10, 4 ГБ ОЗУ, 500 МБ свободного места на диске', 'JetBrains', 'Подписка', 499, 12),
('Final Cut Pro', 'Final Cut Pro — профессиональная программа для редактирования видео, разработанная компанией Apple.', 'finalcutpro.jpg', 'http://www.apple.com/final-cut-pro', 'macOS 10.15 и выше, 8 ГБ ОЗУ, 3.8 ГБ свободного места на диске', 'Apple', 'Подписка', 2999, 12),
('Unity', 'Unity — интегрированная среда разработки игр и визуализаций.', 'unity.jpg', 'http://www.unity.com', 'Windows 10, 4 ГБ ОЗУ, 20 ГБ свободного места на диске', 'Unity Technologies', 'Бесплатная', 0, 0),
('Adobe Premiere Pro', 'Adobe Premiere Pro — профессиональная программа для редактирования видео.', 'premierepro.jpg', 'http://www.adobe.com/products/premiere.html', 'Windows 10, 8 ГБ ОЗУ, 8 ГБ свободного места на диске', 'Adobe Creative Cloud', 'Подписка', 999, 12),
('Microsoft Excel', 'Microsoft Excel — программа для работы с электронными таблицами.', 'excel.jpg', 'http://www.microsoft.com/excel', 'Windows 10, 4 ГБ ОЗУ, 2 ГБ свободного места на диске', 'Microsoft', 'Подписка', 999, 12),
('Blender', 'Blender — свободное программное обеспечение для создания трехмерной компьютерной графики.', 'blender.jpg', 'http://www.blender.org', 'Windows 10, 4 ГБ ОЗУ, 5 ГБ свободного места на диске', 'Blender Foundation', 'Бесплатная', 0, 0),
('Sublime Text', 'Sublime Text — текстовый редактор с широкими возможностями настройки и расширения функционала.', 'sublimetext.jpg', 'http://www.sublimetext.com', 'Windows 10, 2 ГБ ОЗУ, 200 МБ свободного места на диске', 'Sublime HQ', 'Подписка', 99, 12),
('Adobe After Effects', 'Adobe After Effects — программное обеспечение для создания и редактирования видеоэффектов и анимации.', 'aftereffects.jpg', 'http://www.adobe.com/products/aftereffects.html', 'Windows 10, 8 ГБ ОЗУ, 5 ГБ свободного места на диске', 'Adobe Creative Cloud', 'Подписка', 999, 12),
('Xcode', 'Xcode — интегрированная среда разработки для приложений под iOS и macOS.', 'xcode.jpg', 'http://developer.apple.com/xcode', 'macOS 10.15 и выше, 8 ГБ ОЗУ, 20 ГБ свободного места на диске', 'Apple', 'Бесплатная', 0, 0),
('PyCharm', 'PyCharm — интегрированная среда разработки для языка Python.', 'pycharm.jpg', 'http://www.jetbrains.com/pycharm', 'Windows 10, 4 ГБ ОЗУ, 500 МБ свободного места на диске', 'JetBrains', 'Подписка', 499, 12),
('Audacity', 'Audacity — свободное программное обеспечение для обработки звука и звукозаписи.', 'audacity.jpg', 'http://www.audacityteam.org', 'Windows 10, 2 ГБ ОЗУ, 100 МБ свободного места на диске', 'Audacity Team', 'Бесплатная', 0, 0),
('MATLAB', 'MATLAB — высокоуровневый язык программирования и среда разработки, предназначенная для численных расчетов и визуализации данных.', 'matlab.jpg', 'http://www.mathworks.com/matlab', 'Windows 10, 4 ГБ ОЗУ, 5 ГБ свободного места на диске', 'MathWorks', 'Подписка', 1999, 12),
('Atom', 'Atom — открытый текстовый редактор, разработанный компанией GitHub.', 'atom.jpg', 'http://www.atom.io', 'Windows 10, 4 ГБ ОЗУ, 500 МБ свободного места на диске', 'GitHub', 'Бесплатная', 0, 0);

-- Вставка реалистичных данных для связи между Программным обеспечением и Категориями
-- Пример для связи программ с категориями
-- Establish relationships between software and categories
INSERT INTO software_categories (software_id, category_id) VALUES
-- Software 1 (StellarStudio) - Multiple categories
(1, 1), (1, 2), (1, 6),
-- Software 2 (Visual Studio) - Multiple categories
(2, 1), (2, 4), (2, 11),
-- Software 3 (Adobe Illustrator) - Multiple categories
(3, 2), (3, 5), (3, 11),
-- Software 4 (Autodesk AutoCAD) - Multiple categories
(4, 3), (4, 5), (4, 11),
-- Software 5 (Mozilla Firefox) - Multiple categories
(5, 17), (5, 11),
-- Software 6 (IntelliJ IDEA) - Multiple categories
(6, 1), (6, 11), (6, 12),
-- Software 7 (Final Cut Pro) - Multiple categories
(7, 5), (7, 11), (7, 15),
-- Software 8 (Unity) - Multiple categories
(8, 18), (8, 11),
-- Software 9 (Adobe Premiere Pro) - Multiple categories
(9, 5), (9, 11), (9, 15),
-- Software 10 (Microsoft Excel) - Multiple categories
(10, 7), (10, 11),
-- Software 11 (Blender) - Multiple categories
(11, 18), (11, 11), (11, 5),
-- Software 12 (Sublime Text) - Multiple categories
(12, 9), (12, 11),
-- Software 13 (Adobe After Effects) - Multiple categories
(13, 5), (13, 11), (13, 15),
-- Software 14 (Xcode) - Multiple categories
(14, 14), (14, 11),
-- Software 15 (PyCharm) - Multiple categories
(15, 6), (15, 11), (15, 12),
-- Software 16 (Audacity) - Multiple categories
(16, 13), (16, 11),
-- Software 17 (MATLAB) - Multiple categories
(17, 14), (17, 11), (17, 5),
-- Software 18 (Atom) - Multiple categories
(18, 9), (18, 11), (18, 17);





-- Establish relationships between software and comments
INSERT INTO software_comments (software_id, comment_id) VALUES
-- Software 1 (StellarStudio) - Single comment
(1, 1),
-- Software 2 (Visual Studio) - Single comment
(2, 2),
-- Software 3 (Adobe Illustrator) - Single comment
(1, 3),
-- Software 4 (Autodesk AutoCAD) - Single comment
(1, 4),
-- Software 5 (Mozilla Firefox) - Single comment
(1, 5),
-- Software 6 (IntelliJ IDEA) - Single comment
(1, 6),
-- Software 7 (Final Cut Pro) - Single comment
(3, 7),
-- Software 8 (Unity) - Single comment
(1, 8),
-- Software 9 (Adobe Premiere Pro) - Single comment
(1, 9),
-- Software 10 (Microsoft Excel) - Single comment
(3, 10),
-- Software 11 (Blender) - Single comment
(1, 11),
-- Software 12 (Sublime Text) - Single comment
(3, 12),
-- Software 13 (Adobe After Effects) - Single comment
(3, 13),
-- Software 14 (Xcode) - Single comment
(3, 14),
-- Software 15 (PyCharm) - Single comment
(3, 15),
-- Software 16 (Audacity) - Single comment
(3, 16),
-- Software 17 (MATLAB) - Single comment
(3, 17),
-- Software 18 (Atom) - Single comment
(3, 18);



INSERT INTO software_screens (software_id, screen_id) VALUES
-- Software 1 (StellarStudio) - Single comment
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(3, 5),
(3, 6),
(3, 7),
(4, 8),
(4, 9),
(1, 10),
(1, 11);