package advporg.einformation.controller;

import advporg.einformation.model.Information;
import advporg.einformation.model.Monument;
import advporg.einformation.model.MonumentInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
public class MonumentInformationController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${monumentservice.baseurl}")
    private String monumentServiceBaseUrl;

    @Value("${informationservice.baseurl}")
    private String informationServiceBaseUrl;


    // Get top 3 monuments
    @GetMapping("/monuments/top")
    public List<MonumentInformation> getTopMonuments() {
        Information[] informations = restTemplate.getForObject("http://" + informationServiceBaseUrl + "/info/top", Information[].class);

        // Check for 3 information objects
        //if (informations.length == 3){

        // Get monuCodes
        List<String> monuCodes = this.getMonuCodes(informations);

        // Set url params
        String params = "?monuCode=" + monuCodes.get(0) + "&monuCode=" + monuCodes.get(1) + "&monuCode=" + monuCodes.get(2);
        // Get monuments with params
        Monument[] monuments = restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments" + params, Monument[].class);

        //if (monuments.length == 3){
        // Make 3 new MonumentInformation objects to return
        List<MonumentInformation> monumentInformation = this.combineMonumentInformation(monuments, informations);

        return monumentInformation;
        //}
        //}
    }

    // Get oldest monuments
    @GetMapping("/monuments/oldest")
    public List<MonumentInformation> getOldestMonuments() {
        Information[] informations = restTemplate.getForObject("http://" + informationServiceBaseUrl + "/info/old", Information[].class);

        // Get monuCodes
        List<String> monuCodes = this.getMonuCodes(informations);

        // Get monuments
        Monument[] monuments = restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments", Monument[].class);

        // Make new MonumentInformation objects to return
        List<MonumentInformation> monumentInformation = this.combineMonumentInformation(monuments, informations);

        return monumentInformation;
    }

    // Get newest monuments
    @GetMapping("/monuments/newest")
    public List<MonumentInformation> getNewestMonuments() {
        Information[] informations = restTemplate.getForObject("http://" + informationServiceBaseUrl + "/info/new", Information[].class);

        // Get monuCodes
        List<String> monuCodes = this.getMonuCodes(informations);

        // Get monuments
        Monument[] monuments = restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments", Monument[].class);

        // Make new MonumentInformation objects to return
        List<MonumentInformation> monumentInformation = this.combineMonumentInformation(monuments, informations);

        return monumentInformation;
    }

    // Get monuments by price
    @GetMapping("/monuments/{price}")
    public List<MonumentInformation> getMonumentsByPrice(@PathVariable Double price) {
        Information[] informations = restTemplate.getForObject("http://" + informationServiceBaseUrl + "/info/price/{price}", Information[].class, price);

        // Get monuCodes
        List<String> monuCodes = this.getMonuCodes(informations);

        // Get monuments
        Monument[] monuments = restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments", Monument[].class);

        // Make new MonumentInformation objects to return
        List<MonumentInformation> monumentInformation = this.combineMonumentInformation(monuments, informations);

        return monumentInformation;
    }

    private List<String> getMonuCodes(Information[] informations) {
        List<String> monuCodes = new ArrayList<String>();
        for (Information info : informations) {
            monuCodes.add(info.getMonuCode());
        }
        return monuCodes;
    }

    private List<MonumentInformation> combineMonumentInformation(Monument[] monuments, Information[] informations) {
        ArrayList<MonumentInformation> mi = new ArrayList<MonumentInformation>();

        for (Information information : informations) {
            for (Monument monument :
                    monuments) {
                if (Objects.equals(information.getMonuCode(), monument.getMonuCode())) {
                    mi.add(new MonumentInformation(monument, information));
                }
            }
        }

        return mi;
    }

    // Create new monument with information
    @PostMapping("/monuments")
    public MonumentInformation addMonument(@RequestBody MonumentInformation monumentInformation) {
        Information information = restTemplate.postForObject("http://" + informationServiceBaseUrl + "/info", monumentInformation.getInformation(), Information.class);
        Monument monument = restTemplate.postForObject("http://" + monumentServiceBaseUrl + "/monuments", monumentInformation.getMonument(), Monument.class);

        return new MonumentInformation(monument, information);
    }

    // Edit monument with information
    @PutMapping("/monuments")
    public MonumentInformation editMonument(@RequestBody Integer monumentId, @RequestBody Integer informationId, @RequestBody MonumentInformation monumentInformation) {

        // Get information
        Information information = restTemplate.getForObject("http://" + informationServiceBaseUrl + "/info/{informationId}", Information.class, informationId);
        // Put information
        ResponseEntity<Information> responseEntityInformation = restTemplate.exchange(
                "http://" + informationServiceBaseUrl + "/info",
                HttpMethod.PUT, new HttpEntity<>(information), Information.class);
        // get response from PUT
        Information newInformation = responseEntityInformation.getBody();


        // Get monument
        Monument monument = restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments/{monumentId}", Monument.class, monumentId);
        // Put monument
        ResponseEntity<Monument> responseEntityMonument = restTemplate.exchange(
                "http://" + monumentServiceBaseUrl + "/monument",
                HttpMethod.PUT, new HttpEntity<>(monument), Monument.class);
        // get response from PUT
        Monument newMonument = responseEntityMonument.getBody();

        return new MonumentInformation(newMonument, newInformation);
    }

    // Delete monument with information
    @DeleteMapping("/monuments/{monuCode}")
    public ResponseEntity deleteMonument(@PathVariable String monuCode){
        restTemplate.delete("http://" + monumentServiceBaseUrl + "/monuments/" + monuCode);
        restTemplate.delete("http://" + informationServiceBaseUrl + "/info/" + monuCode);

        return ResponseEntity.ok().build();
    }
}
