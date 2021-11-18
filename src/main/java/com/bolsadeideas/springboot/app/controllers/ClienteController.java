package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.dao.ICLienteDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ClienteController {
  @Autowired
  @Qualifier("clienteJPA")
  private ICLienteDao clienteDao;

  @RequestMapping(value="/cliente",method = RequestMethod.GET)
  public String listar(Model model) {
    model.addAttribute("titulo", "Listado de clientes");
    model.addAttribute("clientes", clienteDao.findAll());
    return "listar";
  }
}
