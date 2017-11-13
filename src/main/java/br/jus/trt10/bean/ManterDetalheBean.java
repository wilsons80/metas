/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.Bean
 * Classe    : ManterDetalheBean.java
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
package br.jus.trt10.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.jus.trt10.Bc.BcDetalhe;
import br.jus.trt10.Util.FacesMessageUtil;
import br.jus.trt10.Vo.VoDetalhe;
import br.jus.trt10.Vo.VoMeta;
import br.jus.trt10.modelo.ConexaoEnun;

@ManagedBean(name="manterDetalheBean")
@ViewScoped
public class ManterDetalheBean extends AbstractBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private BcDetalhe     bcDetalhe;
	private VoDetalhe detalheSelecionado;
	private VoDetalhe detalheEdicao = new VoDetalhe();
	
	private String conexaoDS;
	private int    codMetaSelecionado;
	
	private ArrayList<VoDetalhe> listaDetalhes;
	
	
	@Override
	@PostConstruct
	protected void iniciar() {
		verificaPermissaoTela("tl_manter");
	}
	
	
	public void prepararNovoCadastro(){
		detalheEdicao = new VoDetalhe();
		detalheEdicao.setIdItem(codMetaSelecionado);
	}
	
	
	public void excluirDetalhe(){
		try {
			initBcDetalhe();
			
			ArrayList<VoDetalhe> lista = new ArrayList<VoDetalhe>();
			lista.add(detalheSelecionado);
			
			bcDetalhe.excluir(lista);
			
			consultarListDetalhes(detalheEdicao.getIdItem(),false);
			detalheSelecionado = null;
		} catch (Exception e) {
			FacesMessageUtil.informarMessageError(null, "Não foi possível excluir o detalhe da Meta." + e.getMessage());
			return;
		}
		FacesMessageUtil.informarMessageInfo(null, "Operação realizada com Sucesso!!!");
		
		//Atualiza os componentes apenas se o cadastro for com sucesso.
		RequestContext.getCurrentInstance().update(Arrays.asList(":formDetalhe:tabDetalheEdicao"));

	}
	
	public void salvar(){
		try {
			initBcDetalhe();
			bcDetalhe.salvar(detalheEdicao);
			
			consultarListDetalhes(detalheEdicao.getIdItem(),false);
			
			detalheSelecionado = null;
		} catch (Exception e) {
			FacesMessageUtil.informarMessageError(null, "Não foi possível salvar o detalhe da Meta." + e.getMessage());
			return;
		}
		FacesMessageUtil.informarMessageInfo(null, "Operação realizada com Sucesso!!!");
		
		//Atualiza os componentes apenas se o cadastro for com sucesso.
		RequestContext.getCurrentInstance().update(Arrays.asList(":formDetalhe:tabDetalheEdicao"));
	}

	public ArrayList<String> getListaConexaoDS(){
		ArrayList<String> lista = new ArrayList<String>();
		for(ConexaoEnun conexao: ConexaoEnun.values()){
			lista.add(conexao.getNome().toUpperCase());
		}
		return lista;
	}
	
	
	
	private void consultarListDetalhes(int codigoMeta,boolean pesqValor){
		try {
			initBcDetalhe();
			listaDetalhes = new ArrayList<VoDetalhe>();
			listaDetalhes = bcDetalhe.getListaDetalhes(codigoMeta,pesqValor);
		} catch (Exception e) {
			FacesMessageUtil.informarMessageError(null, "Não foi possível recuperar a lista de Detalhes da Meta.");
		}
	}
	
	public ArrayList<VoDetalhe> getListaDetalhes() {
		return listaDetalhes;
	}

	
	
	public void onRowSelect(SelectEvent event) {
		int codigo = ((VoMeta) event.getObject()).getCodigo() ;
		setCodMetaSelecionado(codigo);
	    consultarListDetalhes(codigo,false);
	}
	 
	public void onRowUnselect(UnselectEvent event) {
		setCodMetaSelecionado(0);
		listaDetalhes      = null;
		detalheEdicao      = null;
	}	
	
	
	

	public VoDetalhe getDetalheSelecionado() {return detalheSelecionado;}
	public void setDetalheSelecionado(VoDetalhe detalheSelecionado) {this.detalheSelecionado = detalheSelecionado;}
	
	public VoDetalhe getDetalheEdicao() {return detalheEdicao;}
	public void setDetalheEdicao(VoDetalhe detalheEdicao) {this.detalheEdicao = detalheEdicao;}
	
	public String getConexaoDS() {return conexaoDS;}
	public void setConexaoDS(String conexaoDS) {this.conexaoDS = conexaoDS;}


	public int getCodMetaSelecionado() {return codMetaSelecionado;}
	public void setCodMetaSelecionado(int codMetaSelecionado) {this.codMetaSelecionado = codMetaSelecionado;}


	private void initBcDetalhe(){if(bcDetalhe == null){bcDetalhe = new BcDetalhe();}}


}
