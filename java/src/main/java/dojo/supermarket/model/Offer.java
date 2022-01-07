package dojo.supermarket.model;

public class Offer {
    private SpecialOfferType offerType;
    private final Product product;
    private double specialPrice;

    public Offer(SpecialOfferType offerType, Product product, double specialPrice) {
        this.offerType = offerType;
        this.specialPrice = specialPrice;
        this.product = product;
    }

    public SpecialOfferType getOfferType() {
        return offerType;
    }

    public double getSpecialPrice() {
        return specialPrice;
    }

    Product getProduct() {
        return this.product;
    }

}
