/**
 * Projeto   : Metas
 * Pacote    : br.jus.trt10.BancoDao
 * Classe    : ConexaoDao.java
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
package br.jus.trt10.BancoDao;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.jus.trt10.Vo.VoDetalhe;
import br.jus.trt10.Vo.VoMeta;

public class ConexaoDao {
	
	private String sqlEstItem    = "select id,descricao,periodicidade from www_user.est_item order by id";
	private String sqlDetEstItem = "select id,id_item,cod_item, desc_item,  upper(conexao) , consulta_sql from www_user.est_detalhe_item where id_item = ? order by id"; 
	private String sqlMeta       = "select id,descricao,periodicidade from www_user.est_item where id = ? order by id";

	
	/**====================================================================================================*/
	/**                      Implementa os métodos das Metas                                               */
	/**====================================================================================================*/
	
	private int obterSeqMeta(Connection con) throws Exception{
		int retorno = -1;
		String sql = "select www_user.seq_est_item.nextval from dual";

		try {
			Statement stm = con.createStatement();
			ResultSet rs  = stm.executeQuery(sql);
			if(rs.next()){
				retorno = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception("Não foi possível obter a sequência do Item.");
		}
		
		return retorno;
	}

	private boolean haDetalhes(Connection con, int codItem) throws Exception{
		boolean retorno = false;
		String sql = "select count(*) from www_user.est_detalhe_item where id_item = ?";

		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, codItem);
			
			ResultSet rs  = pst.executeQuery();
			if(rs.next()){if(rs.getInt(1) > 0){retorno = true;}}
		} catch (Exception e) {
			throw new Exception("Não foi possível obter a sequência do Item.");
		}
		
		return retorno;
	}
	
	
	public int excluirMetas(VoMeta vo, String dataSourceName) throws Exception{
		PreparedStatement pstmt;
		Connection        conexao = null;
		int               retorno = -1;
		
		try {
			InitialContext initCtx = new InitialContext();
			DataSource dataSource = (DataSource) initCtx.lookup("java:/jdbc/".concat(dataSourceName));
			conexao = dataSource.getConnection();
			conexao.setAutoCommit(false);
			
			//Verifico se ainda existem detalhes para a meta.
			if(haDetalhes(conexao, vo.getCodigo())){
				return -9; 
			}
			
			pstmt = conexao.prepareStatement(sqlDeleteMeta);
			pstmt.setInt   (1, vo.getCodigo());
			retorno = pstmt.executeUpdate();
			
			conexao.commit();

        }catch (NamingException ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
		} catch (Exception ex) {
			Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
			throw new Exception(ex);
		}finally{
			if (conexao != null) {
				if (!conexao.isClosed()) {
					conexao.close();
				}
				conexao = null;
			}
		}

		return retorno;
	}
	
	
	private String sqlInsertMeta = "insert into www_user.est_item(id,descricao,periodicidade) values(?,?,?)";
	private String sqlUpdateMeta = "update www_user.est_item set descricao = ?, periodicidade = ? where id = ?";
	private String sqlDeleteMeta = "delete from www_user.est_item where id = ?";

	
	public int salvarMeta(VoMeta vo, String dataSourceName) throws Exception{
        PreparedStatement pstmt;
        Connection        conexao = null;
        int               retorno = -1;
		
        try {
            InitialContext initCtx = new InitialContext();
            DataSource dataSource = (DataSource) initCtx.lookup("java:/jdbc/".concat(dataSourceName));
            conexao = dataSource.getConnection();
            conexao.setAutoCommit(false);
            
    		//Insert
    		if(vo.getCodigo() == 0){
    			pstmt = conexao.prepareCall(sqlInsertMeta);
    			
    			pstmt.setInt   (1, obterSeqMeta(conexao));
    			pstmt.setString(2, vo.getDescricao    ());
    			pstmt.setString(3, vo.getPeriodicidade());
    			
    			retorno = pstmt.executeUpdate();
    			
    		}else{
    			//Update

    			pstmt = conexao.prepareStatement(sqlUpdateMeta);
    			pstmt.setString(1, vo.getDescricao    ());
    			pstmt.setString(2, vo.getPeriodicidade());
    			pstmt.setInt   (3, vo.getCodigo       ());
    			retorno = pstmt.executeUpdate();
    		}
            
    		conexao.commit();

        }catch (NamingException ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        } catch (Exception ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }finally{
        	if (conexao != null) {
        		if (!conexao.isClosed()) {
        			conexao.close();
        		}
        		conexao = null;
        	}
        }
        
        return retorno;

	}
	
	public VoMeta obterMeta(int codigo, String dataSourceName) throws Exception{
        PreparedStatement  pstmt;
        Connection         conexao = null;
        VoMeta vo = new VoMeta();
        try {
            InitialContext initCtx = new InitialContext();
            DataSource dataSource = (DataSource) initCtx.lookup("java:/jdbc/".concat(dataSourceName));
            conexao = dataSource.getConnection();
            pstmt = conexao.prepareCall(sqlMeta);
            pstmt.setInt(1, codigo);

            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()){
            	vo.setCodigo       (rs.getInt   (1));
            	vo.setDescricao    (rs.getString(2));
            	vo.setPeriodicidade(rs.getString(3));
            }
            
        }catch (NamingException ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        } catch (Exception ex) {
        	Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }finally{
        	if (conexao != null) {
        		if (!conexao.isClosed()) {
        			conexao.close();
        		}
        		conexao = null;
        	}
        }
        return vo;
	}
	
	public ArrayList<VoMeta> obterListaMetas(String dataSourceName) throws Exception{
        PreparedStatement pstmt;
        Connection        conexao = null;
        ArrayList<VoMeta>  result = new ArrayList<VoMeta>();
        try {
            InitialContext initCtx = new InitialContext();
            DataSource dataSource = (DataSource) initCtx.lookup("java:/jdbc/".concat(dataSourceName));
            conexao = dataSource.getConnection();
            pstmt = conexao.prepareCall(sqlEstItem);

            ResultSet rs = pstmt.executeQuery();
            
            VoMeta vo = new VoMeta();
        	vo.setCodigo       (0);
        	vo.setDescricao    ("...");
        	vo.setPeriodicidade("");
        	result.add(vo);
            
            while(rs.next()){
            	vo = new VoMeta();
            	vo.setCodigo       (rs.getInt   (1));
            	vo.setDescricao    (rs.getString(2));
            	vo.setPeriodicidade(rs.getString(3));
            	result.add(vo);
            }

        }catch (NamingException ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        } catch (Exception ex) {
        	Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }finally{
        	if (conexao != null) {
        		if (!conexao.isClosed()) {
        			conexao.close();
        		}
        		conexao = null;
        	}
        }

        return result;
	}
	
	

	
	
	
	
	
	/**====================================================================================================*/
	/**                      Implementa os métodos dos Detalhe da Metas                                    */
	/**====================================================================================================

	/**
	 * @throws Exception
	 */
	private String obterValorDetalhe(String pDataSource, String sqlValor, String paramBusca) throws Exception{
		Statement         stm;
		Connection        conexao = null;
		String            retorno = "";
		
		try {
			InitialContext initCtx = new InitialContext();
			DataSource dataSource = (DataSource) initCtx.lookup("java:/jdbc/".concat(pDataSource));
			conexao = dataSource.getConnection();
			conexao.setAutoCommit(false);
			
			stm      = conexao.createStatement();
			sqlValor = sqlValor.replaceAll(":P_1", "'".concat(paramBusca).concat("'") );
			
			ResultSet rs = stm.executeQuery(sqlValor);
			if(rs.next()){retorno = String.valueOf(rs.getInt(1));}

        }catch (NamingException ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
		} catch (Exception ex) {
			Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
			throw new Exception(ex);
		}finally{
			if (conexao != null) {
				if (!conexao.isClosed()) {
					conexao.close();
				}
				conexao = null;
			}
		}

		return retorno;
	}
	
	public ArrayList<VoDetalhe> getListaDetalhesMetas(int codItem, String dataSourceName, String paramBusca, boolean pesqValor) throws Exception{
        PreparedStatement pstmt;
        Connection        conexao = null;
        ArrayList<VoDetalhe>  result = new ArrayList<VoDetalhe>();
        try {
            InitialContext initCtx = new InitialContext();
            DataSource dataSource = (DataSource) initCtx.lookup("java:/jdbc/".concat(dataSourceName));
            conexao = dataSource.getConnection();
            pstmt = conexao.prepareCall(sqlDetEstItem);
            pstmt.setInt(1,codItem );

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
            	VoDetalhe vo = new VoDetalhe();
            	vo.setIdDetalhe    (rs.getInt(1));
            	vo.setIdItem       (rs.getInt(2)); 
            	vo.setCodigo       (rs.getString(3));
            	vo.setDescricao    (rs.getString(4));
            	vo.setConexao      (rs.getString(5).toUpperCase());
            	
            	//Outra forma de usar o campo CLOB
            	//Clob aClob = conexao.createClob();
            	//aClob = rs.getClob(4);
            	
            	Clob clob = rs.getClob(6);
            	vo.setConsultaSql(clob.getSubString(1, (int)clob.length()));
            	
            	if(pesqValor){
            		char[] seq = vo.getConsultaSql().toCharArray();
            		StringBuilder str = new StringBuilder();
            		
            		for(int i=0; i<vo.getConsultaSql().length();i++){
            			if((int)seq[i] != 10){ 
            				str.append(seq[i]);
            			}
            		}

            		String valor = obterValorDetalhe(vo.getConexao().toLowerCase(), str.toString() ,paramBusca);
            		vo.setValor(valor);
            	}
            	
            	result.add(vo);
            }

        }catch (NamingException ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        } catch (Exception ex) {
        	Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }finally{
        	if (conexao != null) {
        		if (!conexao.isClosed()) {
        			conexao.close();
        		}
        		conexao = null;
        	}
        }

        return result;
	}	
	
	public int excluirDetalhes(ArrayList<VoDetalhe> lista, String dataSourceName) throws Exception{
		PreparedStatement pstmt;
		Connection        conexao = null;
		int               retorno = -1;
		
		try {
			InitialContext initCtx = new InitialContext();
			DataSource dataSource = (DataSource) initCtx.lookup("java:/jdbc/".concat(dataSourceName));
			conexao = dataSource.getConnection();
			conexao.setAutoCommit(false);
			
			for (VoDetalhe vo : lista) {
				pstmt = conexao.prepareStatement(sqlDeleteDetalhe);
				pstmt.setInt   (1, vo.getIdItem     ());
				pstmt.setInt   (2, vo.getIdDetalhe  ());
				retorno = pstmt.executeUpdate();
				
				if(retorno != 1){break;}
			}
			
			conexao.commit();

        }catch (NamingException ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
		} catch (Exception ex) {
			Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
			throw new Exception(ex);
		}finally{
			if (conexao != null) {
				if (!conexao.isClosed()) {
					conexao.close();
				}
				conexao = null;
			}
		}

		return retorno;
	}
	
	private int obterSeqDetalhe(Connection con) throws Exception{
		int retorno = -1;
		String sql = "select www_user.seq_est_detalhe_item.nextval from dual";

		try {
			Statement stm = con.createStatement();
			ResultSet rs  = stm.executeQuery(sql);
			if(rs.next()){
				retorno = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception("Não foi possível obter a sequência do Detalhe.");
		}
		
		return retorno;
	}
	
	
	private String sqlInsertDetalhe = "insert into www_user.est_detalhe_item(id,id_item,cod_item,desc_item,conexao,consulta_sql) values(?,?,?,?,upper(?),?)";
	private String sqlUpdateDetalhe = "update www_user.est_detalhe_item set cod_item = ?, desc_item = ?, conexao = upper(?),consulta_sql = ? where id_item = ? and id = ?";
	private String sqlDeleteDetalhe = "delete from www_user.est_detalhe_item where id_item = ? and id = ?";
	
	public int salvarDetalhe(VoDetalhe vo,String dataSourceName) throws Exception{
        PreparedStatement pstmt;
        Connection        conexao = null;
        int               retorno = -1;
		
        try {
            InitialContext initCtx = new InitialContext();
            DataSource dataSource = (DataSource) initCtx.lookup("java:/jdbc/".concat(dataSourceName));
            conexao = dataSource.getConnection();
            conexao.setAutoCommit(false);
            
    		//Insert
    		if(vo.getIdDetalhe() == 0){
    			pstmt = conexao.prepareCall(sqlInsertDetalhe);
    			
    			pstmt.setInt   (1, obterSeqDetalhe(conexao));
    			pstmt.setInt   (2, vo.getIdItem   ());
    			pstmt.setString(3, vo.getCodigo   ());
    			pstmt.setString(4, vo.getDescricao());
    			pstmt.setString(5, vo.getConexao  ());
    			
    			Clob clobIns = conexao.createClob();
    			clobIns.setString(1, vo.getConsultaSql());
    			pstmt.setClob  (6, clobIns);
    			
    			retorno = pstmt.executeUpdate();
    			
    		}else{
    			//Update

    			pstmt = conexao.prepareStatement(sqlUpdateDetalhe);
    			pstmt.setString(1, vo.getCodigo     ());
    			pstmt.setString(2, vo.getDescricao  ());
    			pstmt.setString(3, vo.getConexao    ());
    			pstmt.setString(4, vo.getConsultaSql());
    			pstmt.setInt   (5, vo.getIdItem     ());
    			pstmt.setInt   (6, vo.getIdDetalhe  ());
    			retorno = pstmt.executeUpdate();
    		}
            
    		conexao.commit();

        }catch (NamingException ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        } catch (Exception ex) {
            Logger.getLogger(ConexaoDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }finally{
        	if (conexao != null) {
        		if (!conexao.isClosed()) {
        			conexao.close();
        		}
        		conexao = null;
        	}
        }
        
        return retorno;
	}
	
	
	public VoDetalhe obterDetalhe(int codigo, String dataSourceName) throws Exception{	
		VoDetalhe retorno = new VoDetalhe();
		
		return retorno;
	}
	
	
	
	
}
