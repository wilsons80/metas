/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.modelo
 * Classe    : ConexaoEnun.java
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
package br.jus.trt10.modelo;

public enum ConexaoEnun {

	pje_1g    ("PJE-1G"),
	pje_2g    ("PJE-2G"),
	user_int01("USER-INT01");
	
	private ConexaoEnun(String nome){
		this.nome = nome;
	}
	
	private String nome;

	public String getNome() {return nome;}
	public void setNome(String conexao) {this.nome = conexao;}
	
    @Override
    public String toString() {
        return this.nome;
    }
	
}
