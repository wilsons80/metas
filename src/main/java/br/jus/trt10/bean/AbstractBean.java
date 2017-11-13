package br.jus.trt10.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import br.jus.trt10.negocio.ParametroGeralBO;
import br.jus.trt10.sca.restcliente.vo.sistema.Recurso;
import br.jus.trt10.sca.restcliente.vo.usuario.Usuario;

public abstract class AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static String NOME_SISTEMA = "Sistema de Metas";
	protected String URL_WEBSERVRESTSCA;
   

   @EJB
   protected ParametroGeralBO parametroGeralBo;
   
   public AbstractBean() {
      Locale.setDefault(new Locale("pt", "BR"));
   }

   @PostConstruct
   private void inicio() {
      if (URL_WEBSERVRESTSCA == null) {
         URL_WEBSERVRESTSCA = parametroGeralBo.getParametro("URL_WEBSERVICE_SCA_REST").getValor();
      }
   }   
   
   protected abstract void iniciar();

   protected FacesContext getContext() {
      return FacesContext.getCurrentInstance();
   }

   protected void getMensagemErro(String mensagem, String descricao) {
      getMensagens(FacesMessage.SEVERITY_ERROR, mensagem, descricao);
   }

   protected void getMensagemInfo(String mensagem, String descricao) {
      getMensagens(FacesMessage.SEVERITY_INFO, mensagem, descricao);
   }

   protected void getMensagemWarn(String mensagem, String descricao) {
      getMensagens(FacesMessage.SEVERITY_WARN, mensagem, descricao);
   }

   private void getMensagens(Severity fm, String msg, String desc) {
      getContext().addMessage(null, new FacesMessage(fm, msg, desc));
   }
   
   public static void popupMensagemErro(String mensagem){
      popupMensagem(mensagem, FacesMessage.SEVERITY_ERROR);
   }
   
   public static void popupMensagemWarn(String mensagem){
      popupMensagem(mensagem, FacesMessage.SEVERITY_WARN);
   }
   
   public static void popupMensagemInfo(String mensagem){
      popupMensagem(mensagem, FacesMessage.SEVERITY_INFO);
   }
   
   public static void popupMensagemFatal(String mensagem){
      popupMensagem(mensagem, FacesMessage.SEVERITY_FATAL);
   }
   
   public static void popupMensagem(String mensagem, Severity fm) {
      FacesMessage message = new FacesMessage(fm, NOME_SISTEMA, mensagem);
      RequestContext.getCurrentInstance().showMessageInDialog(message);
   }

   protected void setUsuarioSessao(Usuario usr) {
      getContext().getExternalContext().getSessionMap().put("usuairoSessao", usr);
   }

   public Usuario getUsuarioSessao() {
      return (Usuario) getContext().getExternalContext().getSessionMap().get("usuairoSessao");
   }

   protected void setPermissoesSistema(List<Recurso> ltPR) {
      getContext().getExternalContext().getSessionMap().put("listaPermissao", ltPR);
   }

   @SuppressWarnings("unchecked")
   public List<Recurso> getPermissoesSistema() {
      return (List<Recurso>) getContext().getExternalContext().getSessionMap().get("listaPermissao");
   }

   protected void setObjetoSessao(String desc, Object obj) {
      getContext().getExternalContext().getSessionMap().put(desc, obj);
   }

   protected Object getObjetoSessao(String desc) {
      return getContext().getExternalContext().getSessionMap().get(desc);
   }

   public String getDescContesto() {
      return getRequeste().getContextPath();
   }

   protected void redirectInterno(String pagina) throws IOException {
      if (pagina.contains("jsf/")) {
         getContext().getExternalContext().redirect(getDescContesto().concat("/").concat(pagina));
      } else {
         getContext().getExternalContext().redirect(getDescContesto().concat("/jsf/").concat(pagina));
      }
   }

   protected void redirectExterno(String pagina) throws IOException {
      getContext().getExternalContext().redirect(pagina);
   }

   public String getPagina() {
      return getContext().getViewRoot().getViewId();
   }

   public HttpServletRequest getRequeste() {
      return (HttpServletRequest) getContext().getExternalContext().getRequest();
   }

   public boolean getPermissaoAcesso(String recurso) {
      List<Recurso> ltRecurso = getPermissoesSistema();
      if(ltRecurso!=null){
         for (Recurso pr : ltRecurso) {
            if (pr.getNome().equalsIgnoreCase(recurso)) {
               return true;
            }
         }
      }
      return false;
   }

   public void verificaPermissaoTela(String nomeTela) {
      if (!getPermissaoAcesso(nomeTela)) {
         try {
            redirectInterno("/permissaoAcesso.jsf");
         } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   

}
