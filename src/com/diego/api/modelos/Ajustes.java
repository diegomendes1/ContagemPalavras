package com.diego.api.modelos;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

//Armazena todos os ajustes e verifica se um novo processo pode ser executado
@Service
public class Ajustes {
	//Caminho para o banco de dados
	public static String caminho = "jdbc:sqlserver://DIEGO\\SQLEXPRESS:1433;";
	
	//Armazena todos os tipos que podem ser processados
	private static ArrayList<String> tiposExistentes = new ArrayList<String>() {
		private static final long serialVersionUID = 1L; { 
			add("txt"); add("pdf");
			add("doc"); add("docx");
			add("jpg"); add("tif"); }};
	
	//Ajustes da sessao
	private static boolean isPermitidoDesktop = true;
	private static boolean isPermitidoMobile = true;
	private static boolean isPermitidoWeb = true;
	private static ArrayList<String> tiposPermitidos = tiposExistentes;
	
	public static void setTiposPermitidos(String[] tipos) {
		for(int i = 0; i < tipos.length; i++){
			if(tiposExistentes.contains(tipos[i])) {
				tiposPermitidos.add(tipos[i]);
			}
		}
		
		if(tiposPermitidos.isEmpty()) {
			tiposPermitidos.add(tiposExistentes.get(0));
		}
	}
	
	public static void retirarTodosPermitidos() {
		isPermitidoDesktop = false;
		isPermitidoMobile = false;
		isPermitidoWeb = false;
		tiposPermitidos.clear();
	}
	
	public static void ativarAppCapturas(String[] appCapturas) {
		for(int i = 0; i < appCapturas.length; i++) {
			if(appCapturas[i].equals("desktop"))
				isPermitidoDesktop = true;
			else if(appCapturas[i].equals("web"))
				isPermitidoWeb = true;
			else 
				isPermitidoMobile = true;
		}
	}
	
	//Verifica se o processamento atende aos ajustes
	public static boolean isTudoPermitido(String appCaptura, String tipoArquivo) {
		if(appCaptura.equals("desktop")) {
			if(!isPermitidoDesktop) 
				return false;
		}else if(appCaptura.equals("mobile")) {
			if(!isPermitidoMobile) 
				return false;
		}else if(appCaptura.equals("web")){
			if(!isPermitidoWeb) 
				return false;
		}else {
			return false;
		}
		
		if(!tiposPermitidos.contains(tipoArquivo)) {
			return false;
		}
		return true;
	}
}
