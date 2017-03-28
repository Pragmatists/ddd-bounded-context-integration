package specs;

import org.chiknrice.test.SpringifiedConcordionRunner;
import org.concordion.api.ConcordionResources;
import org.concordion.api.FullOGNL;
import org.concordion.api.extension.Extension;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ddd.workshop.TicketsApplication;
import pl.pragmatists.concordion.rest.RestExtension;

@RunWith(SpringifiedConcordionRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { TicketsApplication.class })
@FullOGNL
@ActiveProfiles({ "persistence-hibernate" })
@ConcordionResources(value="/tickets.css")
public abstract class RestFixture {

    @LocalServerPort
    protected int port;

    @Extension
    public RestExtension rest = new RestExtension().includeBootstrap().enableCodeMirror();

    @Before
    public void detectPort() {
        rest.port(port);
    }

}
