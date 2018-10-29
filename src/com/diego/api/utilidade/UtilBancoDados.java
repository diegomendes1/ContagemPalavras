package com.diego.api.utilidade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.diego.api.modelos.Ajustes;

public class UtilBancoDados {
	//Atualiza-se a url para o BD, afim de se conectar ao BD na maquina atual.
	static String caminho = Ajustes.caminho + "databaseName=DBContagem;integratedSecurity=True;";
	static Connection connection;
	static Statement st;
	
	public static void conectarBD() throws Exception{
		if(connection != null) {
			return;
		}
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			connection = DriverManager.getConnection(caminho);
			st = connection.createStatement();
			criarTabelaGeralBD();
			if(connection != null) {
				System.out.println("conectado ao Banco de Dados");
			}
	}
	
	public static ArrayList<String> buscarNoBD(String tipoArquivo, String appCaptura, Date data) throws Exception {
		conectarBD();
		
		String comando = "select * from listaResultados where ";
		
		boolean anterior = false;
		if(tipoArquivo != null) {
			comando += "(TipoArquivo like '" + tipoArquivo + "')";
			anterior = true;
		}
		if(appCaptura != null) {
			if(anterior) 
				comando+= " and ";
			anterior = true;
			comando += "(AppCaptura like '" + appCaptura + "')";
		}
		
		if(data != null) {
			if(anterior) 
				comando+= " and ";
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			comando += "(Data like '" + sdf.format(data) + "')";
		}
		
		try {
			ResultSet rs = st.executeQuery(comando);

			ArrayList<String> resultado = new ArrayList<String>();
			while(rs.next()) {
				resultado.add(rs.getString("NomeResultado"));
			}
			return resultado;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Busca no banco de dados e retorna um hashMap com certo resultado
	public static Map<String, Integer> buscarResultadoBD(String nomeResultado) throws Exception{
		conectarBD();
		Map<String, Integer> hashMap = new HashMap<String, Integer>();
		
		String comando = "select * from " + nomeResultado;
		try {
			ResultSet rs = st.executeQuery(comando);
			
			while(rs.next()) {
				hashMap.put(rs.getString("palavra"), rs.getInt("quantidade"));
			}
			
			return hashMap;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//cria uma tabela para certo resultado, e adiciona seus dados numa tabela geral
	public static void salvarResultadosBD(Map<String, Integer> hashMap, String tipoArquivo, String appCaptura) throws Exception {
		conectarBD();
		int id = contarTotalResultadosBD();
		String nomeResultado = "Resultado_" + tipoArquivo + "_" + id;
		
		Date data = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		
		String comando = "insert into listaResultados Values('" + nomeResultado + "', '"+ tipoArquivo +"', '" + appCaptura + "', '" + sdf.format(data) + "')";
		
		try {
			st.executeUpdate(comando);
			criarUmResultadoBD(hashMap, nomeResultado);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Salva um resultado no banco de dados
	private static void criarUmResultadoBD(Map<String, Integer> hashMap, String nomeResultado) {
		String comando = "create table " + nomeResultado + " (palavra varchar(64) not null, quantidade INTEGER not null)";
		
		try {
			st.executeUpdate(comando);
			
			for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
				comando = "insert into " + nomeResultado + " values('" + entry.getKey() + "', " + entry.getValue() + ")";
				st.executeUpdate(comando);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Cria uma tabela que armazena os resultados feitos anterioremente
	private static int criarTabelaGeralBD() {
		String comando = "if not exists (select * from sysobjects where name='listaResultados' and xtype='U') create table listaResultados ("
				+ "NomeResultado varchar(64) not null, "
				+ "TipoArquivo varchar(64) not null,"
				+ "AppCaptura varchar(64) not null,"
				+ "Data varchar(64) not null)";
		
		try {
			st.executeUpdate(comando);
			comando = "select count(*) from listaResultados";
			ResultSet rs = st.executeQuery(comando);

			rs.next();
			return rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//Retorna a quantidade de resultados armazenados no banco de dados
	public static int contarTotalResultadosBD() throws Exception {
		conectarBD();
		String comando = "select count(*) from listaResultados";
		try {
			ResultSet rs = st.executeQuery(comando);
			rs.next();
			return rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
