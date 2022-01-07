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
            if (!offers.containsKey(p)) {
                continue;
            }
            
            double quantity = productQuantities.get(p);
            Offer offer = offers.get(p);
            double unitPrice = catalog.getUnitPrice(p);
            int quantityAsInt = (int) quantity;
            Discount discount = null;
            
            int quantityDenominator = getQuantityDenominator(offer);
            int numberOfXs = quantityAsInt / quantityDenominator;

            if (offer.offerType == SpecialOfferType.TwoForAmount && quantityAsInt >= 2) {
                // int intDivision = quantityAsInt / x;
                int intDivision = numberOfXs;
                double pricePerUnit = offer.argument * intDivision;
                double theTotal = (quantityAsInt % 2) * unitPrice;
                double total = pricePerUnit + theTotal;
                double discountN = unitPrice * quantity - total;
                discount = new Discount(p, "2 for " + offer.argument, -discountN);
            }

            if (offer.offerType == SpecialOfferType.ThreeForTwo && quantityAsInt > 2) {
                double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
                discount = new Discount(p, "3 for 2", -discountAmount);
            }
            if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                discount = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
            }
            if (offer.offerType == SpecialOfferType.FiveForAmount && quantityAsInt >= 5) {
                double discountTotal = unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice);
                discount = new Discount(p, quantityDenominator + " for " + offer.argument, -discountTotal);
            }
            if (discount != null)
                receipt.addDiscount(discount);

        }
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
