package ddd.workshop.tickets.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class TicketStateMachineTest {

    @Test
    @Parameters(method="validTransitions")
    public void shouldTransitToState(Ticket ticket, Consumer<Ticket> action, Status expectedState) throws Exception {

        // given:
        
        // when:
        action.accept(ticket);
        
        // then:
        assertThat(ticket.status()).isEqualTo(expectedState);
        
    }
    
    @Test
    @Parameters(method="invalidTransitions")
    public void shouldProtectFromInvalidTransition(Ticket ticket, Consumer<Ticket> action) throws Exception {
        
        // given:
        Status initialState = ticket.status();
        
        try{
            
            // when:
            action.accept(ticket);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
            
        } catch(Exception e){
            
            // then:
            assertThat(ticket.status()).isEqualTo(initialState);
            assertThat(e)
                .isInstanceOf(IllegalStateException.class);
        }

    }
    
    public Object[] validTransitions(){
        return $(
                $(openTicket(), transitionUsing(this::assign), Status.ASSIGNED),
                $(openTicket(), transitionUsing(this::fix), Status.RESOLVED),
                $(openTicket(), transitionUsing(this::markAsDuplicate), Status.RESOLVED),
                $(openTicket(), transitionUsing(this::markAsWontFix), Status.RESOLVED),
                $(openTicket(), transitionUsing(this::cannotReproduce), Status.RESOLVED),

                $(assignedTicket(), transitionUsing(this::assign), Status.ASSIGNED),
                $(assignedTicket(), transitionUsing(this::fix), Status.RESOLVED),
                $(assignedTicket(), transitionUsing(this::markAsDuplicate), Status.RESOLVED),
                $(assignedTicket(), transitionUsing(this::markAsWontFix), Status.RESOLVED),
                
                $(resolvedTicket(), transitionUsing(this::assign), Status.RESOLVED),
                $(resolvedTicket(), transitionUsing(this::fix), Status.RESOLVED),
                $(resolvedTicket(), transitionUsing(this::markAsDuplicate), Status.RESOLVED),
                $(resolvedTicket(), transitionUsing(this::markAsWontFix), Status.RESOLVED),

                $(resolvedTicket(), transitionUsing(this::close), Status.CLOSED),
                $(resolvedTicket(), transitionUsing(this::reopen), Status.OPEN),
                
                $(closedTicket(), transitionUsing(this::reopen), Status.OPEN)
               );
    }
    
    public Object[] invalidTransitions(){

        return $(
                $(openTicket(), transitionUsing(this::close)),
                $(openTicket(), transitionUsing(this::reopen)),
                
                $(assignedTicket(), transitionUsing(this::close)),
                $(assignedTicket(), transitionUsing(this::reopen)),
                
                $(closedTicket(), transitionUsing(this::assign)),
                $(closedTicket(), transitionUsing(this::close)),
                $(closedTicket(), transitionUsing(this::fix)),
                $(closedTicket(), transitionUsing(this::markAsDuplicate)),
                $(closedTicket(), transitionUsing(this::markAsWontFix))
               );
    }
    
    // --

    private static Object[] $(Object... objects){
        return objects;
    }
    
    private Consumer<Ticket> transitionUsing(Consumer<Ticket> lambda) {
        return lambda;
    }
    
    private void assign(Ticket ticket) {
        ticket.assignTo(new AssigneeID("homer.simpson"));
    }

    private void close(Ticket ticket) {
        ticket.close();
    }

    private void fix(Ticket ticket) {
        ticket.fixedIn(aProductVersion());
    }
    
    private void markAsDuplicate(Ticket ticket) {
        ticket.duplicateOf(new TicketNumber("787"));
    }

    private void markAsWontFix(Ticket ticket) {
        ticket.wontFix("Works as designed!");
    }

    private void cannotReproduce(Ticket ticket) {
        ticket.cannotReproduce();
    }

    private void reopen(Ticket ticket) {
        ticket.reopen(aProductVersion());
    }
    
    private AssigneeID aParticipant() {
        return new AssigneeID("homer.simpson");
    }
    
    private Ticket openTicket() {
        return new Ticket(new TicketNumber("123"));
    }

    private Ticket resolvedTicket() {
        Ticket ticket = openTicket();
        ticket.assignTo(aParticipant());
        ticket.fixedIn(aProductVersion());
        return ticket;
    }

    private Ticket assignedTicket() {
        Ticket ticket = openTicket();
        ticket.assignTo(aParticipant());
        return ticket;
    }

    private Ticket closedTicket() {
        Ticket ticket = resolvedTicket();
        ticket.close();
        return ticket;
    }
    
    private ProductVersion aProductVersion() {
        return aProductVersion("buggy", "3.2.1");
    }

    private ProductVersion aProductVersion(String product, String version) {
        return new ProductVersion(product, version);
    }
    
}