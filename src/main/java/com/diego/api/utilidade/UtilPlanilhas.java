package com.diego.api.utilidade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class UtilPlanilhas {
	
	//Recebe os resultados da contagem e retorna um arquivo .xls convertido em byte[]
	public static byte[] criarPlanilha(Map<String, Integer> hashMap) {
		try {
			File file = new File("Resultados.xls");
			FileOutputStream out = new FileOutputStream(file);
			Workbook wb = new HSSFWorkbook();
			
			Sheet s = wb.createSheet();
			Row r = s.createRow(0);
			
			Cell cellPalavra = r.createCell(0);
			cellPalavra.setCellValue("Palavra");
			
			Cell cellOcorrencias = r.createCell(1);
			cellOcorrencias.setCellValue("Nº Ocorrências");
			
			int i = 1;
			for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
				r = s.createRow(i);
				cellPalavra = r.createCell(0);
				cellPalavra.setCellValue(entry.getKey());
				
				cellOcorrencias = r.createCell(1);
				cellOcorrencias.setCellValue(entry.getValue());
				i++;
			}
			
			wb.write(out);
			out.close();
			wb.close();
			byte[] resultadoBytes = Files.readAllBytes(file.toPath());
			Files.delete(file.toPath());
			
			return resultadoBytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
