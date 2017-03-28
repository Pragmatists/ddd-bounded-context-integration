package ddd.workshop.tickets.domain;

import static org.assertj.core.api.Assertions.assertThat;

import utils.ValueObjectContractTest;

public class ProductVersionIsValueObjectTest extends ValueObjectContractTest {

    @Override
    protected Object instance() {
        return ProductVersion.of("abc-1.2.0");
    }

    @Override
    protected Object[] equalInstances() {
        return new Object[] { ProductVersion.of("abc-1.2.0"), new ProductVersion("abc", "1.2.0")};
    }

    @Override
    protected Object[] nonEqualInstances() {
        return new Object[] { ProductVersion.of("cab-1.2.0"), ProductVersion.of("abc-1.2.1")};
    }

    @Override
    public void shouldHaveDescriptiveTextualRepresentation() throws Exception {

        assertThat(instance().toString()).isEqualTo("abc-1.2.0");
        
    }

}
