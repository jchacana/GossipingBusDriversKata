package com.meetup.gossip;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Route {

    private final Queue<Integer> stops;

    public Route(List<Integer> stops) {
        this.stops = new LinkedList<>(stops);
    }

    public Integer currentStop() {
        return this.stops.peek();
    }

    public void moveToNextStop() {
        Integer poll = stops.poll();
        stops.offer(poll);
    }

    public boolean isAtStop(Integer stop) {
        assert currentStop() != null;
        return currentStop().equals(stop);
    }
}
