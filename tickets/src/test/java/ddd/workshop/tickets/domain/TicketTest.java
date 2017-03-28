package ddd.workshop.tickets.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ddd.workshop.tickets.domain.Ticket.RelatedTicket;
import ddd.workshop.tickets.domain.Ticket.Resolution;

public class TicketTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldBeInOpenStateJustAfterCreation() throws Exception {

        // given:

        // when:
        Ticket ticket = newTicket();

        // then:
        assertThat(ticket.status()).isEqualTo(Status.OPEN);
    }

    @Test
    public void shouldBeInAssignedStateAfterAssigningParticipant() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.assignTo(aParticipant());

        // then:
        assertThat(ticket.status()).isEqualTo(Status.ASSIGNED);
    }

    @Test
    public void shouldFailMeaningfullyIfAssigningInvalidParticipant() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.assignTo(null);
    }

    @Test
    public void shouldHaveAssigneeAfterAssigningParticipant() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.assignTo(aParticipant("homer.simpson"));

        // then:
        assertThat(ticket.assignee()).isEqualTo(aParticipant("homer.simpson"));
    }

    @Test
    public void shouldBeInResolvedStateAfterFixing() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.fixedIn(aProductVersion());

        // then:
        assertThat(ticket.status()).isEqualTo(Status.RESOLVED);
    }

    @Test
    public void shouldFailMeaningfullyIfAssigningInvalidFixVersion() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.fixedIn(null);
    }

    @Test
    public void shouldHaveResolutionAfterFixing() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.fixedIn(aProductVersion());

        // then:
        assertThat(ticket.resolution()).isEqualTo(Resolution.FIXED);
    }

    @Test
    public void shouldHaveFixVersionAfterFixing() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.fixedIn(aProductVersion("buggy-app", "3.2.1"));

        // then:
        assertThat(ticket.fixVersion()).isEqualTo(aProductVersion("buggy-app", "3.2.1"));
    }

    @Test
    public void shouldBeInResolvedStateAfterMarkingAsDuplicate() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.duplicateOf(new TicketNumber("987"));

        // then:
        assertThat(ticket.status()).isEqualTo(Status.RESOLVED);
    }

    @Test
    public void shouldFailMeaningfullyIfMarkingAsDuplicateOfInvalidTicket() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.duplicateOf(null);
    }

    @Test
    public void shouldHaveResolutionAfterMarkingAsDuplicate() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.duplicateOf(new TicketNumber("987"));

        // then:
        assertThat(ticket.resolution()).isEqualTo(Resolution.DUPLICATE);
    }

    @Test
    public void shouldHaveRelatedTicketsAfterMarkingAsDuplicate() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.duplicateOf(new TicketNumber("987"));

        // then:
        assertThat(ticket.isDuplicateOf(new TicketNumber("987"))).isTrue();
    }

    @Test
    public void shouldBeInResolvedStateAfterMarkingAsCannotReproduce() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.cannotReproduce();

        // then:
        assertThat(ticket.status()).isEqualTo(Status.RESOLVED);
    }

    @Test
    public void shouldHaveResolutionAfterMarkingAsCannotReproduce() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.cannotReproduce();

        // then:
        assertThat(ticket.resolution()).isEqualTo(Resolution.CANNOT_REPRODUCE);
    }

    @Test
    public void shouldBeInResolvedStateAfterMarkingAsWontFix() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.wontFix("Works this way by design.");

        // then:
        assertThat(ticket.status()).isEqualTo(Status.RESOLVED);
    }

    @Test
    public void shouldFailMeaningfullyIfProvidingInvalidExplanation() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.wontFix(null);
    }

    @Test
    public void shouldFailMeaningfullyIfProvidingEmptyExplanation() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.wontFix("");
    }

    @Test
    public void shouldHaveResolutionAfterMarkingAsWontFix() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.wontFix("Works this way by design.");

        // then:
        assertThat(ticket.resolution()).isEqualTo(Resolution.WONT_FIX);
    }

    @Test
    public void shouldBeInClosedStateAfterClosing() throws Exception {

        // given:
        Ticket ticket = resolvedTicket();

        // when:
        ticket.close();

        // then:
        assertThat(ticket.status()).isEqualTo(Status.CLOSED);
    }

    @Test
    public void shouldGoBackToOpenStateAfterReOpening() throws Exception {

        // given:
        Ticket ticket = closedTicket();

        // when:
        ticket.reopen(aProductVersion());

        // then:
        assertThat(ticket.status()).isEqualTo(Status.OPEN);
    }

    @Test
    public void shouldFailMeaningfullyIfAssigningInvalidFixVersionOnReopen() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);

        // given:
        Ticket ticket = closedTicket();

        // when:
        ticket.reopen(null);
    }

    @Test
    public void shouldResetOccuredInAfterReOpening() throws Exception {

        // given:
        Ticket ticket = closedTicket();

        // when:
        ticket.reopen(aProductVersion("buggy-app", "5.5.5"));

        // then:
        assertThat(ticket.occuredIn()).isEqualTo(aProductVersion("buggy-app", "5.5.5"));
    }

    @Test
    public void shouldResetAssigneeAfterReOpening() throws Exception {

        // given:
        Ticket ticket = resolvedTicket();
        ticket.assignTo(aParticipant());

        // when:
        ticket.reopen(aProductVersion("buggy-app", "5.5.5"));

        // then:
        assertThat(ticket.assignee()).isNull();
    }

    @Test
    public void shouldHaveRelatedTicketsAfterMarkingAsBlocker() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.blockedBy(new TicketNumber("987"));

        // then:
        assertThat(ticket.hasRelationshipTo(new TicketNumber("987"), RelatedTicket.RelationshipType.BLOCKED_BY)).isTrue();
    }

    @Test
    public void shouldHaveRelatedTicketsAfterReferringToOtherTicket() throws Exception {

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.relatedTo(new TicketNumber("987"));

        // then:
        assertThat(ticket.hasRelationshipTo(new TicketNumber("987"), RelatedTicket.RelationshipType.RELATED_TO)).isTrue();
    }

    @Test
    public void shouldFailMeaningfullyIfReferingToInvalidTicket() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.relatedTo(null);
    }

    @Test
    public void shouldFailMeaningfullyIfBlocksInvalidTicket() throws Exception {

        // expect:
        thrown.expect(IllegalArgumentException.class);

        // given:
        Ticket ticket = newTicket();

        // when:
        ticket.blockedBy(null);
    }

    // --

    private AssigneeID aParticipant() {
        return new AssigneeID("homer.simpson");
    }

    private AssigneeID aParticipant(String participant) {
        return new AssigneeID(participant);
    }

    private Ticket newTicket() {
        return new Ticket(new TicketNumber("123"));
    }

    private Ticket resolvedTicket() {
        Ticket ticket = newTicket();
        ticket.fixedIn(aProductVersion());
        return ticket;
    }

    private Ticket closedTicket() {
        Ticket ticket = resolvedTicket();
        ticket.close();
        return ticket;
    }

    private ProductVersion aProductVersion() {
        return aProductVersion("buggy", "3.2.1");
    }

    private ProductVersion aProductVersion(String product, String version) {
        return new ProductVersion(product, version);
    }

}
