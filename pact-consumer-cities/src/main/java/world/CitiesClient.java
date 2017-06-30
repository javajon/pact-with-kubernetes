package world;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Makes calls to REST services and wires responses into the City bean(s).
 */
public class CitiesClient
{
    /** The base REST endpoint. */
    private final String restUrl;

    /** Spring's central class for synchronous client-side HTTP access. */
    private final RestTemplate restTemplate;

    @Autowired
    public CitiesClient(@Value("${producer}") String restUrl)
    {
        this.restUrl = restUrl;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Calls rest service to obtain a list of cities and
     * wires it into the City bean.
     */
    public List<City> getCities()
    {
        ParameterizedTypeReference<List<City>> responseType =
                new ParameterizedTypeReference<List<City>>() {};

        return restTemplate.exchange(restUrl("/city/list"), HttpMethod.GET, null, responseType).getBody();
    }

    public List<City> getTopLargestCities()
    {
        ParameterizedTypeReference<List<City>> responseType =
                new ParameterizedTypeReference<List<City>>() {};

        return restTemplate.exchange(restUrl("/city/largest"), HttpMethod.GET, null, responseType).getBody();
    }

    /**
     * Calls rest service to obtain a city and wires it into the City bean.
     */
    public City getDenver()
    {
        ParameterizedTypeReference<City> responseType =
                new ParameterizedTypeReference<City>() {};

        return restTemplate.exchange(restUrl("/city/denver"), HttpMethod.GET, null, responseType).getBody();
    }

    /**
     * Construct the full rest url.
     *
     * @param context relative context
     * @return full url of endpoint plus context
     */
    private String restUrl(String context)
    {
        return restUrl + context;
    }
}
