package com.test.coindesk.controller;

import com.test.coindesk.currencymapping.CurrencyNameMapping;
import com.test.coindesk.service.CurrencyNameMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/currency/mapping")
public class CurrencyNameMappingController {

    @Autowired private CurrencyNameMappingService service;

    @GetMapping("all")
    public List<CurrencyNameMapping> fetchAll(){
        return service.getAll();
    }

    @GetMapping("{currency}")
    public CurrencyNameMapping getMapping(@PathVariable String currency){
        return service.getMapping(currency);
    }

    @PutMapping("{currency}")
    public CurrencyNameMapping update(@PathVariable String currency, @NonNull @RequestBody CurrencyNameMapping mapping) {
       return service.update(currency, mapping);
    }

    @PostMapping
    public String insert(@NonNull @RequestBody CurrencyNameMapping mapping) {
        return service.insert(mapping);
    }

    @DeleteMapping("{currency}")
    public void delete(@PathVariable String currency) {
        service.delete(currency);
    }
}
