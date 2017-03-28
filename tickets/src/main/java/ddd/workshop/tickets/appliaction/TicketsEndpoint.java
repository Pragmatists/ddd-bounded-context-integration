package ddd.workshop.tickets.appliaction;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import ddd.workshop.tickets.appliaction.TicketsEndpoint.NewTicketJson;
import ddd.workshop.tickets.domain.Ticket;
import ddd.workshop.tickets.domain.TicketFactory;
import ddd.workshop.tickets.domain.TicketNumber;
import ddd.workshop.tickets.domain.TicketRepository;

@RestController
@RequestMapping(path="/api/tickets")
public class TicketsEndpoint {

    @Autowired
    private TicketRepository tickets;

    @Autowired
    private TicketFactory factory;
    
    @RequestMapping(method=POST)
    @Transactional
    public ResponseEntity<TicketReferenceJson> create(@RequestBody NewTicketJson json) {
        
        Ticket ticket = factory.newTicket();
        ticket.renameTo(json.title);
        tickets.store(ticket);
        
        return new ResponseEntity<>(new TicketReferenceJson(ticket), CREATED);
    }


    @RequestMapping(path="/{number}", method=GET, produces=APPLICATION_JSON_VALUE)
    @Transactional(readOnly=true)
    public TicketJson load(@PathVariable("number") String number) {
        
        Ticket ticket = tickets.load(new TicketNumber(number));
        return new TicketJson(ticket);
    }
    
    
    class TicketReferenceJson {
    
        @JsonProperty
        String ticketNumber;
        
        TicketReferenceJson(Ticket ticket) {
            ticketNumber = ticket.number().toString();
        }
        
    }
    
    static class NewTicketJson {

        @JsonProperty
        String title;
    }

    class TicketJson extends TicketReferenceJson {
    
        @JsonProperty
        String title;
        
        TicketJson(Ticket ticket) {
            super(ticket);
            title = ticket.title();
        }
    }
}
