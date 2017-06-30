package world;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.collections.MapUtils;
import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;

/**
 * A test that will help create a PACT file to test a listing of cities.
 */
public class CitiesPactRuleTest
{
    private static final String EXPECTED_BODY = TestAssist.getBody("cities-body.json");

    @Rule
    public PactProviderRuleMk2 provider = new PactProviderRuleMk2("largestCitiesProvider", "localhost", 8080, this);

    @Pact(
            provider = "largestCitiesProvider",
            consumer = "largestCitiesConsumer"
    )
    public RequestResponsePact createFragment(PactDslWithProvider builder)
    {
        return builder
                .given("Pact for validating cities list")

                .uponReceiving("a request for top 20 largest cities")
                .path("/city/largest")
                .method("GET")

                .willRespondWith()
                .headers(TestAssist.HEADERS)
                .status(200)
                .body(EXPECTED_BODY)

                .toPact();
    }

    @Test
    @PactVerification("largestCitiesProvider")
    public void runTest() throws IOException
    {
        assertEquals(new CitiesClient(provider.getUrl()).getTopLargestCities().size(), 20);
    }
}
