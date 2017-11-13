/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.bean
 * Classe    : MetaBean.java
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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletContext;

import br.jus.trt10.Bc.BcMeta;
import br.jus.trt10.Util.FacesMessageUtil;
import br.jus.trt10.Vo.VoDetalhe;
import br.jus.trt10.Vo.VoMeta;
import br.jus.trt10.trt10util.DataUtil;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;


@ManagedBean(name="metaBean")
@ViewScoped
public class MetaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private BcMeta                   bcMetas;
	private Integer                  codigoMeta;
	private ArrayList<VoDetalhe> listaDetalheMeta;
	private ArrayList<VoMeta>        listaMeta;
	private boolean                  habilitarParametro = true; 
	private String                   parametro = null;
	private ArrayList<VoMeta> lista = new ArrayList<VoMeta>();

	
	public void initValores(){
		codigoMeta = 0;
		parametro  = DataUtil.formataData(DataUtil.adicionaMeses(DataUtil.getHoje(), -1),"MM/yyyy");
	}
	
	public String enviarPagMetas(){
		return "/formprincipal?faces-redirect=true";
	}
	
	public void obterListaMetas(){
		initBcMeta();
		try {
			if(lista == null || lista.isEmpty()){
				lista = bcMetas.getListaMetas();
			}
		} catch (Exception e) {
			FacesMessageUtil.informarMessageError(null, "Não foi possível recuperar a lista de Itens de Metas.");
		}
		setListaMeta(lista);
	}
	
	
	
	public void pesquisar(){
		setListaDetalheMeta  (null);
		setHabilitarParametro(true);
		try {
			initBcMeta();
			
			if(parametro == null || parametro.equals("")){
				FacesMessageUtil.informarMessageInfo(null, "O campo Data deve ser informado.");
				return;
			}
			
			if(codigoMeta > 0){
				VoMeta item = new VoMeta();
				item.setCodigo(codigoMeta);
				ArrayList<VoDetalhe> listaDetalhe = bcMetas.obterDetalhesMetas(item,parametro,true);
				if(listaDetalhe.isEmpty()){
					FacesMessageUtil.informarMessageError(null, "Não há detalhes para essa Meta.");
					return;
				}
				setListaDetalheMeta(listaDetalhe);
				setHabilitarParametro(false);
			}
			
		} catch (Exception e2) {
			FacesMessageUtil.informarMessageError(null, "Não foi possível obter os detalhes da Meta.");
			e2.printStackTrace();
		}

	}
	
	public ArrayList<VoMeta> getListaEstItem() throws Exception{
		obterListaMetas();
		return listaMeta;
	}	

	public void mostrarParametro(AjaxBehaviorEvent e) throws Exception {
		setHabilitarParametro(true);
		setListaDetalheMeta  (null);
		setParametro         (null);
		
		UIComponent source = (UIComponent)e.getSource();
		int codigo = (int) ((HtmlSelectOneMenu)source).getValue();
		
		setCodigoMeta(codigo);
		if(codigo > 0){
			//será usado para definir os parâmetros de acordo com a configuração da meta
			//VoMeta vo = bcMetas.getMeta(codigoMeta);

			setHabilitarParametro(false);
		}
	}
	
	
	public void postProcessPDF(Object document) throws IOException,BadElementException , DocumentException{
		
	}
	
	public void preProcessPDF(Object document) throws IOException,BadElementException , DocumentException{
		initBcMeta();
		
		Document pdf = (Document) document;
		
		//coluna direita, coluna esquerda, topo, rodape
		pdf.setMargins(10f, 10f, 0f, 10f);
		pdf.setPageSize(PageSize.A4);
		
		pdf.open();
		
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		
		//cria a imagem e passando a url  
		String logo = servletContext.getRealPath("/resources") + File.separator + "imagens" + File.separator + "cab_logo.png";
	    Image image = Image.getInstance(logo);
	    image.setAlignment(Image.ALIGN_CENTER);
	    pdf.add(image);
		
		try {
			//Obtenho a descrição da meta...
			VoMeta vo = bcMetas.getMeta(codigoMeta);
			
			Paragraph p;
			
			//Adiciona a descrição da meta no pdf
			p = new Paragraph(vo.getDescricao(), FontFactory.getFont(FontFactory.TIMES, 10));
			p.setSpacingBefore(10);
			p.setSpacingAfter (5);
			p.setAlignment(Element.ALIGN_LEFT);
			
			pdf.add(p);
			
			
			p = new Paragraph("Periodo: " + parametro, FontFactory.getFont(FontFactory.TIMES,10));
			p.setSpacingAfter (20);
			p.setAlignment(Element.ALIGN_LEFT);
			
			pdf.add(p);

			pdf.setHtmlStyleClass("fontConteudo");
			
		} catch (Exception e) {
			FacesMessageUtil.informarMessageError(null, "Não foi possível obter a descrição da Meta.");
			return;
		}
	    
	}
	
	
	
	public ArrayList<VoMeta> getListaMeta() {return listaMeta;}
	public void setListaMeta(ArrayList<VoMeta> listaMeta) {this.listaMeta = listaMeta;}

	public ArrayList<VoDetalhe> getListaDetalheMeta() {return listaDetalheMeta;}
	public void setListaDetalheMeta(ArrayList<VoDetalhe> listaDetalheMeta) {this.listaDetalheMeta = listaDetalheMeta;}

	public Integer getCodigoMeta() {return codigoMeta;}
	public void setCodigoMeta(Integer codigoMeta) {this.codigoMeta = codigoMeta;}
	
	public boolean isHabilitarParametro() {return habilitarParametro;}
	public void setHabilitarParametro(boolean habilitarParametro) {this.habilitarParametro = habilitarParametro;}

	public String getParametro() {if( parametro == null ) {parametro = DataUtil.formataData(DataUtil.adicionaMeses(DataUtil.getHoje(), -1),"MM/yyyy"); return parametro;}return parametro;}
	public void setParametro(String parametro) {this.parametro = parametro;}

	private void initBcMeta(){if(bcMetas == null){bcMetas = new BcMeta();}}
}
