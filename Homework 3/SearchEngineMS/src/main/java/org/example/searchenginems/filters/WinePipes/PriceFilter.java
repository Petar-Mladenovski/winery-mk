package org.example.searchenginems.filters.WinePipes;

import org.example.searchenginems.model.Wine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceFilter implements Filter<String> {

    @Override
    public List<Wine> execute(String price, List<Wine> wines) {
        if(price != null) {
            Integer priceInteger = Integer.parseInt(price);
            return wines.stream().filter(wine -> (wine.getPrice() <= priceInteger)).collect(Collectors.toList());
        }
        return wines;
    }
}
