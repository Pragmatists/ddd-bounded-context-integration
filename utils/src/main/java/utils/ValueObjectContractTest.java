package utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public abstract class ValueObjectContractTest {

    protected abstract Object instance();
    protected abstract Object[] equalInstances();
    protected abstract Object[] nonEqualInstances();

    @Test
    public abstract void shouldHaveDescriptiveTextualRepresentation() throws Exception;

    @Test
    @Parameters(method = "equalInstances")
    public void shouldBeEqualIfHasSameValues(Object equalInstance) throws Exception {

        // given:
        final Object some = instance();

        // when:
        boolean areEqual = some.equals(equalInstance);

        // then
        assertThat(areEqual).isTrue();
    }

    @Test
    @Parameters(method = "nonEqualInstances")
    public void shouldNotBeEqualIfDifferentValues(Object nonEqual) throws Exception {

        // given:
        final Object some = instance();

        // when:
        boolean result = some.equals(nonEqual);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotBeEqualIfDifferentClassPassed() throws Exception {

        // given:
        final Object some = instance();

        // when:
        boolean result = some.equals("somethingDifferent");

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotBeEqualIfNullPassed() throws Exception {

        // given:
        final Object some = instance();

        // when:
        boolean result = some.equals(null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @Parameters(method = "equalInstances")
    public void shouldHaveSameHashCodesIfObjectsEquals(Object equalInstance) throws Exception {

        // given:
        final Object some = instance();

        // when:
        boolean areEqual = some.hashCode() == equalInstance.hashCode();

        // then
        assertThat(areEqual).isTrue();
    }

    @Test
    @Parameters(method = "nonEqualInstances")
    public void shouldHaveDifferentHashCodesIfNonEqualInstances(Object nonEqualInstance) throws Exception {

        // given:
        final Object some = instance();

        // when:
        boolean areEqual = some.hashCode() == nonEqualInstance.hashCode();

        // then
        assertThat(areEqual).isFalse();
    }

}
