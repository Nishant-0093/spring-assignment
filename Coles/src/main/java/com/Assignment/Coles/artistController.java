package com.Assignment.Coles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class artistController {
    @GetMapping("/artist/{id}")
    public JsonNode getListOfArtists(@PathVariable String id) {
        // Api to fetch artist details
        String artistApi =  "https://musicbrainz.org/ws/2/artist/?query=" + id;;

        //creating a restTemplate object
        System.out.println("hello shubham: " + id);
        if(id == null)
        {
            System.out.println("Hello Coles");
        }
        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(artistApi, String.class);

        //used to deserialize JSON string into Java objects.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(result);
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
        }

        if(id != "")
        {
            JsonNode artistsArray = jsonNode.get("artists");
            JsonNode releaseNode = null;
            if (artistsArray.size() == 1) {
                String releaseApi = "https://musicbrainz.org/ws/2/release/?query=" + id;
                String releases = restTemplate.getForObject(releaseApi, String.class);
                try {
                    releaseNode = objectMapper.readTree(releases);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return releaseNode;
            }
            else {
                return artistsArray;
            }
        }
        return jsonNode;
    }
    @GetMapping("/")
    public String Home()
    {
        return "<h1>Welcome to our Homepage</h1>";
    }
    @Controller
    public static class CustomErrorController implements ErrorController
    {
        @RequestMapping("/error")
        @ResponseBody
        String error(HttpServletRequest request) {
            return "<h1>This Application is just built to fetch the artist details by using the correct url path: /artist/{artist_name}</h1>";
        }

    }
}
