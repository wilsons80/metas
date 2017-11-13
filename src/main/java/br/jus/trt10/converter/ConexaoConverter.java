/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.converter
 * Classe    : ConexaoConverter.java
 * Autor     : wilson.souza
 * Data      : 22/06/2015
 * Descrição : 
 * 
 * Modificações 
 * 
 * Responsavel  Data        Descrição
 * ===========  ==========  =====================================================               
 *
 */
package br.jus.trt10.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("conexaoConverter")
public class ConexaoConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,Object value) {
		return value.toString().toUpperCase();
	}

}
