package com.test.coindesk.service;

import com.test.coindesk.currencymapping.CurrencyNameMapping;
import com.test.coindesk.currencymapping.CurrencyNameMappingException;
import com.test.coindesk.repository.CurrencyNameMappingRepository;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyNameMappingService {
    @Autowired
    private CurrencyNameMappingRepository repository;

    public List<CurrencyNameMapping> getAll() {
        try {
            return repository.findAll();
        } catch (Exception e){
            throw new CurrencyNameMappingException("取得幣別列表失敗", e);
        }
    }

    public CurrencyNameMapping getMapping(String id) {
        Optional<CurrencyNameMapping> mapping = repository.findById(id);
        if (mapping.isPresent()) {
            return mapping.get();
        }
        else throw new CurrencyNameMappingException("查無資料");
    }

    public String insert(CurrencyNameMapping mapping) {
        try {
            Asserts.notBlank(mapping.getCurrency(), "幣別");
            Asserts.notBlank(mapping.getName(), "中文名稱");
            repository.save(mapping);
            return mapping.getCurrency();
        } catch (Exception e){
            throw new CurrencyNameMappingException("新增失敗", e);
        }
    }

    public CurrencyNameMapping update(String id, CurrencyNameMapping mapping) {
        Optional<CurrencyNameMapping> orig = repository.findById(id);

        if (orig.isPresent()) {
            try {
                Asserts.notBlank(mapping.getCurrency(), "幣別");
                Asserts.notBlank(mapping.getName(), "中文名稱");
                CurrencyNameMapping currencyNameMapping = orig.get();
                currencyNameMapping.setName(mapping.getName());
                repository.save(currencyNameMapping);
                return currencyNameMapping;
            } catch (Exception e){
                throw new CurrencyNameMappingException("更新失敗", e);
            }
        }
        else throw new CurrencyNameMappingException("查無資料");
    }

    public void delete(String id) {
        Optional<CurrencyNameMapping> mapping = repository.findById(id);
        if (mapping.isPresent()) {
            try {
                repository.deleteById(id);
            } catch (Exception e) {
                throw new CurrencyNameMappingException("刪除失敗", e);
            }
        }else throw new CurrencyNameMappingException("查無資料");
    }
}
