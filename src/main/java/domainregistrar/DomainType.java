package domainregistrar;

public class DomainType {

    private String zone;
    private double price;
    private Type type;

    public DomainType(String zone, double price, Type type) {
        this.zone = zone;
        this.price = price;
        this.type = type;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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