package com.meetup.gossip;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;

import static java.util.Arrays.asList;

class Driver {
    private final Route route;
    private final Integer identifier;
    private final Gossips gossips;

    private Driver(Integer identifier, Integer... stopRoute) {
        this.identifier = identifier;
        this.gossips = new Gossips(new LinkedHashSet<>(asList(identifier)));
        this.route = new Route(asList(stopRoute));
    }

    public Driver(IdentifierService identifierService, Integer... stopRoute) {
        this(identifierService.next(), stopRoute);
    }

    public Integer currentStop() {
        return route.currentStop();
    }

    public boolean areAtTheSameStop(Driver driver) {
        return route.isAtStop(driver.currentStop());
    }

    public boolean isAtStop(Integer stop) {
        return route.isAtStop(stop);
    }

    public void moveNext() {
        route.moveToNextStop();
    }

    public Integer gossips() {
        return gossips.size();
    }

    public boolean exchange(Driver driver) {
        if(this.areAtTheSameStop(driver)) {
            this.receiveGossip(driver);
            driver.receiveGossip(this);
            return true;
        }
        return false;
    }

    private void receiveGossip(Driver driver) {
        this.gossips.add(driver.identifier);
        this.gossips.addAll(driver.gossips);
    }

    public Integer identifier() {
        return identifier;
    }
}
