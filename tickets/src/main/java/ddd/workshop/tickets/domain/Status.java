package ddd.workshop.tickets.domain;

import static java.util.Arrays.asList;

public class Status {

    public static final Status OPEN = new Status("OPEN"){
        public Status assign() { return Status.ASSIGNED; }
        public Status resolve() { return Status.RESOLVED; }
    };
    
    public static final Status ASSIGNED = new Status("ASSIGNED"){
        public Status assign() { return Status.ASSIGNED; }
        public Status resolve() { return Status.RESOLVED; }
    };
    
    public static final Status RESOLVED = new Status("RESOLVED"){
        public Status assign() { return Status.RESOLVED; }
        public Status resolve() { return Status.RESOLVED; }
        public Status close() { return Status.CLOSED; }
        public Status reopen() { return Status.OPEN; }
    };
    
    public static final Status CLOSED = new Status("CLOSED"){
        public Status reopen() { return Status.OPEN; }
    };
    
    public static final Status CANCELLED = new Status("CACNELLED");

    private String value;

    private Status(String value) {
        this.value = value;
    }
    
    static Status of(String value){
        return asList(OPEN, ASSIGNED, RESOLVED, CLOSED)
                .stream()
                .filter(s -> s.value.equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + value));
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    public Status reopen(){
        throw cannotTransitTo(OPEN);
    }

    public Status assign(){
        throw cannotTransitTo(ASSIGNED);
    }

    public Status resolve(){
        throw cannotTransitTo(RESOLVED);
    }

    public Status close(){
        throw cannotTransitTo(CLOSED);
    }

    private IllegalStateException cannotTransitTo(Status target) {
        return new IllegalStateException(String.format("Cannot transit from %s to %s", this, target));
    }
    
}
