package org.dijure.world.controller;

import org.dijure.world.model.City;
import org.dijure.world.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

@RestController
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
        City denver = CityService.getDenver();
        denver.setName("This is wrong");  // Remove to ensure pactVerify will pass
        return denver;
    }

    @RequestMapping("/build")
    @ResponseBody
    public String getBuildTime() throws URISyntaxException
    {
        return Long.toString(classBuildTime());
    }

    private long classBuildTime() throws URISyntaxException, IllegalStateException, IllegalArgumentException
    {
        URL resource = getClass().getResource(getClass().getSimpleName() + ".class");
        if (resource == null)
        {
            throw new IllegalStateException("Failed to find class file for class: " +
                    getClass().getName());
        }

        switch (resource.getProtocol())
        {
            case "file":
                return new File(resource.toURI()).lastModified();
            case "jar":
                String path = resource.getPath();
                return new File(path.substring(5, path.indexOf("!"))).lastModified();

            default:
                throw new IllegalArgumentException("Unhandled url protocol: " +
                        resource.getProtocol() + " for class: " +
                        getClass().getName() + " resource: " + resource.toString());
        }
    }
}
