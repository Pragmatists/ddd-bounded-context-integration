package ddd.workshop.tickets.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class TicketNumber implements Serializable {

    private String value;

    public TicketNumber(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof TicketNumber)) {
            return false;
        }

        TicketNumber other = (TicketNumber) obj;

        return Objects.equals(this.value, other.value);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash("TicketNumber", this.value);
    }

    public static TicketNumber next() {
        return new TicketNumber(UUID.randomUUID().toString());
    }
}
