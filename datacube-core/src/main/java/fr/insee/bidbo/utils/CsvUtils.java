package fr.insee.bidbo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

public abstract class CsvUtils {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtils.class);

    public static <T> Iterator<T> readToBean(BufferedReader bufferedReader, char separator, String[] colonnes,
	    Class<T> clazz) {
	ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<>();
	strategy.setType(clazz);
	strategy.setColumnMapping(colonnes);
	CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(bufferedReader)
		.withSeparator('#')
		.withMappingStrategy(strategy)
		.withIgnoreQuotations(true)
		.withIgnoreLeadingWhiteSpace(true)
		.withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
		.build();

	return csvToBean.iterator();
    }

    public static Iterator<String[]> read(BufferedReader bufferedReader, char separator) {
	return readCommun(bufferedReader, separator).iterator();
    }

    public static List<String[]> readAll(BufferedReader bufferedReader, char separator) throws IOException {
	return readCommun(bufferedReader, separator).readAll();
    }

    public static List<String[]> readAll(File fichier, char separator) {
	try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
	    return readAll(reader, separator);
	} catch (IOException e) {
	    logger.error("Impossible de lire le fichier", e);
	    return null;
	}
    }

    public static String[] firstLine(File fichier) {
	try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
	    Iterator<String[]> iterateur = CsvUtils.read(reader, '#');
	    if (iterateur.hasNext()) {
		return iterateur.next();
	    } else {
		return null;
	    }
	} catch (Exception e) {
	    logger.error("Problème lors de la lecture du fichier", e);
	    return null;
	}
    }

    private static CSVReader readCommun(BufferedReader bufferedReader, char separator) {
	CSVParser parser = new CSVParserBuilder()
		.withSeparator(separator)
		.withIgnoreQuotations(true)
		.withIgnoreLeadingWhiteSpace(true)
		.build();
	return new CSVReaderBuilder(bufferedReader).withCSVParser(parser).build();
    }

    public static Iterator<LigneFichier> readToMap(BufferedReader bufferedReader, char separator) {
	HeaderColumnNameMappingStrategy<LigneFichier> strategy = new HeaderColumnNameMappingStrategy<>();
	strategy.setType(LigneFichier.class);
	CsvToBean<LigneFichier> csvToBean = new CsvToBeanBuilder<LigneFichier>(bufferedReader)
		.withSeparator('#')
		.withMappingStrategy(strategy)
		.withIgnoreQuotations(true)
		.withIgnoreLeadingWhiteSpace(true)
		.withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
		.build();

	return csvToBean.iterator();
    }

    public static class LigneFichier {

	@CsvBindAndJoinByName(column = ".*", elementType = String.class)
	private MultiValuedMap<String, String> map;

	public MultiValuedMap<String, String> getMap() {
	    return map;
	}

	public void setMap(MultiValuedMap<String, String> map) {
	    this.map = map;
	}

	public String getMapFirst(String key) {
	    return map.get(key).iterator().next();
	}

    }
}
