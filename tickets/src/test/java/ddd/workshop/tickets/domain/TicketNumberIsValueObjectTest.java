package ddd.workshop.tickets.domain;

import static org.assertj.core.api.Assertions.assertThat;

import utils.ValueObjectContractTest;

public class TicketNumberIsValueObjectTest extends ValueObjectContractTest {

    @Override
    protected TicketNumber instance() {
        return new TicketNumber("42");
    }

    @Override
    protected Object[] equalInstances() {
        return new Object[] { new TicketNumber("42") };
    }

    @Override
    protected Object[] nonEqualInstances() {
        return new Object[] { new TicketNumber("24") };
    }

    @Override
    public void shouldHaveDescriptiveTextualRepresentation() throws Exception {

        // given:
        TicketNumber number = instance();

        // when:
        String representation = number.toString();

        // then:
        assertThat(representation).isEqualTo("42");
    }

}
