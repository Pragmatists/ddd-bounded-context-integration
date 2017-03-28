package ddd.workshop.tickets.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ddd.workshop.tickets.domain.TicketFactory.Clock;

@RunWith(MockitoJUnitRunner.class)
public class TicketFactoryTest {

    @Mock
    private Clock clock;
    
    private TicketFactory factory;

    @Before
    public void setUp() {
        factory = new TicketFactory(clock);
    }
    
    @Test
    public void shouldCreateTicketInInitialState() throws Exception {

        // given:
        
        // when:
        Ticket ticket = factory.newTicket();
        
        // then:
        assertThat(ticket.status()).isEqualTo(Status.OPEN);
    }

    @Test
    public void shouldAssignUniqueNumber() throws Exception {

        // given:
        
        // when:
        Ticket ticket1 = factory.newTicket();
        Ticket ticket2 = factory.newTicket();
        
        // then:
        assertThat(ticket1.number()).isNotEqualTo(ticket2.number());
    }
    
    @Test
    public void shouldAssignCreationDate() throws Exception {
        
        // given:
        currentTimeIs("2017-01-02T12:00:00");
        
        // when:
        Ticket ticket = factory.newTicket();
        
        // then:
        assertThat(ticket.createdAt()).isEqualTo(aTime("2017-01-02T12:00:00"));
    }
    
    // --
    
    private void currentTimeIs(String time) {
        Mockito.when(clock.time()).thenReturn(LocalDateTime.parse(time));
    }

    private LocalDateTime aTime(String value) {
        return LocalDateTime.parse(value);
    }
    
}
