package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupByDemoInJava8 {

    public static void main(String args[]) {
        List<Person> people = new ArrayList<>();
        City city1 = new City("SZ", "描述SZ");
        City city2 = new City("SZ", "描述BJ");
        City city3 = new City("SH", "描述SH");
        people.add(new Person("John", city1, 21));
        people.add(new Person("Swann", city2, 21));
        people.add(new Person("Kevin", city2, 23));
        people.add(new Person("Monobo", city2, 23));
        people.add(new Person("Sam", city3, 23));
        people.add(new Person("Nadal", city3, 31));
        // Now let's group all person by city in pre Java 8 world
        Map<String, List<Person>> personByCity = new HashMap<>();

        // Let's see how we can group objects in Java 8
        personByCity = people.stream().collect(Collectors.groupingBy(entity -> entity.getCity().getName()));
        System.out.println("Person grouped by cities in Java 8: " + personByCity);
        // Now let's group person by age
        Map<Integer, List<Person>> personByAge = people.stream().collect(Collectors.groupingBy(Person::getAge));
        System.out.println("Person grouped by age in Java 8: " + personByAge);
    }
}
