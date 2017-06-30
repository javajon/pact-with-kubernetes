package world;

/**
 * A simple bean to hold the City data here on the Client / Consumer side.
 * This bean matches the City in the Microservice / Producer side.
 */
public class City
{
    private Integer id;
    private String name;
    private String countryCode;
    private Integer population;
    private String district;

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getPopulation()
    {
        return population;
    }

    public void setPopulation(Integer population)
    {
        this.population = population;
    }
}
