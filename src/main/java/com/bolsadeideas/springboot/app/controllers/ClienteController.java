package com.bolsadeideas.springboot.app.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.pageRender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
  @Autowired
  // @Qualifier("clienteJPA")
  private IClienteService iClienteService;

  @Autowired
  private IUploadFileService uploadFileService;

  @GetMapping(value = "/uploads/{filename:.+}")
  public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
    Resource recurso = null;
    try {
      recurso = uploadFileService.load(filename);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
      .body(recurso);
  }

  @GetMapping(value = "/ver/{id}")
  public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
    Cliente cliente = iClienteService.findOne(id);
    if (cliente == null) {
      flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
      return "redirect:/listar";
    }

    model.put("cliente", cliente);
    model.put("titulo", "Detalle del cliente: " + cliente.getNombre());
    return "ver";
  }

  @RequestMapping(value="/cliente",method = RequestMethod.GET)
  public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model) {
    Pageable pageRequest = PageRequest.of(page, 5);
    
    Page<Cliente> clientes = iClienteService.findAll(pageRequest);

    pageRender<Cliente> pageRender = new pageRender<>("/cliente", clientes);
    model.addAttribute("titulo", "Listado de clientes");
    model.addAttribute("clientes", clientes);
    model.addAttribute("page", pageRender);
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
  public String editar(@PathVariable(value="id") Long id, Map<String,Object> model,RedirectAttributes flash) {
    Cliente cliente = null;

    if(id > 0) {
      cliente = iClienteService.findOne(id);
      if(cliente == null) {
        flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
        return "redirect:/listar";
      }
    } else {
      flash.addFlashAttribute("error", "El ID del cliente no puede ser cero");
      return "redirect:/cliente";
    }

    model.put("cliente", cliente);
    model.put("titulo", "Editar cliente");
    return "form";
  }

  @PostMapping(value="/form")
  public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto ,RedirectAttributes flash,SessionStatus status) {
    if(result.hasErrors()) {
      model.addAttribute("titulo", "Formulario de cliente");
      return "form";
    }
    if (!foto.isEmpty() ) {
      if(cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
        Path rootPath = Paths.get("uploads").resolve(cliente.getFoto()).toAbsolutePath();
        File archivo = rootPath.toFile();
        if(archivo.exists() && archivo.canRead()) {
          archivo.delete();
        }
        uploadFileService.delete(cliente.getFoto());
      }
      String uniqueFilename = null;
      try {
        uniqueFilename = uploadFileService.copy(foto);
      } catch (IOException e) {
        e.printStackTrace();
      }
      flash.addFlashAttribute("info", "Se ha subido correctamente el archivo " + uniqueFilename + "!");
      cliente.setFoto(uniqueFilename);
    }
    String mensaje = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!";
    String statusMensaje = (cliente.getId() != null) ? "info" : "success";
    iClienteService.save(cliente);
    status.setComplete();
    flash.addFlashAttribute(statusMensaje, mensaje);
    return "redirect:/cliente";
  }

  @GetMapping(value="/form/eliminar/{id}")
  public String eliminar(@PathVariable(value="id") Long id,RedirectAttributes flash) {
      Cliente cliente = iClienteService.findOne(id);
      iClienteService.delete(id);
      flash.addFlashAttribute("success", "Cliente eliminado con éxito");
      if(uploadFileService.delete(cliente.getFoto())) {
        flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
      }
    return "redirect:/cliente";
  }
}
