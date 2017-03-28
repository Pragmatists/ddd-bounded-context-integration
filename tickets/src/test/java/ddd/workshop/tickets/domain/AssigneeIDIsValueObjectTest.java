package ddd.workshop.tickets.domain;

import static org.assertj.core.api.Assertions.assertThat;

import utils.ValueObjectContractTest;

public class AssigneeIDIsValueObjectTest extends ValueObjectContractTest {

    @Override
    protected Object instance() {
        return new AssigneeID("homer@springfield.com");
    }

    @Override
    protected Object[] equalInstances() {
        return new Object[]{ new AssigneeID("homer@springfield.com") };
    }

    @Override
    protected Object[] nonEqualInstances() {
        return new Object[]{ new AssigneeID("bart@springfield.com") };
    }

    @Override
    public void shouldHaveDescriptiveTextualRepresentation() throws Exception {

        assertThat(instance().toString()).isEqualTo("homer@springfield.com");
        
    }

}
