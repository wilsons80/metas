package br.jus.trt10.negocio;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.jus.trt10.entidade.ParametroGeral;

/**
 *
 * @author wilson.souza
 */
@Stateless
public class ParametroGeralBO {

   @PersistenceContext(unitName = "metas")
   private EntityManager em;

   public List<ParametroGeral> getParametrosGerais() {
      Query q = em.createQuery("SELECT p FROM ParametroGeral p");
      return q.getResultList();
   }

   public ParametroGeral getParametro(String nome) {
      Query q = em.createQuery("SELECT p FROM ParametroGeral p WHERE p.nome = :nome ");
      q.setParameter("nome", nome);
      return (ParametroGeral) q.getSingleResult();
   }
}
