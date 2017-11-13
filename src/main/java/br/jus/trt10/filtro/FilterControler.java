/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.filtro
 * Classe    : FilterControler.java
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
package br.jus.trt10.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.jus.trt10.bean.LoginBean;

@WebFilter( displayName = "metas", urlPatterns = {"/metas"})
public class FilterControler implements Filter{

	
	public static final  String loginPage      = "/login.xhtml";
	private final static String FILTER_APPLIED = "_security_filter_applied";
	
	
	@Override
	public void destroy() {}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
		try {
	        
			HttpServletResponse response = (HttpServletResponse) res;
			HttpServletRequest request = (HttpServletRequest) req;
			
			HttpSession session = request.getSession();
			String user = null;
			
			if ( !request.getRequestURI().endsWith("/login.xhtml")
			  && !request.getRequestURI().contains("/javax.faces.resource/")) {
	        	request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
	        	
	        	// Se a sessão do bean não estiver nula, então pego a propriedade login do bean da sessão.  
	        	if (((LoginBean) session.getAttribute("loginBean")) != null) {  
	        		user = ((LoginBean)session.getAttribute("loginBean")).getLogin();
	        	}  
	        	
		        if ((user == null) || (user.equals(""))) {  
		        	response.sendRedirect(request.getContextPath() + "/login.xhtml" );
		        }else{
		        	chain.doFilter(req, res);
		        }
		        
	        }else{
	        	chain.doFilter(req, res);
	        }
			
	      
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
	}	
	
}
