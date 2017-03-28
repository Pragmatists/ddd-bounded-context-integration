package ddd.workshop.tickets.infrastructure.persistence;

import org.hibernate.Session;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ddd.workshop.tickets.domain.Ticket;
import ddd.workshop.tickets.domain.TicketRepository;
import ddd.workshop.tickets.domain.TicketRepositoryContractTest;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("persistence-hibernate")
public class HibernateTicketRepositoryTest extends TicketRepositoryContractTest {

    @Autowired
    private Session session;
    
    @Override
    public TicketRepository freshReposiotry() {
        
        deleteTables("T_RELATED_TICKETS", "T_TICKETS");
        
        return new HibernateTicketRepository(session) {
            @Override
            public void store(Ticket ticket) {
                super.store(ticket);
                session.flush();
                session.clear();
            }
        };
    }

    private void deleteTables(String... tables) {
        for (String table: tables) {
            session.createNativeQuery("DELETE FROM " + table + ";").executeUpdate();
        }
    }

}
