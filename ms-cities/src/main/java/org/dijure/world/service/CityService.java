package org.dijure.world.service;

import org.dijure.world.mapper.CityMapper;
import org.dijure.world.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Random;


@Service
public class CityService
{
    @Autowired
    private CityMapper cityMapper;

    public City[] getCities()
    {
        return cityMapper.findCityInfo();
    }

    public City getRandomCity()
    {
        City[] cities = getCities();
        return cities[new Random().nextInt(cities.length)];
    }

    public City getDenver()
    {
        // SQL search would be better
        for (City city: getCities())
        {
            if (city.getName().equals("Denver"))
            {
                return city;
            }
        }

        City city = new City();
        city.setName("Not found");

        return city;
    }

    public City[] getTopLargestCities()
    {
        City[] cities = getCities();
        return Arrays.copyOfRange(cities, 0, 20);
    }
}
