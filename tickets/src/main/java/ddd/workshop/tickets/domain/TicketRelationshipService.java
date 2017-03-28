package ddd.workshop.tickets.domain;

public class TicketRelationshipService {

    private TicketRepository tickets;

    public TicketRelationshipService(TicketRepository tickets) {
        this.tickets = tickets;
    }

    public void markAsDuplicate(TicketNumber duplicate, TicketNumber original) {
     
        Ticket source = tickets.load(duplicate);
        Ticket target = tickets.load(original);
        
        source.duplicateOf(original);
        target.markAsBeingDuplicatedBy(duplicate);
    }

    public void markAsBlockedBy(TicketNumber blocked, TicketNumber blocker) {

        Ticket source = tickets.load(blocked);
        Ticket target = tickets.load(blocker);
        
        source.blockedBy(blocker);
        target.markAsBlockerFor(blocked);
        
    }

    public void markAsReletedTo(TicketNumber source, TicketNumber target) {
        
        Ticket from = tickets.load(source);
        Ticket to = tickets.load(target);
        
        from.relatedTo(target);
        to.relatedTo(source);
    }

}
