package guru.springframework.msscbrewery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.service.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BeerController.class)
public class BeerControllerTest {

    @MockBean
    BeerService beerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    static BeerDto validBeer;

    @BeforeAll
    public static void setup(){
        validBeer= BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("Beer1")
                .beerStyle("PALE_ALE")
                .upc(123456789012L)
                .build();
    }

    @Test
    void handleGet() throws Exception {
        //given
        given(beerService.getBeerById(any(UUID.class))).willReturn(validBeer);

        mockMvc.perform(get("/api/v1/beers/"
                        + validBeer.getId().toString())
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(validBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is("Beer1")));
    }

    @Test
    void handlePost() throws Exception {
        //given
        BeerDto beerDto= validBeer;
        //beerDto.setId(null);
        BeerDto savedDto= BeerDto.builder().id(UUID.randomUUID()).beerName("New beer").build();
        String beerDtoJson= objectMapper.writeValueAsString(beerDto);
        //String beerDtoJson= "";

        given(beerService.saveNewBear(any())).willReturn(savedDto);

        mockMvc.perform(post("/api/v1/beers/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(beerDtoJson))
                .andExpect(status().isCreated());





    }

    @Test
    void handleUpdate() {
    }
}