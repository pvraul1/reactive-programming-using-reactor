package com.learnreactiveprogramming.imperative;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImperativeExample {

    public static void main(String[] args) {
        var nameList = List.of("alex", "ben", "chloe", "adam", "emma", "maya", "sophia", "adam", "ben", "chloe");

        var newNamesList = namesGreaterThanSize(nameList, 3);
        System.out.println("newNamesList = " + newNamesList);
    }

    private static List<String> namesGreaterThanSize(List<String> nameList, int size) {
        return nameList
                .parallelStream()
                .filter(name -> name.length() > size)
                .map(String::toUpperCase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
