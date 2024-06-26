package org.example.searchenginems.filters.WinePipes;

import org.example.searchenginems.model.Wine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WineryFilter implements Filter<String> {
    @Override
    public List<Wine> execute(String wineryId, List<Wine> wines) {
        if(wineryId != null) {
            return wines.stream().filter(wine -> wine.getWinery().getId().equals(Long.parseLong(wineryId))).collect(Collectors.toList());
        }
        return wines;
    }
}
