package ddd.workshop.tickets.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import ddd.workshop.tickets.domain.TicketRepository.TicketAlreadyExistsException;
import ddd.workshop.tickets.domain.TicketRepository.TicketDoesNotExistException;

@Commit
@Transactional
public abstract class TicketRepositoryContractTest  {

    public abstract TicketRepository freshReposiotry();

    private TicketRepository reposiotry;

    @Before
    public void setUp() {
        reposiotry = freshReposiotry();
    }
    
    @Test
    public void shouldStoreAndLoadMinimalTicket() throws Exception {

        // given:
        Ticket stored = aTicket();
        
        // when:
        reposiotry.store(stored);
        Ticket loaded = reposiotry.load(stored.number());
        
        // then:
        assertThat(loaded)
            .isEqualToComparingFieldByField(stored);
    }

    @Test
    public void shouldStoreAndLoadComplexTicket() throws Exception {

        // given:
        Ticket stored = aComplexTicket();
        
        // when:
        reposiotry.store(stored);
        Ticket loaded = reposiotry.load(stored.number());
        
        // then:
        assertThat(loaded)
            .isEqualToIgnoringGivenFields(stored, "related");   // FIXME: shitty PersistentSet =/= PersistentSet 
    }
    
    
    @Test
    public void shouldFailMeaningfullyWhenLoadingNonExistentTicket() throws Exception {

        // then:
        assertThatThrownBy(
                () -> reposiotry.load(new TicketNumber("404"))
        ).isInstanceOf(TicketDoesNotExistException.class); 
    }

    @Test
    public void shouldFailMeaningfullyWhenStoringTicketWithSameID() throws Exception {

        // given:
        Ticket ticket = aTicket();
        
        ticketsAlreadyExists(ticket);
        
        // then:
        assertThatThrownBy(
                () -> reposiotry.store(ticket)
        ).isInstanceOf(TicketAlreadyExistsException.class); 
    }

    
    // --
    
    private void ticketsAlreadyExists(Ticket... tickets) {
        for(Ticket ticket: tickets){
            reposiotry.store(ticket);
        }
    }

    private Ticket aTicket() {
        return new Ticket(new TicketNumber("23"), LocalDateTime.parse("2017-01-02T12:34:56"));
    }

    private Ticket aComplexTicket() {
        
        Ticket ticket = aTicket();
        ticket.renameTo("Nothing works!");
        ticket.updateDescription("This is a very, very long description of an issue...");
        ticket.assignTo(new AssigneeID("homer@springfield.com"));
        ticket.markAsBeingDuplicatedBy(new TicketNumber("88"));
        ticket.blockedBy(new TicketNumber("77"));
        ticket.fixedIn(new ProductVersion("acb", "1.2.3"));
        
        return ticket;
    }
    
}

