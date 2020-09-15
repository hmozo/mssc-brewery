package guru.springframework.msscbrewery.controller;

import guru.springframework.msscbrewery.service.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeer(@PathVariable("beerId") UUID beerId){
        return new ResponseEntity<>(beerService.getBeerById(beerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewBeer(@RequestBody BeerDto beerDto){

        BeerDto newBeerDto= beerService.saveNewBear(beerDto);

        HttpHeaders headers= new HttpHeaders();
        // TODO add host:port to URI
        headers.add("Location", "/api/v1/beers" + beerDto.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDto beerDto){
        beerService.updateBeer(beerId, beerDto);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    @DeleteMapping("/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable("beerId") UUID beerId) {
    	beerService.deleteBeerById(beerId);
    	
    	
    	
    }
}
