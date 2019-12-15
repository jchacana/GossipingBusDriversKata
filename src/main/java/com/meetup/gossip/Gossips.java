package com.meetup.gossip;

import java.util.Collection;
import java.util.LinkedHashSet;

public class Gossips {

    private LinkedHashSet<Integer> gossips;

    public Gossips(LinkedHashSet<Integer> gossips) {
        this.gossips = gossips;
    }

    public Integer size() {
        return gossips.size();
    }

    public void add(Integer gossipFrom) {
        this.gossips.add(gossipFrom);
    }

    public void addAll(Gossips gossips) {
        this.gossips.addAll(gossips.gossips);
    }
}
