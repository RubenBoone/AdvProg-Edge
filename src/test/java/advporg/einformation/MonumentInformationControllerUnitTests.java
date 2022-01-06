package advporg.einformation;

import advporg.einformation.model.Monument;
import advporg.einformation.model.Ticket;
import advporg.einformation.model.Tour;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MonumentInformationControllerUnitTests {
    @Value("${monumentservice.baseurl}")
    private String monumentServiceBaseUrl;

    @Value("${tourservice.baseurl}")
    private String tourServiceBaseUrl;

    @Value("${ticketservice.baseurl}")
    private String ticketServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    // Test Data
    private Monument monument1 = new Monument(1, "Atomium", "BE", "BE19580318", "1958");
    private Monument monument2 = new Monument(2, "Eiffel tower", "FR", "FR18890331", "1889");
    private Monument monument3 = new Monument(3, "Statue of Liberty", "US", "US18860901", "1886");
    private List<Monument> allMonuments = Arrays.asList(monument1, monument2, monument3);

    private Tour tour1 = new Tour(1, "BE19580318", "BET001", 2.5, 20, 4.0, "Dinner tour", "Full tour with dinner at the end", 20);
    private Tour tour2 = new Tour(2, "BE19580318", "BET002", 1.5, 15.50, 3.5, "Tour", "Full tour", 30);
    private Tour tour3 = new Tour(3, "FR18890331", "FRT001", 1.5, 25.10, 3.8, "Guided", "Full tour with guide", 40);
    private List<Tour> allTours = Arrays.asList(tour1, tour2, tour3);

    private Ticket ticket1 = new Ticket(1, "BET001", "Ruben", "Boone", "r0785519@student.thomasmore.be", new Date());
    private Ticket ticket2 = new Ticket(2, "FRT001", "Ruben", "Boone", "r0785519@student.thomasmore.be", new Date());
    private Ticket ticket3 = new Ticket(3, "FRT001", "Joppe", "Peeters", "r0802173@student.thomasmore.be", new Date());
    private List<Ticket> allTickets = Arrays.asList(ticket1, ticket2, ticket3);

    private List<Monument> popularMonuments = Arrays.asList(monument2, monument2, monument1);
    private List<Tour> topTours = Arrays.asList(tour1, tour3, tour2);
    private List<Monument> topMonuments = Arrays.asList(monument1, monument1, monument2);
    private List<Monument> oldestMonuments = Arrays.asList(monument3, monument2, monument1);
    private List<Monument> newestMonuments = Arrays.asList(monument1, monument2, monument3);
    // Tours cheaper than €21
    private List<Tour> toursByPrice = Arrays.asList(tour1, tour2);

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void whenGetMonuments_thenReturnMonuments() throws Exception {
        // GET all monuments
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + monumentServiceBaseUrl + "/monuments")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allMonuments)));

        mockMvc.perform(get("/monuments"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Atomium")))
                .andExpect(jsonPath("$[0].location", is("BE")))
                .andExpect(jsonPath("$[0].monuCode", is("BE19580318")))
                .andExpect(jsonPath("$[0].buildYear", is("1958")))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Eiffel tower")))
                .andExpect(jsonPath("$[1].location", is("FR")))
                .andExpect(jsonPath("$[1].monuCode", is("FR18890331")))
                .andExpect(jsonPath("$[1].buildYear", is("1889")))

                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Statue of Liberty")))
                .andExpect(jsonPath("$[2].location", is("US")))
                .andExpect(jsonPath("$[2].monuCode", is("US18860901")))
                .andExpect(jsonPath("$[2].buildYear", is("1886")));
    }

    @Test
    public void whenGetMonumentByMonuCode_thenReturnMonument() throws Exception {
        // GET 1 monument
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + monumentServiceBaseUrl + "/monuments/BE19580318")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(monument1)));

        mockMvc.perform(get("/monuments/{monuCode}", "BE19580318"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Atomium")))
                .andExpect(jsonPath("$.location", is("BE")))
                .andExpect(jsonPath("$.monuCode", is("BE19580318")))
                .andExpect(jsonPath("$.buildYear", is("1958")));
    }

    @Test
    public void whenGetTours_thenReturnTours() throws Exception {
        // GET all tours
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + tourServiceBaseUrl + "/tours")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allTours)));

        mockMvc.perform(get("/tours"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].monuCode", is("BE19580318")))
                .andExpect(jsonPath("$[0].tourCode", is("BET001")))
                .andExpect(jsonPath("$[0].tourTime", is(2.5)))
                .andExpect(jsonPath("$[0].entryFee", is(20.0)))
                .andExpect(jsonPath("$[0].score", is(4.0)))
                .andExpect(jsonPath("$[0].title", is("Dinner tour")))
                .andExpect(jsonPath("$[0].description", is("Full tour with dinner at the end")))
                .andExpect(jsonPath("$[0].avgCustomer", is(20)))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].monuCode", is("BE19580318")))
                .andExpect(jsonPath("$[1].tourCode", is("BET002")))
                .andExpect(jsonPath("$[1].tourTime", is(1.5)))
                .andExpect(jsonPath("$[1].entryFee", is(15.50)))
                .andExpect(jsonPath("$[1].score", is(3.5)))
                .andExpect(jsonPath("$[1].title", is("Tour")))
                .andExpect(jsonPath("$[1].description", is("Full tour")))
                .andExpect(jsonPath("$[1].avgCustomer", is(30)))

                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].monuCode", is("FR18890331")))
                .andExpect(jsonPath("$[2].tourCode", is("FRT001")))
                .andExpect(jsonPath("$[2].tourTime", is(1.5)))
                .andExpect(jsonPath("$[2].entryFee", is(25.10)))
                .andExpect(jsonPath("$[2].score", is(3.8)))
                .andExpect(jsonPath("$[2].title", is("Guided")))
                .andExpect(jsonPath("$[2].description", is("Full tour with guide")))
                .andExpect(jsonPath("$[2].avgCustomer", is(40)));
    }

    @Test
    public void whenGetTourByTourCode_thenReturnTour() throws Exception {
        // GET 1 tour
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + tourServiceBaseUrl + "/tours/BET002")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(tour2)));

        mockMvc.perform(get("/tours/{tourCode}", "BET002"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.monuCode", is("BE19580318")))
                .andExpect(jsonPath("$.tourCode", is("BET002")))
                .andExpect(jsonPath("$.tourTime", is(1.5)))
                .andExpect(jsonPath("$.entryFee", is(15.50)))
                .andExpect(jsonPath("$.score", is(3.5)))
                .andExpect(jsonPath("$.title", is("Tour")))
                .andExpect(jsonPath("$.description", is("Full tour")))
                .andExpect(jsonPath("$.avgCustomer", is(30)));
    }

    @Test
    public void whenGetTickets_thenReturnTickets() throws Exception {
        // GET all tickets
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + ticketServiceBaseUrl + "/tickets")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allTickets)));

        mockMvc.perform(get("/tickets"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].tourCode", is("BET001")))
                .andExpect(jsonPath("$[0].firstName", is("Ruben")))
                .andExpect(jsonPath("$[0].lastName", is("Boone")))
                .andExpect(jsonPath("$[0].email", is("r0785519@student.thomasmore.be")))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].tourCode", is("FRT001")))
                .andExpect(jsonPath("$[1].firstName", is("Ruben")))
                .andExpect(jsonPath("$[1].lastName", is("Boone")))
                .andExpect(jsonPath("$[1].email", is("r0785519@student.thomasmore.be")))

                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].tourCode", is("FRT001")))
                .andExpect(jsonPath("$[2].firstName", is("Joppe")))
                .andExpect(jsonPath("$[2].lastName", is("Peeters")))
                .andExpect(jsonPath("$[2].email", is("r0802173@student.thomasmore.be")));
    }

    @Test
    public void whenGetToursByPopular_thenReturnTourMonuments() throws Exception {
        // GET all tours
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + tourServiceBaseUrl + "/tours")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allTours)));

        // GET all tickets
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + ticketServiceBaseUrl + "/tickets")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allTickets)));

        // GET popular monuments with params
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + monumentServiceBaseUrl + "/monuments?monuCode=FR18890331&monuCode=FR18890331&monuCode=BE19580318")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(popularMonuments)));

        mockMvc.perform(get("/tours/popular"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].tour.id", is(3)))
                .andExpect(jsonPath("$[0].tour.monuCode", is("FR18890331")))
                .andExpect(jsonPath("$[0].tour.tourCode", is("FRT001")))
                .andExpect(jsonPath("$[0].tour.tourTime", is(1.5)))
                .andExpect(jsonPath("$[0].tour.entryFee", is(25.10)))
                .andExpect(jsonPath("$[0].tour.score", is(3.8)))
                .andExpect(jsonPath("$[0].tour.title", is("Guided")))
                .andExpect(jsonPath("$[0].tour.description", is("Full tour with guide")))
                .andExpect(jsonPath("$[0].tour.avgCustomer", is(40)));
    }

    @Test
    public void whenGetToursByTop_thenReturnTourMonuments() throws Exception {
        // GET top tours
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + tourServiceBaseUrl + "/tours/top")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(topTours)));

        // GET top monuments with params
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + monumentServiceBaseUrl + "/monuments?monuCode=BE19580318&monuCode=BE19580318&monuCode=FR18890331")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(topMonuments)));

        mockMvc.perform(get("/tours/best"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].tour.id", is(1)))
                .andExpect(jsonPath("$[0].tour.monuCode", is("BE19580318")))
                .andExpect(jsonPath("$[0].tour.tourCode", is("BET001")))
                .andExpect(jsonPath("$[0].tour.tourTime", is(2.5)))
                .andExpect(jsonPath("$[0].tour.entryFee", is(20.0)))
                .andExpect(jsonPath("$[0].tour.score", is(4.0)))
                .andExpect(jsonPath("$[0].tour.title", is("Dinner tour")))
                .andExpect(jsonPath("$[0].tour.description", is("Full tour with dinner at the end")))
                .andExpect(jsonPath("$[0].tour.avgCustomer", is(20)));
    }

    @Test
    public void whenGetMonumentsByOld_thenReturnMonuments() throws Exception {
        // GET old monuments
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + monumentServiceBaseUrl + "/monuments/old")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(oldestMonuments)));

        mockMvc.perform(get("/monuments/oldest"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(3)))
                .andExpect(jsonPath("$[0].name", is("Statue of Liberty")))
                .andExpect(jsonPath("$[0].location", is("US")))
                .andExpect(jsonPath("$[0].monuCode", is("US18860901")))
                .andExpect(jsonPath("$[0].buildYear", is("1886")));
    }

    @Test
    public void whenGetMonumentsByNew_thenReturnMonuments() throws Exception {
        // GET new monuments
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + monumentServiceBaseUrl + "/monuments/new")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newestMonuments)));

        mockMvc.perform(get("/monuments/newest"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Atomium")))
                .andExpect(jsonPath("$[0].location", is("BE")))
                .andExpect(jsonPath("$[0].monuCode", is("BE19580318")))
                .andExpect(jsonPath("$[0].buildYear", is("1958")));
    }

    @Test
    public void whenGetToursByPrice_thenReturnTourMonuments() throws Exception {
        // GET tours by price
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + tourServiceBaseUrl + "/tours/price/21.0")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(toursByPrice)));

        // GET monuments with params
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://" + monumentServiceBaseUrl + "/monuments?monuCode=BE19580318&monuCode=BE19580318")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(topMonuments)));

        mockMvc.perform(get("/tours/price/{price}", 21))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].tour.id", is(1)))
                .andExpect(jsonPath("$[0].tour.monuCode", is("BE19580318")))
                .andExpect(jsonPath("$[0].tour.tourCode", is("BET001")))
                .andExpect(jsonPath("$[0].tour.tourTime", is(2.5)))
                .andExpect(jsonPath("$[0].tour.entryFee", is(20.0)))
                .andExpect(jsonPath("$[0].tour.score", is(4.0)))
                .andExpect(jsonPath("$[0].tour.title", is("Dinner tour")))
                .andExpect(jsonPath("$[0].tour.description", is("Full tour with dinner at the end")))
                .andExpect(jsonPath("$[0].tour.avgCustomer", is(20)))

                .andExpect(jsonPath("$[1].tour.id", is(2)))
                .andExpect(jsonPath("$[1].tour.monuCode", is("BE19580318")))
                .andExpect(jsonPath("$[1].tour.tourCode", is("BET002")))
                .andExpect(jsonPath("$[1].tour.tourTime", is(1.5)))
                .andExpect(jsonPath("$[1].tour.entryFee", is(15.50)))
                .andExpect(jsonPath("$[1].tour.score", is(3.5)))
                .andExpect(jsonPath("$[1].tour.title", is("Tour")))
                .andExpect(jsonPath("$[1].tour.description", is("Full tour")))
                .andExpect(jsonPath("$[1].tour.avgCustomer", is(30)));
    }

