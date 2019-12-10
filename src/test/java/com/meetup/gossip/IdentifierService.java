package com.meetup.gossip;

class IdentifierService {
    private Integer current = 0;
    public Integer next() {
        return ++current;
    }
}
