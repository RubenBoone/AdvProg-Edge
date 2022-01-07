# AdvProg-Edge

## Korte beschrijving:
Wij hebben voor het thema van **monumenten** en **rondleidingen** gekozen. Hier bovenop hebben we nog een 
extra service toegevoegd, namelijk **tickets**. Een bezoeker kan een ticket aankopen die bij een
rondleiding hoort. Deze rondeleiding behoort tot een monument. 

Een monument kan meerdere
rondleidingen bevatten, deze worden gekoppeld aan elkaar met behulp van een code (MonuCode). Een
ticket wordt op dezelfde manier gekoppeld aan een rondleiding (TourCode).

## Diagram microservices architectuur :
![](images/Summary.jpg)

## SwaggerUI EdgeService:
![Overzicht endpoints](images/SwaggerUI.png)

## Code Coverage:
### EdgeService Tests
![](CoverageEdge.png)

POST,PUT,DELETE niet inbegrepen*
### MonumentService Tests:
![](images/CoverageMonument.png)
### TourService Tests:
![](images/CoverageTour.png)
### TicketService Tests:
![](images/CoverageTicket.png)


### Github Repo's:
- [Edge service](https://github.com/RubenBoone/AdvProg-Edge)
- [Monument microservice](https://github.com/RubenBoone/AdvProg-Monument)
- [Tour microservice](https://github.com/RubenBoone/AdvProg-Tour)
- [Ticket microservice](https://github.com/RubenBoone/AdvProg-Ticket)
- [Angular Frontend](https://github.com/PeetersJ2010/APT-Frontend)

## Postman test voor endpoints
### GET
#### /monuments
![](images/endpoints/Monuments.png)
#### /monuments/{monuCode}
![](images/endpoints/MonumentsMonuCode.png)
#### /tours
![](images/endpoints/Tours.png)
#### /tours/{tourCode}
![](images/endpoints/ToursTourCode.png)
#### /tickets
![](images/endpoints/Tickets.png)
#### /tours/popular
![](images/endpoints/ToursPopular.png)
#### /tours/best
![](images/endpoints/ToursBest.png)
#### /monuments/oldest
![](images/endpoints/MonumentsOldest.png)
#### /monuments/newest
![](images/endpoints/MonumentsNewest.png)
#### /tours/price/{price}
![](images/endpoints/ToursPrice.png)

### POST
#### /monuments
![](images/endpoints/PostMonuments.png)
#### /tours
![](images/endpoints/PostTours.png)
#### /tickets
![](images/endpoints/PostTicket.png)

### PUT
#### /monuments
![](images/endpoints/PutMonuments.png)
#### /tours
![](images/endpoints/PutTours.png)

## DELETE
#### /monuments/{monuCode}
![](images/endpoints/DelMonuments.png)
#### /tours/{tourCode}
![](images/endpoints/DelTours.png)



