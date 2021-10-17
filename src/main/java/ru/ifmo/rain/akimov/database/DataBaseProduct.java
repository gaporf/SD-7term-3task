package ru.ifmo.rain.akimov.database;

public class DataBaseProduct {
    private final String product;
    private final long price;

    public DataBaseProduct(final String product, final long price) {
        this.product = product;
        this.price = price;
    }

    public final String getName() {
        return product;
    }

    public final long getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataBaseProduct)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final DataBaseProduct anotherProduct = (DataBaseProduct) obj;
        return product.equals(anotherProduct.product) && price == anotherProduct.price;
    }
}
