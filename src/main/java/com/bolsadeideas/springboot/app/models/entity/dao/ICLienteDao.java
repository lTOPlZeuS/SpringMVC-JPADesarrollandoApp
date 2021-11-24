package com.bolsadeideas.springboot.app.models.entity.dao;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICLienteDao extends PagingAndSortingRepository<Cliente, Long> {
  
}
