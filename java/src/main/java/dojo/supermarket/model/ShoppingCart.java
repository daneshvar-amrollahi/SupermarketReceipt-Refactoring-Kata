package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p: productQuantities().keySet()) {
            if (!offers.containsKey(p))
                continue;

            double quantity = productQuantities.get(p);
            Offer offer = offers.get(p);
            double unitPrice = catalog.getUnitPrice(p);            
            Discount discount = makeDiscount(p, quantity, offer, unitPrice);
            if (discount != null)
                receipt.addDiscount(discount);
        }
    }

    private Discount makeDiscount(Product p, double quantity, Offer offer, double unitPrice) {
        int quantityDenominator = getQuantityDenominator(offer);
        int quantityFloored = (int) quantity;
        int discountedQuantity = quantityFloored / quantityDenominator;
    
        if (offer.offerType == SpecialOfferType.TwoForAmount && quantityFloored >= 2) {
            double discountAmount = unitPrice * quantity - (offer.argument * discountedQuantity + (quantityFloored % 2) * unitPrice);
            return new Discount(p, "2 for " + offer.argument, -discountAmount);
        }
        if (offer.offerType == SpecialOfferType.ThreeForTwo && quantityFloored > 2) {
            double discountAmount = quantity * unitPrice - ((discountedQuantity * 2 * unitPrice) + quantityFloored % 3 * unitPrice);
            return new Discount(p, "3 for 2", -discountAmount);
        }
        if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
            double discountAmount = quantity * unitPrice * offer.argument / 100.0;
            return new Discount(p, offer.argument + "% off", -discountAmount);
        }
        if (offer.offerType == SpecialOfferType.FiveForAmount && quantityFloored >= 5) {
            double discountAmount = unitPrice * quantity - (offer.argument * discountedQuantity + quantityFloored % 5 * unitPrice);
            return new Discount(p, quantityDenominator + " for " + offer.argument, -discountAmount);
        }

        return null;
    }

    private int getQuantityDenominator(Offer offer) {
        switch (offer.offerType) {
            case TwoForAmount:
                return 2;
            case ThreeForTwo:
                return 3;
            case FiveForAmount:
                return 5;
            default:
                return 1;
        }
    }
}
