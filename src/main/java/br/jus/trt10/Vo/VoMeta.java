/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.Vo
 * Classe    : VoMeta.java
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
package br.jus.trt10.Vo;

import java.io.Serializable;

public class VoMeta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int    codigo;
	private String descricao;
	private String periodicidade;
	
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getPeriodicidade() {
		return periodicidade;
	}
	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	
	
}
