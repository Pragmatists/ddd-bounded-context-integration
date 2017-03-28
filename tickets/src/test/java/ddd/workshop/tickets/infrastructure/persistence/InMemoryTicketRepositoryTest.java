package ddd.workshop.tickets.infrastructure.persistence;

import ddd.workshop.tickets.domain.TicketRepository;
import ddd.workshop.tickets.domain.TicketRepositoryContractTest;

public class InMemoryTicketRepositoryTest extends TicketRepositoryContractTest {

    @Override
    public TicketRepository freshReposiotry() {
        return new InMemoryTicketRepository();
    }

}
