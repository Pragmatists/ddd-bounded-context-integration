package ddd.workshop.tickets.domain;

public interface TicketRepository {

    
    public void store(Ticket ticket);
    public Ticket load(TicketNumber number);

    public class TicketDoesNotExistException extends IllegalArgumentException {
        
        public TicketDoesNotExistException(TicketNumber number) {
            super(String.format("Ticket %s does not exist!", number));
        }
    }

    public class TicketAlreadyExistsException extends IllegalArgumentException {
        
        public TicketAlreadyExistsException(TicketNumber number) {
            super(String.format("Ticket %s already exist!", number));
        }
    }

}
