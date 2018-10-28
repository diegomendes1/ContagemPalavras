package com.diego.api.modelos;

import org.springframework.web.multipart.MultipartFile;

//As classes que implementam esta interface podem ser usadas como um tipo de arquivo
public interface Arquivo {
	public void gerarTexto(MultipartFile arquivo);
	public String getTexto();
}
