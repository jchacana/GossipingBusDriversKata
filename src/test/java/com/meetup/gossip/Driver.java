package com.meetup.gossip;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;

import static java.util.Arrays.asList;

class Driver {
    private final Queue<Integer> stopRoute;
    private final Integer identifier;
    private LinkedHashSet<Integer> receivedGossipsFrom;

    private Driver(Integer identifier, Integer... stopRoute) {
        this.identifier = identifier;
        this.stopRoute = new LinkedList<>(asList(stopRoute));
        this.receivedGossipsFrom = new LinkedHashSet<>();
        this.receivedGossipsFrom.add(identifier);
    }

    public Driver(IdentifierService identifierService, Integer... stopRoute) {
        this(identifierService.next(), stopRoute);
    }

    public boolean areAtTheSameStop(Driver driver) {
        assert this.stopRoute.peek() != null;
        return this.stopRoute.peek().equals(driver.stopRoute.peek());
    }

    public boolean isAtStop(Integer stop) {
        assert stopRoute.peek() != null;
        return stopRoute.peek().equals(stop);
    }

    public void moveNext() {
        Integer poll = stopRoute.poll();
        stopRoute.offer(poll);
    }

    public Integer gossips() {
        return receivedGossipsFrom.size();
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
        this.receivedGossipsFrom.add(driver.identifier);
        this.receivedGossipsFrom.addAll(driver.receivedGossipsFrom);
    }

    public Integer identifier() {
        return identifier;
    }
}
