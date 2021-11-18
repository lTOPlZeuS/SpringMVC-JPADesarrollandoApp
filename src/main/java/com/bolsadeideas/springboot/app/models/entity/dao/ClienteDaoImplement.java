package com.bolsadeideas.springboot.app.models.entity.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

import org.springframework.data.annotation.Persistent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClienteDaoImplement implements ICLienteDao {

  @PersistenceContext
  private EntityManager em;

  @SuppressWarnings("unchecked")
  // Ponemos que es solo de lectura
  @Transactional(readOnly = true)
  @Override
  public List<Cliente> findAll() {
    return em.createQuery("from Cliente").getResultList();
  }

}
