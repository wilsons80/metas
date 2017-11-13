package br.jus.trt10.entidade;

import java.io.Serializable;

/**
 *
 * @author wilson.souza
 */
public interface IEntity extends Serializable {

   static final String SCHEMA = "SIS_ACESSO10";
   static final String ID_GENERATOR = "ID_GENERATOR";
   static final String VERSION = "NR_VERSAO";

   static final String SCHEMA_PJE_CLIENT = "CLIENT";

}
