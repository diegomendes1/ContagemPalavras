package com.diego.api.modelos;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.diego.api.models.tipos.ArquivoDOC;
import com.diego.api.models.tipos.ArquivoDOCX;
import com.diego.api.models.tipos.ArquivoIMG;
import com.diego.api.models.tipos.ArquivoPDF;
import com.diego.api.models.tipos.ArquivoTXT;
import com.diego.api.utilidade.UtilBancoDados;
import com.diego.api.utilidade.UtilPlanilhas;

@Service
public class NovoProcesso {
	
	//Faz o processamento do arquivo, escolhendo o tipo correto e chamando os metodos necessarios
	public byte[] processarArquivo(MultipartFile arquivoOriginal, String appCaptura, String tipoArquivo) throws Exception{
		if(Ajustes.isTudoPermitido(appCaptura, tipoArquivo)) {
			Arquivo arquivo = encontrarTipoCorreto(tipoArquivo);
			if(arquivo == null) {
				throw new Exception("O tipo " + tipoArquivo + " esta indisponivel para processamento.");
			}else{
				arquivo.gerarTexto(arquivoOriginal);
				Map<String, Integer> hashMap = realizarContagem(arquivo.getTexto());
				UtilBancoDados.salvarResultadosBD(hashMap, tipoArquivo, appCaptura);
				return UtilPlanilhas.criarPlanilha(hashMap);
			}
		}else {
			throw new Exception("O processamento precisa se adequar aos ajustes atuais.");
		}
	}
	
	//Adicionarndo novos tipos, mais arquivos podem ser processados
	public Arquivo encontrarTipoCorreto(String tipoArquivo) {
		Arquivo resultado = null;
		switch(tipoArquivo){
		case "txt":
			resultado = new ArquivoTXT();
			break;
		case "docx":
			resultado = new ArquivoDOCX(); 
			break;
		case "doc":
			resultado = new ArquivoDOC();
			break;
		case "pdf":
			resultado = new ArquivoPDF();
			break;
		case "jpg":
			resultado = new ArquivoIMG();
			break;
		case "tif":
			resultado = new ArquivoIMG();
			break;
		default:
			resultado = null;
			break;
		}
		return resultado;
	}
	
	//Recebe a String com o texto inteiro, e realiza a contagem salvando num hashMap
	public Map<String, Integer> realizarContagem(String texto) {
		Map<String, Integer> hashMap = new HashMap<String, Integer>();
		StringTokenizer st = new StringTokenizer(texto);
		
		while(st.hasMoreTokens()) {
			String palavraAtual = st.nextToken();
			if(hashMap.containsKey(palavraAtual)) {
				hashMap.put(palavraAtual, hashMap.get(palavraAtual)+1);
			}else {
				hashMap.put(palavraAtual, 1);
			}
		}
		return hashMap;
	}
}
