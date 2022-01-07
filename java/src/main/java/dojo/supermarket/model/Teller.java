package dojo.supermarket.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teller {

    private final SupermarketCatalog catalog;
    private Map<Product, Offer> offers = new HashMap<>();

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double specialPrice) {
        this.offers.put(product, new Offer(offerType, product, specialPrice));
    }

    public Receipt checksOutArticlesFrom(ShoppingCart shoppingCart) {
        Receipt receipt = new Receipt();
        List<ProductQuantity> productQuantities = shoppingCart.getItems();
        for (ProductQuantity productQuantity: productQuantities) {
            Product product = productQuantity.getProduct();
            double quantity = productQuantity.getQuantity();
            double unitPrice = this.catalog.getUnitPrice(product);
            double price = quantity * unitPrice;
            receipt.addProduct(product, quantity, unitPrice, price);
        }
        shoppingCart.handleOffers(receipt, this.offers, this.catalog);

        return receipt;
    }

}
