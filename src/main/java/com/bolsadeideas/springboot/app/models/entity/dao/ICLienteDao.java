package com.bolsadeideas.springboot.app.models.entity.dao;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

import org.springframework.data.repository.CrudRepository;

public interface ICLienteDao extends CrudRepository<Cliente, Long> {
  
}
