package com.diego.api.models.tipos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

import com.diego.api.modelos.Arquivo;

import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

public class ArquivoIMG implements Arquivo{
	private String texto;

	@Override
	public void gerarTexto(MultipartFile arquivo) {
		Tesseract1 tess = new Tesseract1();
		tess.setLanguage("por");
		tess.setDatapath(Paths.get("").toAbsolutePath().toString());
		
		File convFile = new File(arquivo.getOriginalFilename());
	    try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
		    fos.write(arquivo.getBytes());
		    fos.close();
		    String text = tess.doOCR(convFile);
		    texto = text;
		    convFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(TesseractException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTexto() {
		return texto;
	}

}
