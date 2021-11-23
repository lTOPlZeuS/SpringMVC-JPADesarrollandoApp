package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
  @Autowired
  // @Qualifier("clienteJPA")
  private IClienteService iClienteService;

  @RequestMapping(value="/cliente",method = RequestMethod.GET)
  public String listar(Model model) {
    model.addAttribute("titulo", "Listado de clientes");
    model.addAttribute("clientes", iClienteService.findAll());
    return "listar";
  }

  @GetMapping(value="/form")
  public String crear(Map<String,Object> model) {
    Cliente cliente = new Cliente();
    model.put("cliente", cliente);
    model.put("titulo", "Formulario de cliente");
    return "form";
  }

  @GetMapping(value="/form/{id}")
  public String editar(@PathVariable(value="id") Long id, Map<String,Object> model) {
    Cliente cliente = null;

    if(id > 0) {
      cliente = iClienteService.findOne(id);
    } else {
      return "redirect:/cliente";
    }

    model.put("cliente", cliente);
    model.put("titulo", "Editar cliente");
    return "form";
  }

  @PostMapping(value="/form")
  public String guardar(@Valid Cliente cliente, BindingResult result, Model model,SessionStatus status) {
    if(result.hasErrors()) {
      model.addAttribute("titulo", "Formulario de cliente");
      return "form";
    }
    iClienteService.save(cliente);
    status.setComplete();
    return "redirect:/cliente";
  }

  @GetMapping(value="/form/eliminar/{id}")
  public String eliminar(@PathVariable(value="id") Long id) {
    if(id > 0) {
      iClienteService.delete(id);
    }
    return "redirect:/cliente";
  }
}
