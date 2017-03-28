package ddd.workshop.tickets.domain;

import java.util.Objects;

public class AssigneeID {

    private String id;

    public AssigneeID(String id) {
        this.id = id;
    }

    @Override
    public int hashCode(){
        return Objects.hash("AssigneeID", id);
    }
    
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof AssigneeID)) {
            return false;
        }

        AssigneeID other = (AssigneeID) obj;

        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public String toString() {
        return id;
    }
    
}
