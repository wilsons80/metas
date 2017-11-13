/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.Util
 * Classe    : FacesMessageUtil.java
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
package br.jus.trt10.Util;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

public class FacesMessageUtil implements Serializable{

	private static final long serialVersionUID = 1L;


	public static void informarMessageError(String titulo, String mensagem){
		if(titulo == null){titulo = "Metas Estatísticas";}
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, mensagem);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
	
	
	public static void informarMessageInfo(String titulo, String mensagem){
		if(titulo == null){titulo = "Metas Estatísticas";}
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, mensagem);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	
	
	private static void add(String message, Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage(message);
		msg.setSeverity(severity);
		
		context.addMessage(null, msg);
	}

	public static void info(String message) {
		add(message, FacesMessage.SEVERITY_INFO);
	}
	
	public static void error(String message) {
		add(message, FacesMessage.SEVERITY_ERROR);
	}
}
