package ddd.workshop.tickets.domain;

import static org.assertj.core.api.Assertions.assertThat;

import utils.ValueObjectContractTest;

public class StatusIsValueObjectTest extends ValueObjectContractTest {

    @Override
    protected Status instance() {
        return Status.OPEN;
    }

    @Override
    protected Object[] equalInstances() {
        return new Object[] { Status.OPEN };
    }

    @Override
    protected Object[] nonEqualInstances() {
        return new Object[] { Status.ASSIGNED, Status.CLOSED, Status.CANCELLED };
    }

    @Override
    public void shouldHaveDescriptiveTextualRepresentation() throws Exception {

        // given:
        Status status = instance();

        // when:
        String representation = status.toString();

        // then:
        assertThat(representation).isEqualTo("OPEN");

    }
}