//    @Test
//    public void whenAddMonument_thenReturnMonument() throws Exception {
//
//        Monument newMonument = new Monument(4, "NewMonument", "NewMonument", "NewMonument", "NewMonument");
//
//        // POST review for Book 1 from User 3
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://" + monumentServiceBaseUrl + "/monuments")))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(newMonument))
//                );
//
//        mockMvc.perform(post("/monuments")
//                        .param("id", "4")
//                        .param("name", newMonument.getName())
//                        .param("location", "NewMonument")
//                        .param("monuCode", "NewMonument")
//                        .param("buildYear", "NewMonument")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is("4")))
//                .andExpect(jsonPath("$.name", is("NewMonument")))
//                .andExpect(jsonPath("$.location", is("NewMonument")))
//                .andExpect(jsonPath("$.monuCode", is("NewMonument")))
//                .andExpect(jsonPath("$.buildYear", is("NewMonument")));
//    }

//    @Test
//    public void whenAddTour_thenReturnTour() throws Exception {
//
//        Tour newTour = new Tour(1, "BE19580318", "newTour", 1, 1, 1, "newTour", "newTour", 1);
//
//
//        // POST review for Book 1 from User 3
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://" + tourServiceBaseUrl + "/tours")))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(newTour))
//                );
//
//        mockMvc.perform(post("/tours")
//                        .param("id", Integer.toString(newTour.getId()))
//                        .param("monuCode", newTour.getMonuCode())
//                        .param("tourCode", newTour.getTourCode())
//                        .param("tourTime", Double.toString(newTour.getTourTime()))
//                        .param("entryFee", Double.toString(newTour.getEntryFee()))
//                        .param("score", Double.toString(newTour.getScore()))
//                        .param("title", newTour.getTitle())
//                        .param("description", newTour.getDescription())
//                        .param("avgCustomer", Double.toString(newTour.getAvgCustomer()))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is("4")));
//    }
}
