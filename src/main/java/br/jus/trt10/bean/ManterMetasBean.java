/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.bean
 * Classe    : ManterMetasBean.java
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

import br.jus.trt10.Bc.BcMeta;
import br.jus.trt10.Util.FacesMessageUtil;
import br.jus.trt10.Vo.VoMeta;

@ManagedBean(name="manterMetasBean")
@ViewScoped
public class ManterMetasBean extends AbstractBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private BcMeta            bcMetas;
	private VoMeta            metaSelecionada;
	private VoMeta            metaEdicao = new VoMeta() ;
	
	private ArrayList<VoMeta> listaMetas;
 	
	public String enviarPagManter(){
		return "/adm/manter?faces-redirect=true";
	}

	
	@Override
	@PostConstruct
	protected void iniciar() {
		verificaPermissaoTela("tl_manter");
	}
	
	
	private void consultarListaMetas(){
		try {
			initBcMeta();
			listaMetas = new ArrayList<VoMeta>();
			listaMetas = bcMetas.getListaMetas();
			listaMetas.remove(0);
		} catch (Exception e) {
			FacesMessageUtil.informarMessageError(null, "Não foi possível recuperar a lista de Itens de Metas.");
		}
	}
	
	
	public ArrayList<VoMeta> getListaMetas(){
		consultarListaMetas();
		return listaMetas;
	}

	public void incluirMeta(){
		
	}
	
	public void excluirMeta(){
		try {
			initBcMeta();
			
			int retorno = bcMetas.excluir(metaEdicao);
			
			if(retorno == -9){FacesMessageUtil.informarMessageError(null,"Existem detalhes para essa Meta!"); return;}
			if(retorno !=  1){throw new Exception();}
			
			consultarListaMetas();
			metaSelecionada = null;
		} catch (Exception e) {
			FacesMessageUtil.informarMessageError(null, e.getMessage());
			return;
		}
		FacesMessageUtil.informarMessageInfo(null, "Operação realizada com Sucesso!!!");
		
		//Atualiza os componentes apenas se o cadastro for com sucesso.
		RequestContext.getCurrentInstance().update(Arrays.asList(":formDetalhe:tabMetaEdicao"));
	}
	
	
	public void prepararNovoCadastroMeta(){
		metaEdicao = new VoMeta();
	}

	public void salvar(){
		try {
			initBcMeta();
			bcMetas.salvar(metaEdicao);
			consultarListaMetas();
			
			metaSelecionada = null;
		} catch (Exception e) {
			FacesMessageUtil.informarMessageError(null, "Não foi possível salvar a Meta." + e.getMessage());
			return;
		}
		FacesMessageUtil.informarMessageInfo(null, "Operação realizada com Sucesso!!!");
		
		//Atualiza os componentes apenas se o cadastro for com sucesso.
		RequestContext.getCurrentInstance().update(Arrays.asList("formMeta:tabMetasEdicao"));
	}
	
	
	
	public VoMeta getMetaSelecionada() {return metaSelecionada;}
	public void setMetaSelecionada(VoMeta metaSelecionada) {this.metaSelecionada = metaSelecionada;}
	
	public VoMeta getMetaEdicao() {return metaEdicao;}
	public void setMetaEdicao(VoMeta metaEdicao) {this.metaEdicao = metaEdicao;}


	private void initBcMeta(){if(bcMetas == null){bcMetas = new BcMeta();}}
}
