package com.diego.api.models.tipos;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.diego.api.modelos.Arquivo;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class ArquivoDOCX implements Arquivo {
	private String texto;
	@Override
	public void gerarTexto(MultipartFile arquivo) {
		 XWPFDocument docx;
		try {
			docx = new XWPFDocument(arquivo.getInputStream());
			XWPFWordExtractor we = new XWPFWordExtractor(docx);
			texto = we.getText();
			we.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
