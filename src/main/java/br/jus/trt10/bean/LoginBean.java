/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.bean
 * Classe    : LoginBean.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.ViewExpiredException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.jus.trt10.sca.restcliente.LoginRest;
import br.jus.trt10.sca.restcliente.vo.sistema.Recurso;
import br.jus.trt10.sca.restcliente.vo.usuario.Perfil;
import br.jus.trt10.sca.restcliente.vo.usuario.Usuario;


@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean extends AbstractBean  {

	private static final long serialVersionUID = 1L;
	private String  login ;
	private String  senha ;
	
	private LoginRest loginRest;
	
	public LoginBean(){}

	
	@Override
	@PostConstruct
	protected void iniciar() {
	   loginRest = new LoginRest(URL_WEBSERVRESTSCA, "meta");
	}
	

	public String logar() {
		String       resultado = "";
		try {
			if(login == null || login.equals("")){throw new Exception("Informe o usuario!");}
			if(senha == null || senha.equals("")){throw new Exception("Informe a senha!");}

			Usuario usr = loginRest.loginSistema(login,senha);
			if (usr == null) {
				getMensagemErro("Erro ao efetuar login","Usuario ou senha invalidos!");
				return "";
			}
			setUsuarioSessao(usr);

			List<Recurso> ltRecursos = new ArrayList<>();
			for (Perfil perf : usr.getListaPerfilGlobal()) {
				ltRecursos.addAll(loginRest.recuperaPerfilGlobal(perf.getIdPerfil()));
			}

			if (ltRecursos.isEmpty()) {
				finalizarSessao();
				return "/permissaoAcesso.jsf";
			}

			setPermissoesSistema(ltRecursos);
			resultado = "formprincipal?faces-redirect=true";
			
		} catch (Exception ex) {
			finalizarSessao();
			Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE,null,ex);
			if (ex.getMessage().contains("/scatrt10")) {
				getMensagemErro("Erro ao efetuar login: ","Serviço de login temporariamente indisponível.");
			} else {
				getMensagemErro("Erro ao efetuar login: ",ex.getMessage());
			}
			return "";
		}
		
		return resultado;
	}

	public String getVerificaLogin() {
		try {
			Usuario usr = getUsuarioSessao();
			String pgn = getPagina();
			if (usr != null && !"/login.xhtml".equals(pgn)) {
				return "";
			} else if (usr == null && !pgn.endsWith("/login.xhtml")) {
				try {
					redirectInterno("login.xhtml");
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,null, e);
				}
				return "";
			} else {
				return "";
			}
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return "";
		}
	}
	
	
	public String typeSessaoExpirada = "javax.faces.application.ViewExpiredException";
	public String getTypeSessaoExpirada() {return typeSessaoExpirada;}

	public void getLogout() {
        throw new ViewExpiredException("A ViewExpiredException!",FacesContext.getCurrentInstance().getViewRoot().getViewId());
    }

	public String logout() {
	    setUsuarioSessao    (null);
	    setPermissoesSistema(null);
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login?faces-redirect=true";
	}
	
	public void finalizarSessao(){
	    setUsuarioSessao    (null);
	    setPermissoesSistema(null);

	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
	
	public String getLogin() {return login;}
	public void setLogin(String login) {this.login = login;}

	
	public String getSenha() {return senha;}
	public void setSenha(String senha) {this.senha = senha;}


	
}
