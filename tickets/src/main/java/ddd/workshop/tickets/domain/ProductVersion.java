package ddd.workshop.tickets.domain;

import java.util.Objects;

public class ProductVersion {

    private String value;

    private ProductVersion(String value) {
        this.value = value;
    }
    
    public ProductVersion(String product, String version) {
        this(product + "-" + version);
    }

    public static ProductVersion of(String productAndVersion) {
        String[] parts = productAndVersion.split("-");
        return new ProductVersion(parts[0], parts[1]);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ProductVersion)) {
            return false;
        }

        ProductVersion other = (ProductVersion) obj;

        return Objects.equals(this.value, other.value);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash("ProductVersion", value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
