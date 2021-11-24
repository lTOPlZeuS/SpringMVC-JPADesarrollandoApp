package com.bolsadeideas.springboot.app.util.paginator;

public class pageItem {
  private int numero;
  private boolean actual; 


  public pageItem(int numero, boolean actual) {
    this.numero = numero;
    this.actual = actual;
  }

  public int getNumero() {
    return this.numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public boolean isActual() {
    return this.actual;
  }

  public boolean getActual() {
    return this.actual;
  }

}
