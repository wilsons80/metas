/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jus.trt10.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wilson.souza
 */
@Entity
@Table(name = "TB_PARAMETRO_GERAL", catalog = "", schema = IEntity.SCHEMA)
public class ParametroGeral implements IEntity {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "ID_PARAMENTRO_GERAL", nullable = false)
   private Long idParamentroGeral;
   @Size(max = 30)
   @Column(name = "DS_NOME", length = 30)
   private String nome;
   @Size(max = 400)
   @Column(name = "DS_VALOR", length = 400)
   private String valor;
   @Size(max = 100)
   @Column(name = "DS_DESCRICAO", length = 100)
   private String descricao;

   public ParametroGeral() {
   }

   public ParametroGeral(Long idParamentroGeral) {
      this.idParamentroGeral = idParamentroGeral;
   }

   public Long getIdParamentroGeral() {
      return idParamentroGeral;
   }

   public void setIdParamentroGeral(Long idParamentroGeral) {
      this.idParamentroGeral = idParamentroGeral;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getValor() {
      return valor;
   }

   public void setValor(String valor) {
      this.valor = valor;
   }

   public String getDescricao() {
      return descricao;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (idParamentroGeral != null ? idParamentroGeral.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof ParametroGeral)) {
         return false;
      }
      ParametroGeral other = (ParametroGeral) object;
      if ((this.idParamentroGeral == null && other.idParamentroGeral != null) || (this.idParamentroGeral != null && !this.idParamentroGeral.equals(other.idParamentroGeral))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "ParametrosGerais{" + "idParamentroGeral=" + idParamentroGeral + ", nome=" + nome + ", valor=" + valor + ", descricao=" + descricao + '}';
   }

}
