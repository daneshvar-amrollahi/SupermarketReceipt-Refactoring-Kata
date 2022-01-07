package dojo.supermarket.model;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    double specialPrice;

    public Offer(SpecialOfferType offerType, Product product, double specialPrice) {
        this.offerType = offerType;
        this.specialPrice = specialPrice;
        this.product = product;
    }

    Product getProduct() {
        return this.product;
    }

}
