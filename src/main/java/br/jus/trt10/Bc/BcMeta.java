/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.Bc
 * Classe    : BcMeta.java
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
package br.jus.trt10.Bc;

import java.util.ArrayList;

import br.jus.trt10.BancoDao.ConexaoDao;
import br.jus.trt10.Vo.VoDetalhe;
import br.jus.trt10.Vo.VoMeta;

public class BcMeta {
	
	private ConexaoDao conexaoDao = null;
	
	public BcMeta(){}
	
	
	public ArrayList<VoMeta> getListaMetas() throws Exception{
		initConexaoDao();
		
		String datasource = "user-int01";
		return conexaoDao.obterListaMetas(datasource);
	}

	
	public VoMeta getMeta(int codigo) throws Exception{
		initConexaoDao();
		
		String datasource = "user-int01";
		return conexaoDao.obterMeta(codigo,datasource);
	}
	
	
	public ArrayList<VoDetalhe> obterDetalhesMetas(VoMeta vo, String paramBusca,boolean pesqValor) throws Exception{
		initConexaoDao();
		
		String datasource = "user-int01";
		return conexaoDao.getListaDetalhesMetas(vo.getCodigo(),datasource,paramBusca,pesqValor);
	}
	
	
	public void salvar(VoMeta vo) throws Exception{
		initConexaoDao();

		String datasource = "user-int01";
		conexaoDao.salvarMeta(vo, datasource);
	}
	
	public int excluir(VoMeta vo) throws Exception{
		initConexaoDao();
		
		String datasource = "user-int01";
		int retorno = conexaoDao.excluirMetas(vo, datasource);

		return retorno;
	}
	
	
	private void initConexaoDao(){if(conexaoDao == null){conexaoDao = new ConexaoDao();}}
}
