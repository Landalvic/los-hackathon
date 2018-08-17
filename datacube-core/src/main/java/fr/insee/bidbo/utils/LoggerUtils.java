package fr.insee.bidbo.utils;

public abstract class LoggerUtils {

	public static String getSeparationsInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("line.separator"));
		sb.append("----------------------------------------------------------------------------------------------------------------------------------");
		return sb.toString();
	}

}
