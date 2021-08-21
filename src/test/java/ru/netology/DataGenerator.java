package ru.netology;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    static Faker faker = new Faker(new Locale("ru-RU"));
    static String[] cities = new String[] {
            "Абакан", "Волгоград", "Вологда", "Кемерово", "Майкоп",
            "Москва", "Санкт-Петербург", "Самара", "Смоленск", "Тамбов"
    };
    static String invalidCity = "Абракадабра-на-Лене";

    public static String generateCity() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(10);
        return cities[randomIndex];
    }

    public static String generateName() {
        return faker.name().fullName().replaceAll("ё","е");
    }

    public static String generatePhone() {
        return faker.phoneNumber().phoneNumber().replaceAll("[^0-9+]", "");
    }

    public static String generateDate(int minShift, int maxShift) {
        Random random = new Random();
        int addDays = random.ints(minShift, maxShift).findFirst().getAsInt();
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static class Registration
    {
        public static UserInfo generateUser() {
            UserInfo user = new UserInfo(
                    generateCity(),
                    generateName(),
                    generatePhone()
            );
            return user;
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }

}
