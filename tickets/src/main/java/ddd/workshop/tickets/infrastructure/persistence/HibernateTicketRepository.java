package ddd.workshop.tickets.infrastructure.persistence;

import java.util.Optional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import ddd.workshop.tickets.domain.Ticket;
import ddd.workshop.tickets.domain.TicketNumber;
import ddd.workshop.tickets.domain.TicketRepository;

@Repository
public class HibernateTicketRepository implements TicketRepository {

    private Session session;

    public HibernateTicketRepository(Session session) {
        this.session = session;
    }

    @Override
    public void store(Ticket ticket) {
        findBy(ticket.number())
            .ifPresent(t -> {throw new TicketAlreadyExistsException(t.number()); });
        session.save(ticket);
    }

    private Optional<Ticket> findBy(TicketNumber number) {
        return Optional.ofNullable(session.get(Ticket.class, number));
    }

    @Override
    public Ticket load(TicketNumber number) {
        
        return findBy(number).orElseThrow(() -> new TicketDoesNotExistException(number));
    }

}
