package com.meetup.gossip;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.meetup.gossip.GossipExchanger.NEVER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;

public class GossipTest {

    private IdentifierService identifierService;

    @Before
    public void initTest() {
        identifierService = new IdentifierService();
    }

    @Test
    public void three_drivers_with_defined_route_should_end_at_turn_five() {
        Driver driver_1 = new Driver(identifierService, 3, 1, 2, 3);
        Driver driver_2 = new Driver(identifierService, 3, 2, 3, 1);
        Driver driver_3 = new Driver(identifierService, 4, 2, 3, 4, 5);

        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2, driver_3));
        Assert.assertThat(gossipExchanger.turnsToExchangeAllGossips(), is("5"));
    }

    @Test
    public void two_drivers_with_defined_route_should_never_exchange_all_gossips() {
        Driver driver_1 = new Driver(identifierService, 2, 1, 2);
        Driver driver_2 = new Driver(identifierService, 5, 2, 8);

        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2));
        Assert.assertThat(gossipExchanger.turnsToExchangeAllGossips(), is(NEVER));
    }

    @Test
    public void three_drivers_with_defined_route_should_end_at_turn_five_step_by_step() {
        Driver driver_1 = new Driver(identifierService, 3, 1, 2, 3);
        Driver driver_2 = new Driver(identifierService, 3, 2, 3, 1);
        Driver driver_3 = new Driver(identifierService, 4, 2, 3, 4, 5);

        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2, driver_3));


        Assert.assertThat(driver_1.gossips(), is(2));
        Assert.assertThat(driver_2.gossips(), is(2));
        Assert.assertThat(driver_3.gossips(), is(1));
        Assert.assertThat(gossipExchanger.hasEveryoneExchangedAllGossips(), is(false));

        gossipExchanger.nextTurn();

        Assert.assertThat(driver_1.gossips(), is(2));
        Assert.assertThat(driver_2.gossips(), is(3));
        Assert.assertThat(driver_3.gossips(), is(3));
        Assert.assertThat(gossipExchanger.hasEveryoneExchangedAllGossips(), is(false));

        gossipExchanger.nextTurn();

        Assert.assertThat(driver_1.gossips(), is(2));
        Assert.assertThat(driver_2.gossips(), is(3));
        Assert.assertThat(driver_3.gossips(), is(3));
        Assert.assertThat(gossipExchanger.hasEveryoneExchangedAllGossips(), is(false));

        gossipExchanger.nextTurn();
        Assert.assertThat(driver_1.gossips(), is(2));
        Assert.assertThat(driver_2.gossips(), is(3));
        Assert.assertThat(driver_3.gossips(), is(3));
        Assert.assertThat(gossipExchanger.hasEveryoneExchangedAllGossips(), is(false));

        gossipExchanger.nextTurn();
        Assert.assertThat(driver_1.gossips(), is(3));
        Assert.assertThat(driver_2.gossips(), is(3));
        Assert.assertThat(driver_3.gossips(), is(3));
        Assert.assertThat(gossipExchanger.hasEveryoneExchangedAllGossips(), is(true));

    }


    @Test
    public void two_drivers_which_starts_at_the_same_stop() {
        Assert.assertThat(new GossipExchanger(asList(new Driver(identifierService, 1), new Driver(identifierService, 1))).turnsToExchangeAllGossips(), is("1"));
    }

    @Test
    public void two_drivers_which_dont_start_at_same_stop() {
        Driver driver_1 = new Driver(identifierService,1, 2);
        Driver driver_2 = new Driver(identifierService, 2);
        Assert.assertThat(new GossipExchanger(asList(driver_1, driver_2)).turnsToExchangeAllGossips(), is("2"));
    }

    @Test
    public void two_drivers_which_will_never_meet() {
        Driver driver_1 = new Driver(identifierService,1);
        Driver driver_2 = new Driver(identifierService, 2);

        Assert.assertThat(new GossipExchanger(asList(driver_1, driver_2)).turnsToExchangeAllGossips(), is(NEVER));
    }

    @Test
    public void two_drivers_at_same_stop_have_exchanged_all_gossips() {
        Driver driver_1 = new Driver(identifierService,1);
        Driver driver_2 = new Driver(identifierService, 1);
        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2));
        Assert.assertThat(gossipExchanger.hasEveryoneExchangedAllGossips(), is(true));
    }

    @Test
    public void two_drivers_at_same_different_stop_have_not_exchanged_all_gossips() {
        Driver driver_1 = new Driver(identifierService,1);
        Driver driver_2 = new Driver(identifierService, 2);
        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2));
        Assert.assertThat(gossipExchanger.hasEveryoneExchangedAllGossips(), is(false));
    }

    @Test
    public void driver_at_first_iteration_should_be_at_first_stop() {
        GossipExchanger gossipExchanger = new GossipExchanger(singletonList(new Driver(identifierService, 1, 2)));
        Assert.assertThat(gossipExchanger.getDriverById(1).isAtStop(1), is(true));
    }

    @Test
    public void driver_at_second_iteration_should_be_at_second_stop() {
        GossipExchanger gossipExchanger = new GossipExchanger(singletonList(new Driver(identifierService, 1, 2)));
        gossipExchanger.nextTurn();
        Assert.assertThat(gossipExchanger.getDriverById(1).isAtStop(2), is(true));
    }

    @Test
    public void drivers_at_second_iteration_should_be_at_second_stop_each() {
        GossipExchanger gossipExchanger = new GossipExchanger(asList(new Driver(identifierService, 1, 2), new Driver(identifierService, 2, 1)));
        gossipExchanger.nextTurn();
        Assert.assertThat(gossipExchanger.getDriverById(1).isAtStop(2), is(true));
        Assert.assertThat(gossipExchanger.getDriverById(2).isAtStop(1), is(true));
    }

    @Test
    public void drivers_at_different_stop_at_first_iteration_should_not_exchange_gossip() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        Driver driver_2 = new Driver(identifierService, 2, 1);
        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2));
        Assert.assertThat(driver_1.gossips(), is(1));
        Assert.assertThat(driver_2.gossips(), is(1));
    }

    @Test
    public void drivers_at_different_stop_at_second_iteration_should_not_exchange_gossip() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        Driver driver_2 = new Driver(identifierService, 2, 1);
        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2));
        gossipExchanger.nextTurn();
        Assert.assertThat(driver_1.gossips(), is(1));
        Assert.assertThat(driver_2.gossips(), is(1));
    }

    @Test
    public void drivers_at_same_stop_at_second_iteration_should_exchange_gossip() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        Driver driver_2 = new Driver(identifierService, 2);
        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2));
        gossipExchanger.nextTurn();
        Assert.assertThat(driver_1.gossips(), is(2));
        Assert.assertThat(driver_2.gossips(), is(2));
    }

    @Test
    public void three_drivers_at_same_stop_at_third_iteration_should_exchange_all_gossips() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        Driver driver_2 = new Driver(identifierService, 2);
        Driver driver_3 = new Driver(identifierService, 1);
        GossipExchanger gossipExchanger = new GossipExchanger(asList(driver_1, driver_2, driver_3));
        gossipExchanger.nextTurn();
        gossipExchanger.nextTurn();
        Assert.assertThat(driver_1.gossips(), is(3));
        Assert.assertThat(driver_2.gossips(), is(3));
        Assert.assertThat(driver_3.gossips(), is(3));
    }

    @Test
    public void identifierservice_should_return_one_on_next_number() {
        Assert.assertThat(identifierService.next(), is(1));
    }

    @Test
    public void identifierservice_should_return_two_on_twice_next_number() {
        identifierService.next();
        Assert.assertThat(identifierService.next(), is(2));
    }

    @Test
    public void driver_should_be_at_first_stop() {
        Driver driver_1 = new Driver(identifierService, 1);
        Assert.assertThat(driver_1.isAtStop(1), is(true));
    }

    @Test
    public void driver_should_be_at_second_stop() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        driver_1.moveNext();
        Assert.assertThat(driver_1.isAtStop(2), is(true));
    }

    @Test
    public void driver_should_be_at_first_stop_after_second_move() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        driver_1.moveNext();
        driver_1.moveNext();
        Assert.assertThat(driver_1.isAtStop(1), is(true));
    }

    @Test
    public void driver_with_4_stops_same_last_first_stop_should_visit_twice() {
        Driver driver = new Driver(identifierService, 3, 1, 2, 3);
        driver.moveNext();
        Assert.assertThat(driver.isAtStop(1), is(true));
        driver.moveNext();
        Assert.assertThat(driver.isAtStop(2), is(true));
        driver.moveNext();
        Assert.assertThat(driver.isAtStop(3), is(true));
        driver.moveNext();
        Assert.assertThat(driver.isAtStop(3), is(true));
        driver.moveNext();
        Assert.assertThat(driver.isAtStop(1), is(true));
    }

    @Test
    public void drivers_should_be_at_first_stop() {
        Driver driver_1 = new Driver(identifierService, 1);
        Driver driver_2 = new Driver(identifierService, 1);
        Assert.assertThat(driver_1.areAtTheSameStop(driver_2), is(true));
    }

    @Test
    public void drivers_should_be_at_second_stop() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        driver_1.moveNext();
        Driver driver_2 = new Driver(identifierService, 1, 2);
        driver_2.moveNext();
        Assert.assertThat(driver_1.isAtStop(2), is(true));
        Assert.assertThat(driver_1.areAtTheSameStop(driver_2), is(true));
    }

    @Test
    public void driver_should_have_one_gossip() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        Assert.assertThat(driver_1.gossips(), is(1));
    }

    @Test
    public void driver_should_have_an_identifier_based_on_order() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        Driver driver_2 = new Driver(identifierService, 1, 2);
        Assert.assertThat(driver_1.identifier(), is(1));
        Assert.assertThat(driver_2.identifier(), is(2));
    }

    @Test
    public void drivers_at_same_stop_should_exchange_gossip() {
        Driver driver_1 = new Driver(identifierService, 1);
        Driver driver_2 = new Driver(identifierService, 1);

        Assert.assertThat(driver_1.exchange(driver_2), is(true));
    }

    @Test
    public void drivers_at_different_stop_should_not_exchange_gossip() {
        Driver driver_1 = new Driver(identifierService, 1);
        Driver driver_2 = new Driver(identifierService, 2);

        Assert.assertThat(driver_1.exchange(driver_2), is(false));
    }

    @Test
    public void two_drivers_at_same_stop_should_have_two_gossips() {
        Driver driver_1 = new Driver(identifierService, 1);
        Driver driver_2 = new Driver(identifierService, 1);
        driver_1.exchange(driver_2);
        Assert.assertThat(driver_1.gossips(), is(2));
        Assert.assertThat(driver_2.gossips(), is(2));
    }

    @Test
    public void two_drivers_at_same_stops_twice_should_have_two_gossips() {
        Driver driver_1 = new Driver(identifierService, 1, 2);
        Driver driver_2 = new Driver(identifierService, 1, 2);
        driver_1.exchange(driver_2);
        Assert.assertThat(driver_1.gossips(), is(2));
        Assert.assertThat(driver_2.gossips(), is(2));
    }

    @Test
    public void two_drivers_at_different_stop_should_have_one_gossip() {
        Driver driver_1 = new Driver(identifierService, 1);
        Driver driver_2 = new Driver(identifierService, 2);
        driver_1.exchange(driver_2);
        Assert.assertThat(driver_1.gossips(), is(1));
        Assert.assertThat(driver_2.gossips(), is(1));
    }

    @Test
    public void three_drivers_at_same_stop_should_have_three_gossips() {
        Driver driver_1 = new Driver(identifierService, 1);
        Driver driver_2 = new Driver(identifierService, 1);
        Driver driver_3 = new Driver(identifierService, 1);
        driver_1.exchange(driver_2);
        driver_1.exchange(driver_3);
        driver_2.exchange(driver_3);
        Assert.assertThat(driver_1.gossips(), is(3));
        Assert.assertThat(driver_2.gossips(), is(3));
        Assert.assertThat(driver_3.gossips(), is(3));
    }

}
