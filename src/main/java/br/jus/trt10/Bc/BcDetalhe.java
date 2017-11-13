/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.Bc
 * Classe    : BcDetalhe.java
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

public class BcDetalhe {

	private ConexaoDao conexaoDao = null;


	public ArrayList<VoDetalhe> getListaDetalhes(int codMeta,boolean pesqValor) throws Exception{
		initConexaoDao();
		
		String datasource = "user-int01";
		return conexaoDao.getListaDetalhesMetas(codMeta,datasource,"",pesqValor);
	}

	
	public VoDetalhe getDetalhe(int codDetalhe) throws Exception{
		initConexaoDao();
		
		String datasource = "user-int01";
		return conexaoDao.obterDetalhe(codDetalhe,datasource);
	}

	
	public void salvar(VoDetalhe vo) throws Exception{
		initConexaoDao();

		String datasource = "user-int01";
		int retorno = conexaoDao.salvarDetalhe(vo, datasource);
		
		if(retorno != 1){throw new Exception();}
	}
	
	
	public void excluir(ArrayList<VoDetalhe> listaDetalhe) throws Exception{
		initConexaoDao();
		
		String datasource = "user-int01";
		int retorno = conexaoDao.excluirDetalhes(listaDetalhe,datasource);
		if(retorno != 1){throw new Exception();}
	}
	
	private void initConexaoDao(){if(conexaoDao == null){conexaoDao = new ConexaoDao();}}
}
