package com.meetup.gossip;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class GossipExchanger {

    public static final String NEVER = "never";
    public static final int MAX_MINUTES = 480;
    private final List<Driver> drivers;
    private Integer turns;

    GossipExchanger(List<Driver> drivers) {
        this.drivers = drivers;
        drivers.forEach(driver ->
                drivers.forEach(driver::exchange));
        turns = 1;
    }

    public String turnsToExchangeAllGossips() {
        String turnsStr = turns.toString();
        while (turns < MAX_MINUTES && !hasEveryoneExchangedAllGossips()) {
            turnsStr = nextTurn();
        }
        return turnsStr;
    }

    public Driver getDriverById(Integer id) {
        return drivers.stream()
                .filter(driver -> driver.identifier().equals(id))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public String nextTurn() {
        drivers.forEach(Driver::moveNext);
        drivers.forEach(driver -> drivers.forEach(driver::exchange));
        turns++;
        return turns >= MAX_MINUTES? NEVER: turns.toString();
    }

    public boolean hasEveryoneExchangedAllGossips() {
        Integer gossips = drivers.size();
        List<Driver> driversWithAllGossips = drivers.stream().filter(driver -> driver.gossips().equals(gossips))
                .collect(Collectors.toList());
        return gossips.equals(driversWithAllGossips.size());
    }
}
