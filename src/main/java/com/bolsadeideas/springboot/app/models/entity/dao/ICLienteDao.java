package com.bolsadeideas.springboot.app.models.entity.dao;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

public interface ICLienteDao {
  public List<Cliente> findAll();
}
