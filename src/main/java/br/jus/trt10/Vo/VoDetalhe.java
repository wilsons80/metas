/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.Vo
 * Classe    : VoDetalheMeta.java
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
import java.sql.Clob;

public class VoDetalhe implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int    idDetalhe;
	private int    idItem;
	
	private String codigo;
	private String descricao;
	private String conexao;
	private Clob   consulta;
	private String consultaSql;
	private String valor;
	
	
	public int getIdDetalhe() {
		return idDetalhe;
	}
	public void setIdDetalhe(int idDetalhe) {
		this.idDetalhe = idDetalhe;
	}
	public int getIdItem() {
		return idItem;
	}
	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getConexao() {
		return conexao;
	}
	public void setConexao(String conexao) {
		this.conexao = conexao;
	}
	public Clob getConsulta() {
		return consulta;
	}
	public void setConsulta(Clob consulta) {
		this.consulta = consulta;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getConsultaSql() {
		return consultaSql;
	}
	public void setConsultaSql(String consultaSql) {
		this.consultaSql = consultaSql;
	}
	
	
	

}
