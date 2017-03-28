package ddd.workshop.tickets.domain;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class TicketFactory {

    private Clock clock;

    public TicketFactory(Clock clock) {
        this.clock = clock;
    }

    public Ticket newTicket() {
        return new Ticket(TicketNumber.next(), clock.time());
    }

    interface Clock {
        
        LocalDateTime time();
        
    }
    
    @Service
    static class SystemClock implements Clock {

        @Override
        public LocalDateTime time() {
            return LocalDateTime.now();
        }
        
    }
    
}
