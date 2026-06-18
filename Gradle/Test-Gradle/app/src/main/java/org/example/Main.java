package org.example;

import org.example.utils.StringProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import java.util.Properties;
import java.io.InputStream;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Программа запущена");

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("build-passport.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                logger.info("Информация о сборке: Пользователь {}, ОС {}", 
                            prop.getProperty("build.user"), prop.getProperty("build.os"));
                logger.info("Сборка №: {}", prop.getProperty("build.number"));
                logger.info("Git Hash: {}", prop.getProperty("build.git.hash"));
            }
        } catch (Exception ex) {
            logger.error("Не удалось прочитать паспорт сборки", ex);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите строку: ");
        if (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String result = StringProcessor.process(input);
            logger.info("Результат обработки: {}", result);
        }

        logger.info("Программа завершена");
    } 
} 