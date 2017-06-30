package world;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * A test that will help create a PACT file to test the
 * query of a specific city.
 */
public class DenverPactRuleTest
{
    private static final String EXPECTED_BODY = TestAssist.getBody("denver.json");

    @Rule
    public PactProviderRuleMk2 provider = new PactProviderRuleMk2("denverProvider", "localhost", 8080, this);

    @Pact(
            provider = "denverProvider",
            consumer = "denverConsumer"
    )
    public RequestResponsePact createFragment(PactDslWithProvider builder)
    {
        return builder
                .given("Pact for validating the request for Denver info")

                .uponReceiving("a request for Denver information")
                .path("/city/denver")
                .method("GET")

                .willRespondWith()
                .headers(TestAssist.HEADERS)
                .status(200)
                .body(EXPECTED_BODY)

                .toPact();
    }

    @Test
    @PactVerification("denverProvider")
    public void runTest() throws IOException
    {
        String cityName = new CitiesClient(provider.getUrl()).getDenver().getName();
        assertEquals(cityName, "Denver");
    }
}
