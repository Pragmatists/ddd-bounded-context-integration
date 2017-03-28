package ddd.workshop.tickets.domain;

import static ddd.workshop.tickets.domain.Ticket.RelatedTicket.RelationshipType.DUPLICATED_BY;
import static ddd.workshop.tickets.domain.Ticket.RelatedTicket.RelationshipType.DUPLICATES;
import static ddd.workshop.tickets.domain.Ticket.RelatedTicket.RelationshipType.BLOCKED_BY;
import static ddd.workshop.tickets.domain.Ticket.RelatedTicket.RelationshipType.BLOCKS;
import static ddd.workshop.tickets.domain.Ticket.RelatedTicket.RelationshipType.RELATED_TO;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import ddd.workshop.tickets.domain.Ticket.RelatedTicket.RelationshipType;
import ddd.workshop.tickets.infrastructure.persistence.InMemoryTicketRepository;

public class TicketRelationshipServiceTest {

    private TicketRepository tickets;
    private TicketRelationshipService service;
    
    private Ticket source;
    private Ticket target;

    @Before
    public void setUp() {
        tickets = new InMemoryTicketRepository();
        service = new TicketRelationshipService(tickets);
    }
    
    @Test
    public void shouldMarkAsDuplicate() throws Exception {

        // given:
        followingTicketsExist(
            source = aTicket(),
            target = aTicket()
        );
        
        // when:
        service.markAsDuplicate(source.number(), target.number());
        
        // then:
        sourceIsRefereingAs(DUPLICATES);
        targetIsReferedAs(DUPLICATED_BY);
    }

    @Test
    public void shouldMarkAsBlockedBy() throws Exception {

        // given:
        followingTicketsExist(
            source = aTicket(),
            target = aTicket()
        );
        
        // when:
        service.markAsBlockedBy(source.number(), target.number());
        
        // then:
        sourceIsRefereingAs(BLOCKED_BY);
        targetIsReferedAs(BLOCKS);
    }

    @Test
    public void shouldMarkAsReletedTo() throws Exception {

        // given:
        followingTicketsExist(
            source = aTicket(),
            target = aTicket()
        );
            
        // when:
        service.markAsReletedTo(source.number(), target.number());
        
        // then:
        sourceIsRefereingAs(RELATED_TO);
        targetIsReferedAs(RELATED_TO);
    }

    // --
    
    private void followingTicketsExist(Ticket... manyTickets) {
        for (Ticket ticket : manyTickets) {
            tickets.store(ticket);
        }
    }

    private void targetIsReferedAs(RelationshipType relationshipType) {
        assertThat(tickets.load(target.number()).hasRelationshipTo(source.number(), relationshipType)).isTrue();
    }

    private void sourceIsRefereingAs(RelationshipType relationshipType) {
        assertThat(tickets.load(source.number()).hasRelationshipTo(target.number(), relationshipType)).isTrue();
    }

    private Ticket aTicket() {
        return new Ticket(TicketNumber.next());
    }
    
}
