package com.meetup.gossip;

import java.util.List;
import java.util.stream.Collectors;

class GossipExchanger {

    public static final String NEVER = "never";
    private final List<Driver> drivers;
    private Integer turns;

    GossipExchanger(List<Driver> drivers) {
        this.drivers = drivers;
        drivers.forEach(driver ->
                drivers.forEach(driver::exchange));
        turns = 1;
    }

    public String turnsToExchangeAllGossips() {
        if(hasEveryoneExchangedAllGossips()) return turns.toString();
        while (turns < 480) {
            nextTurn();
            if(hasEveryoneExchangedAllGossips()) return turns.toString();
        }
        return NEVER;
    }

    public Driver getDriverById(Integer id) {
        return drivers.stream()
                .filter(driver -> driver.identifier().equals(id))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public void nextTurn() {
        drivers.forEach(Driver::moveNext);
        drivers.forEach(driver -> drivers.forEach(driver::exchange));
        turns++;
    }

    public boolean hasEveryoneExchangedAllGossips() {
        Integer gossips = drivers.size();
        List<Driver> driversWithAllGossips = drivers.stream().filter(driver -> driver.gossips().equals(gossips))
                .collect(Collectors.toList());
        return gossips.equals(driversWithAllGossips.size());
    }
}
