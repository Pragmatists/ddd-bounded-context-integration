package ddd.workshop.tickets.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ddd.workshop.tickets.domain.Ticket;
import ddd.workshop.tickets.domain.TicketNumber;
import ddd.workshop.tickets.domain.TicketRepository;

public class InMemoryTicketRepository implements TicketRepository {

    private List<Ticket> store = new ArrayList<>();
    
    @Override
    public void store(Ticket ticket) {
        findBy(ticket.number())
            .ifPresent(t -> { throw new TicketAlreadyExistsException(t.number()); }); 
        store.add(ticket);
    }

    @Override
    public Ticket load(TicketNumber number) {
        return findBy(number)
                .orElseThrow(() -> new TicketDoesNotExistException(number));
    }

    private Optional<Ticket> findBy(TicketNumber number) {
        return store.stream().filter(t -> t.number().equals(number)).findFirst();
    }

}
