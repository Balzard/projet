package adri.suys.un_mutescan.model;

import java.io.Serializable;

/**
 * A counterpart is a type of ticket
 * It can be a standard ticket, a child ticket, a vip ticket, a ticket for disabled person, etc.
 * It has a price
 * The quantity is only used when we want to buy tickets, this is the quantity of ticket to be sold.
 */
public class Counterpart implements Serializable {

    private final int id;
    private final String name;
    private final double price;
    private int quantity = 0;

    public Counterpart(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
