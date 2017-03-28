package ddd.workshop.tickets.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.common.base.Preconditions;

import ddd.workshop.tickets.domain.Ticket.RelatedTicket.RelationshipType;

public class Ticket {

    public enum Resolution {
        FIXED, DUPLICATE, CANNOT_REPRODUCE, WONT_FIX
    }

    private TicketNumber number;
    private String title;
    private String description;
    private Status status = Status.OPEN;
    private AssigneeID assignee;
    public Set<RelatedTicket> related = new HashSet<>();
    private Resolution resolution;
    
    private ProductVersion fixedIn;
    private ProductVersion occuredIn;
    
    private LocalDateTime createdAt;
    
    protected Ticket() {}

    public Ticket(TicketNumber number) {
        this();
        this.number = number;
    }
    
    public Ticket(TicketNumber number, LocalDateTime createdAt) {
        this(number);
        this.createdAt = createdAt;
    }

    public void renameTo(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void assignTo(AssigneeID assignee) {
        Preconditions.checkArgument(assignee != null);
        this.status = status.assign();
        this.assignee = assignee;
    }
    
    public void close(){
        this.status = status.close();
    }
    
    public void blockedBy(TicketNumber blocker) {
        Preconditions.checkArgument(blocker != null);
        related.add(RelatedTicket.blockedBy(this, blocker));
    }

    public void relatedTo(TicketNumber other) {
        Preconditions.checkArgument(other != null);
        related.add(RelatedTicket.relatedTo(this, other));
    }

    public void fixedIn(ProductVersion fixVersion) {
        Preconditions.checkArgument(fixVersion != null);
        status = status.resolve();
        resolution = Resolution.FIXED;
        fixedIn = fixVersion;
    }    
    
    public void markAsBeingDuplicatedBy(TicketNumber number) {
        related.add(RelatedTicket.duplicatedBy(this, number));
    }

    public void markAsBeingReferedFrom(TicketNumber number) {
        throw new IllegalArgumentException("Not implemented yet!");
    }

    public void markAsBlockerFor(TicketNumber number) {
        related.add(RelatedTicket.blocks(this, number));
    }

    public void duplicateOf(TicketNumber original) {
        Preconditions.checkArgument(original != null);
        status = status.resolve();
        resolution = Resolution.DUPLICATE;
        related.add(RelatedTicket.duplicateOf(this, original));
    }

    public void cannotReproduce() {
        status = status.resolve();
        resolution = Resolution.CANNOT_REPRODUCE;
    }

    public void wontFix(String reason) {
        Preconditions.checkArgument(reason != null);
        Preconditions.checkArgument(!reason.isEmpty());
        
        status = status.resolve();
        resolution = Resolution.WONT_FIX;
    }

    public void reopen(ProductVersion reopenVersion) {
        Preconditions.checkArgument(reopenVersion != null);
        status = status.reopen();
        assignee = null;
        occuredIn = reopenVersion;
    }

    protected static class RelatedTicket implements Serializable {

        public enum RelationshipType {
            DUPLICATED_BY, DUPLICATES,
            BLOCKED_BY, BLOCKS, 
            RELATED_TO
        };
        
        private Ticket source;
        private TicketNumber target;
        private RelationshipType type;

        private RelatedTicket() {}
        
        private RelatedTicket(Ticket source, TicketNumber target, RelationshipType type) {
            this();
            this.source = source;
            this.target = target;
            this.type = type;
        }

        public static RelatedTicket duplicatedBy(Ticket ticket, TicketNumber number) {
            return new RelatedTicket(ticket, number, RelationshipType.DUPLICATED_BY);
        }

        public static RelatedTicket duplicateOf(Ticket ticket, TicketNumber number) {
            return new RelatedTicket(ticket, number, RelationshipType.DUPLICATES);
        }

        @Override
        public String toString() {
            return String.format("%s -(%s)-> %s", source.number, type, target);
        }

        public static RelatedTicket relatedTo(Ticket ticket, TicketNumber other) {
            return new RelatedTicket(ticket, other, RelationshipType.RELATED_TO);
        }

        public static RelatedTicket blocks(Ticket ticket, TicketNumber other) {
            return new RelatedTicket(ticket, other, RelationshipType.BLOCKS);
        }
        public static RelatedTicket blockedBy(Ticket ticket, TicketNumber other) {
            return new RelatedTicket(ticket, other, RelationshipType.BLOCKED_BY);
        }

        @Override
        public boolean equals(Object obj) {

            if (!(obj instanceof Ticket.RelatedTicket)) {
                return false;
            }

            Ticket.RelatedTicket other = (Ticket.RelatedTicket) obj;

            return Objects.equals(
                    new Object[]{this.source.number, this.target, this.type}, 
                    new Object[]{other.source.number, other.target, other.type}
                    );
        }
        
        @Override
        public int hashCode(){
            return Objects.hash("Ticket.RelatedTicket", this.source.number, this.target, this.type);
        }

    }

    public TicketNumber number() {
        return number;
    }
    
    public Status status() {
        return status;
    }
    
    public String title() {
        return title;
    }
    
    public String description() {
        return description;
    }

    public AssigneeID assignee() {
        return assignee;
    }
    
    public Resolution resolution() {
        return resolution;
    }

    public ProductVersion fixVersion() {
        return fixedIn;
    }

    public ProductVersion occuredIn() {
        return occuredIn;
    }

    public boolean isDuplicateOf(TicketNumber ticketNumber) {
        return hasRelationshipTo(ticketNumber, RelationshipType.DUPLICATES);
    }

    public boolean hasRelationshipTo(TicketNumber ticketNumber, RelationshipType relationshipType) {
        return related.stream().anyMatch(t -> {
          return t.source == this && t.target.equals(ticketNumber) && t.type == relationshipType;   
        });
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }
}
