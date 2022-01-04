package advporg.einformation.controller;

import advporg.einformation.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class MonumentInformationController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${monumentservice.baseurl}")
    private String monumentServiceBaseUrl;

    @Value("${tourservice.baseurl}")
    private String tourServiceBaseUrl;

    @Value("${ticketservice.baseurl}")
    private String ticketServiceBaseUrl;


    // Get top 3 most popular tours (amount of tickets sold)
    @GetMapping("/tours/popular")
    public List<TourMonument> getPopularTours() {
        Tour[] tourList = {new Tour(), new Tour(), new Tour()};
        int first = 0, second = 0, third = 0;

        // Get all tours
        Tour[] tours = restTemplate.getForObject("http://" + tourServiceBaseUrl + "/tours", Tour[].class);
        // Get all tickets
        Ticket[] tickets = restTemplate.getForObject("http://" + ticketServiceBaseUrl + "/tickets", Ticket[].class);

        for (Tour tour : tours) {
            // count tickets
            int ticketAmount = 0;
            for (Ticket ticket : tickets) {
                if (Objects.equals(tour.getTourCode(), ticket.getTourCode())) {
                    ticketAmount++;
                }
            }
            // check if tour has tickets
            if (ticketAmount > first) {
                third = second;
                second = first;
                first = ticketAmount;
                tourList[2] = tourList[1];
                tourList[1] = tourList[0];
                tourList[0] = tour;
            } else if (ticketAmount > second) {
                third = second;
                second = ticketAmount;
                tourList[2] = tourList[1];
                tourList[1] = tour;
            } else if (ticketAmount > third) {
                third = ticketAmount;
                tourList[2] = tour;
            } else {
                continue;
            }
        }

        // Get monuCodes
        List<String> monuCodes = this.getMonuCodes(tourList);
        // Set url params
        String params = "?monuCode=" + monuCodes.get(0) + "&monuCode=" + monuCodes.get(1) + "&monuCode=" + monuCodes.get(2);
        // Get monuments with params
        Monument[] monuments = restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments" + params, Monument[].class);
        // Combine tours and monuments
        return this.combineTourMonument(tourList, monuments);
    }

    // Get top 3 best tours (tour rating)
    @GetMapping("/tours/best")
    public List<TourMonument> getBestTours() {
        // Get best tours
        Tour[] tours = restTemplate.getForObject("http://" + tourServiceBaseUrl + "/tours/top", Tour[].class);
        // Get monuCodes
        List<String> monuCodes = this.getMonuCodes(tours);
        // Set url params
        String params = "?monuCode=" + monuCodes.get(0) + "&monuCode=" + monuCodes.get(1) + "&monuCode=" + monuCodes.get(2);
        // Get monuments with params
        Monument[] monuments = restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments" + params, Monument[].class);
        // Combine tours and monuments
        return this.combineTourMonument(tours, monuments);
    }

    // Get oldest monuments
    @GetMapping("/monuments/oldest")
    public Monument[] getOldestMonuments() {
        // Get oldest monuments
        return restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments/old", Monument[].class);
    }

    // Get newest monuments
    @GetMapping("/monuments/newest")
    public Monument[] getNewestMonuments() {
        // Get newest monuments
        return restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments/new", Monument[].class);
    }

    // Get tours by price
    @GetMapping("/tours/{price}")
    public List<TourMonument> getToursByPrice(@PathVariable Double price) {
        // Get tours by price
        Tour[] tours = restTemplate.getForObject("http://" + tourServiceBaseUrl + "/tours/price/{price}", Tour[].class, price);
        // Get monuCodes
        List<String> monuCodes = this.getMonuCodes(tours);
        // Set url params
        String params = "?monuCode=" + monuCodes.get(0) + "&monuCode=" + monuCodes.get(1);
        // Get monuments with params
        Monument[] monuments = restTemplate.getForObject("http://" + monumentServiceBaseUrl + "/monuments" + params, Monument[].class);
        return this.combineTourMonument(tours, monuments);
    }

    private List<String> getMonuCodes(Tour[] tours) {
        List<String> monuCodes = new ArrayList<String>();
        for (Tour tour : tours) {
            monuCodes.add(tour.getMonuCode());
        }
        return monuCodes;
    }

    private List<TourMonument> combineTourMonument(Tour[] tours, Monument[] monuments) {
        ArrayList<TourMonument> tm = new ArrayList<TourMonument>();

        for (Tour tour : tours) {
            for (Monument monument : monuments) {
                if (Objects.equals(tour.getMonuCode(), monument.getMonuCode())) {
                    tm.add(new TourMonument(tour, monument));
                }
            }
        }

        return tm;
    }

    // Create new monument
    @PostMapping("/monuments")
    public Monument addMonument(@RequestBody Monument monument) {
        return restTemplate.postForObject("http://" + monumentServiceBaseUrl + "/monuments", monument, Monument.class);
    }

    // Create new tour
    @PostMapping("/tours")
    public Tour addTour(@RequestBody Tour tour) {
        return restTemplate.postForObject("http://" + tourServiceBaseUrl + "/tours", tour, Tour.class);
    }

    // Create new ticket
    @PostMapping("/tickets")
    public Ticket addTicket(@RequestBody Ticket ticket) {
        return restTemplate.postForObject("http://" + ticketServiceBaseUrl + "/tickets", ticket, Ticket.class);
    }

    // Edit monument
    @PutMapping("/monuments")
    public Monument editMonument(@RequestBody Monument monument) {
        // PUT
        ResponseEntity<Monument> responseEntityInformation = restTemplate.exchange(
                "http://" + monumentServiceBaseUrl + "/monuments",
                HttpMethod.PUT, new HttpEntity<>(monument), Monument.class);
        // return response from PUT
        return responseEntityInformation.getBody();
    }

    // Edit tour
    @PutMapping("/tours")
    public Tour editTour(@RequestBody Tour tour) {
        // PUT
        ResponseEntity<Tour> responseEntityInformation = restTemplate.exchange(
                "http://" + tourServiceBaseUrl + "/tours",
                HttpMethod.PUT, new HttpEntity<>(tour), Tour.class);
        // return response from PUT
        return responseEntityInformation.getBody();
    }

    // Edit ticket
    @PutMapping("/tickets")
    public Ticket editTicket(@RequestBody Ticket ticket) {
        // PUT
        ResponseEntity<Ticket> responseEntityInformation = restTemplate.exchange(
                "http://" + ticketServiceBaseUrl + "/tickets",
                HttpMethod.PUT, new HttpEntity<>(ticket), Ticket.class);
        // return response from PUT
        return responseEntityInformation.getBody();
    }

    // Delete monument
    @DeleteMapping("/monuments/{monuCode}")
    public ResponseEntity deleteMonument(@PathVariable String monuCode) {
        // Set url params
        String params = "?monuCode=" + monuCode;
        // Get tours with params
        Tour[] tours = restTemplate.getForObject("http://" + tourServiceBaseUrl + "/tours" + params, Tour[].class);

        // Delete all tickets from each tour
        for (Tour tour : tours) {
            restTemplate.delete("http://" + ticketServiceBaseUrl + "/tickets/" + tour.getTourCode());
        }
        restTemplate.delete("http://" + tourServiceBaseUrl + "/tours/" + monuCode);
        restTemplate.delete("http://" + monumentServiceBaseUrl + "/monuments/" + monuCode);
        return ResponseEntity.ok().build();
    }

    // Delete tour
    @DeleteMapping("/tours/{tourCode}")
    public ResponseEntity deleteTour(@PathVariable String tourCode) {
        restTemplate.delete("http://" + ticketServiceBaseUrl + "/tickets/" + tourCode);
        restTemplate.delete("http://" + tourServiceBaseUrl + "/tours/" + tourCode);
        return ResponseEntity.ok().build();
    }
}
