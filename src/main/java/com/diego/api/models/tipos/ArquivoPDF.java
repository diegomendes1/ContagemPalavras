package com.diego.api.models.tipos;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import com.diego.api.modelos.Arquivo;

public class ArquivoPDF implements Arquivo{
	private String texto;

	@Override
	public void gerarTexto(MultipartFile arquivo) {
		try {
			PDDocument document = PDDocument.load(arquivo.getInputStream());
			if (!document.isEncrypted()) {
			    PDFTextStripper stripper = new PDFTextStripper();
			    String text = stripper.getText(document);
			    texto = text;
			}
			document.close();
		} catch (InvalidPasswordException e) {
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
