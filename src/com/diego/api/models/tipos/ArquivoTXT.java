package com.diego.api.models.tipos;


import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.diego.api.modelos.Arquivo;

public class ArquivoTXT implements Arquivo{
	private String texto;

	@Override
	public void gerarTexto(MultipartFile arquivo) {
		byte[] encoded;
		try {
			encoded = arquivo.getBytes();
			texto = new String(encoded);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getTexto() {
		return texto;
	}

}
