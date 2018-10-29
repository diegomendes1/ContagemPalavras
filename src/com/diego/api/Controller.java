package com.diego.api;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.diego.api.modelos.Ajustes;
import com.diego.api.modelos.NovoProcesso;
import com.diego.api.utilidade.UtilBancoDados;
import com.diego.api.utilidade.UtilPlanilhas;

@RestController
public class Controller {
	
	@Autowired NovoProcesso novoProcesso;
	
	//Inicia um novo processamento de um arquivo recebido, retornando um documento .xls
	@ResponseBody
	@PostMapping("/novo-processo")
	public ResponseEntity<?> novoProcesso(@RequestParam MultipartFile arquivo, 
							 					 @RequestParam String appCaptura, 
							 					 @RequestParam String tipoArquivo) {
		byte[] resultadoBytes;
		try {
			resultadoBytes = novoProcesso.processarArquivo(arquivo, appCaptura, tipoArquivo);
			ByteArrayResource resource = new ByteArrayResource(resultadoBytes);
			
			String nomeResultado = "Resultado_" + tipoArquivo + "_" + UtilBancoDados.contarTotalResultadosBD();
			HttpHeaders header = new HttpHeaders();
			header.add("content-disposition", "attachment; filename=\"" + nomeResultado + ".xls\"");
			header.add("content-type", "application/vnd.ms-excel");
			
			return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//Recebe novos ajustes e armazena na classe especifica
	@PostMapping("/ajustes")
	public ResponseEntity<String> ajustes(@RequestParam(defaultValue = "web") String[] appCapturas,
						@RequestParam(defaultValue = "txt") String[] tipos) {
		Ajustes.ativarAppCapturas(appCapturas);
		Ajustes.setTiposPermitidos(tipos);
		return new ResponseEntity<String>("Ajustes atualizados.", HttpStatus.OK);
	}
	
	//Recebe filtros, inicia uma busca pelo banco de dados, e retorna uma lista com os resultados
	@GetMapping("/busca")
	public Object busca(@RequestHeader(required = false) String tipoArquivo,
					  @RequestHeader(required = false) String appCaptura,
					  @RequestHeader(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date data) {
		try {
			return UtilBancoDados.buscarNoBD(tipoArquivo, appCaptura, data);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Rece um nome de arquivo qualquer, e retorna um documento .xls correspondente
	@ResponseBody
	@GetMapping("/arquivo-processado/{nomeArquivo}")
	public ResponseEntity<?> resultados(@PathVariable("nomeArquivo") String nomeArquivo) {
		Map<String, Integer> hashMap;
		try {
			hashMap = UtilBancoDados.buscarResultadoBD(nomeArquivo);
			byte[] resultadoBytes = UtilPlanilhas.criarPlanilha(hashMap);
			ByteArrayResource resource = new ByteArrayResource(resultadoBytes);
			
			HttpHeaders header = new HttpHeaders();
			header.add("content-disposition", "attachment; filename=\"" + nomeArquivo + ".xls\"");
			header.add("content-type", "application/vnd.ms-excel");
			return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
