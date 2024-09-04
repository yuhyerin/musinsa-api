package com.musinsa.style.repository;

import com.musinsa.style.repository.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, BrandCustomRepository {

}
