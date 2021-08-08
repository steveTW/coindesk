package com.test.coindesk.repository;

import com.test.coindesk.currencymapping.CurrencyNameMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrencyNameMappingRepository extends JpaRepository<CurrencyNameMapping, String> {
}
