package com.diego.api.models.tipos;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.diego.api.modelos.Arquivo;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ArquivoDOC implements Arquivo {
	private String texto;

	@Override
	public void gerarTexto(MultipartFile arquivo) {
		 HWPFDocument docx;
		try {
			docx = new HWPFDocument(arquivo.getInputStream());
			WordExtractor we = new WordExtractor(docx);
			texto = we.getText();
			we.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTexto() {
		return texto;
	}

}