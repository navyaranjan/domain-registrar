package domainregistrar;

import java.io.Serializable;

public class DomainType implements Serializable {

    private String domain;
    private double price;
    private Type type;

    public DomainType() {

    }

    public DomainType(String domain, double price, Type type) {
        this.domain = domain;
        this.price = price;
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}

enum Type {

    PREMIUM, NORMAL

}