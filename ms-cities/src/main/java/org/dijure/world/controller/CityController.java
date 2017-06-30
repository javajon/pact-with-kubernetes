package org.dijure.world.controller;

import org.dijure.world.model.City;
import org.dijure.world.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CityController
{
    @Autowired
    private CityService CityService;

    @RequestMapping("/city/random")
    @ResponseBody
    public City getRandomCity()
    {
        return CityService.getRandomCity();
    }

    @RequestMapping("/city/list")
    @ResponseBody
    public City[] getCities()
    {
        return CityService.getCities();
    }

    @RequestMapping("/city/largest")
    @ResponseBody
    public City[] getTopLargestCities()
    {
        return CityService.getTopLargestCities();
    }

    @RequestMapping("/city/denver")
    @ResponseBody
    public City getDenver()
    {
        return CityService.getDenver();
    }
}
