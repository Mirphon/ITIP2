package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Приложение запущено");
        try {
            ObjectMapper mapper = new ObjectMapper();
            User user = new User("Mikhail", 19);    

            String jsonString = mapper.writeValueAsString(user);
            logger.info("Сериализация: {}", jsonString);

            User newUser = mapper.readValue(jsonString, User.class);
            logger.info("Десериализация: имя пользователя - {}", newUser.getName());

        } catch (Exception e) {
            logger.error("Ошибка в работе программы", e);
        }
    }
}