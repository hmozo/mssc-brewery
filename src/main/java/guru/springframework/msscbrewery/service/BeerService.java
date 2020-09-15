package guru.springframework.msscbrewery.service;

import guru.springframework.msscbrewery.web.model.BeerDto;

import java.util.UUID;

public interface BeerService {
    BeerDto getBeerById(UUID beerId);

    BeerDto saveNewBear(BeerDto beerDto);

    void updateBeer(UUID beerId, BeerDto beerDto);

	void deleteBeerById(UUID beerId);
}
