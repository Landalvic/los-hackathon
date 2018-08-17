package fr.insee.bidbo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.itextpdf.text.pdf.PdfReader;

public abstract class FichierUtils {

    private static Logger logger = LoggerFactory.getLogger(FichierUtils.class);
    private static final Pattern fileNamePattern = Pattern.compile("[^\\\\/:\\*\\?\"<>\\|]+");
    private static final String XML = "xml";

    public static boolean isFichierZip(File file) {
	return isFichierZip(file.getName());
    }

    public static boolean isFichierZip(String fileName) {
	return StringUtils.equalsIgnoreCase("zip", FilenameUtils.getExtension(fileName));
    }

    public static boolean isFichierXml(File file) {
	return isFichierXml(file.getName());
    }

    public static boolean isFichierXml(String fileName) {
	return StringUtils.equalsIgnoreCase("xml", FilenameUtils.getExtension(fileName));
    }

    public static boolean isFichierCsv(File file) {
	return isFichierCsv(file.getName());
    }

    public static boolean isFichierCsv(String fileName) {
	return StringUtils.equalsIgnoreCase("csv", FilenameUtils.getExtension(fileName));
    }

    public static boolean isFichierTxt(File file) {
	return isFichierTxt(file.getName());
    }

    public static boolean isFichierTxt(String fileName) {
	return StringUtils.equalsIgnoreCase("txt", FilenameUtils.getExtension(fileName));
    }

    public static boolean isFichierPDF(File file) {
	return StringUtils.equalsIgnoreCase("pdf", FilenameUtils.getExtension(file.getName()));
    }

    public static File fichierXml(File dossier) {
	Iterable<File> files = Files.fileTreeTraverser().children(dossier);
	for (File file : files) {
	    if (isFichierXml(file)) {
		return file;
	    }
	}
	return null;
    }

    public static File getFichierPdf(File dossier) {
	Iterable<File> files = Files.fileTreeTraverser().children(dossier);
	for (File file : files) {
	    if (isFichierPDF(file)) {
		return file;
	    }
	}
	return null;
    }

    public static boolean isFichierDonnees(File file) {
	return StringUtils.equalsIgnoreCase("ods", FilenameUtils.getExtension(file.getName()))
		|| StringUtils.equalsIgnoreCase("xls", FilenameUtils.getExtension(file.getName()));
    }

    public static File fichierDonnees(File dossier) {
	Iterable<File> files = Files.fileTreeTraverser().children(dossier);
	for (File file : files) {
	    if (isFichierDonnees(file)) {
		return file;
	    }
	}
	return null;
    }

    public static File fichierTxt(File dossier) {
	Iterable<File> files = Files.fileTreeTraverser().children(dossier);
	for (File file : files) {
	    if (isFichierTxt(file)) {
		return file;
	    }
	}
	return null;
    }

    public static String tailleOctet(long byteCount) {
	return StringUtils.replaceChars(FileUtils.byteCountToDisplaySize(byteCount), 'B', 'o').replace("bytes",
		"octets");
    }

    public static boolean isValidFileName(String string) {
	return fileNamePattern.matcher(string).matches();
    }

    public static File fichierTelechargeable(File dossier, File file) {
	Iterable<File> files = Files.fileTreeTraverser().children(dossier);
	for (File fichier : files) {
	    if (isSameName(file, fichier)) {
		return fichier;
	    }
	}
	return null;
    }

    public static File fichier(File dossier, String file) {
	Iterable<File> files = Files.fileTreeTraverser().children(dossier);
	for (File fichier : files) {
	    if (StringUtils.equalsIgnoreCase(FilenameUtils.getName(file), FilenameUtils.getName(fichier.getName()))) {
		return fichier;
	    }
	}
	return null;
    }

    private static boolean isSameName(File file1, File file2) {
	String name1 = FilenameUtils.getName(file1.getName());
	String name2 = FilenameUtils.getName(file2.getName());
	return StringUtils.equalsIgnoreCase(name1, name2);
    }

    /**
     * Crée un zip de nom nomZip dans le dossier myDir et qui contient les
     * fichiersAZipper (qui sont dans le répertoire myDir).
     */
    public static File creerZipFromFiles(String nomZip, File myDir, File[] fichiersAZipper)
	    throws FileNotFoundException, IOException {

	File zipFile = new File(myDir.getPath() + "/" + nomZip);
	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

	for (File file : fichiersAZipper) {
	    File f = new File(myDir.getPath() + "/" + file.getName());
	    FichierUtils.addToZipFile(f, file.getName(), out);
	}
	out.flush();
	out.close();
	return zipFile;
    }

    /**
     * Crée un zip de nom nomZip dans le dossier dirZip et qui contient la map
     * fichiersAZipper : fichier et nom du fichier à afficher dans le zip .
     */
    public static File creerZipFromMapFiles(String nomZip, File dirZip, Map<File, String> fichiersAZipper)
	    throws FileNotFoundException, IOException {

	File zipFile = new File(dirZip.getPath() + "/" + nomZip);
	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

	for (Map.Entry<File, String> file : fichiersAZipper.entrySet()) {
	    FichierUtils.addToZipFile(file.getKey(), file.getValue(), out);
	}
	out.flush();
	out.close();
	return zipFile;
    }

    /**
     * Ajoute un fichier dont le chemin est path + fileName dans zos. Ne pas
     * oublier de flush() et de close() le zos à la fin des traitements.
     */
    private static void addToZipFile(File file, String name, ZipOutputStream zos)
	    throws FileNotFoundException, IOException {

	logger.debug("Ecriture de '" + file.getName() + "' dans un fichier zip.");

	FileInputStream fis = new FileInputStream(file);
	ZipEntry zipEntry = new ZipEntry(name);
	zos.putNextEntry(zipEntry);

	byte[] bytes = new byte[1024];
	int length;
	while ((length = fis.read(bytes)) >= 0) {
	    zos.write(bytes, 0, length);
	}

	zos.closeEntry();
	fis.close();
    }

    public static void moveDirectoryContent(File srcDir, File destDir, boolean createDestDir) throws IOException {
	Iterable<File> files = Files.fileTreeTraverser().children(srcDir);
	for (File file : files) {
	    try {
		FileUtils.moveFileToDirectory(file, destDir, createDestDir);
	    } catch (FileExistsException e) {
		logger.warn(e.getMessage(), e);
	    }
	}
    }

    public static void copyDirectoryContent(File srcDir, File destDir) throws IOException {
	Iterable<File> files = Files.fileTreeTraverser().children(srcDir);
	if (!destDir.exists()) {
	    destDir.mkdir();
	}
	for (File file : files) {
	    FileUtils.copyFileToDirectory(file, destDir);
	}
    }

    public static void emptyDirectory(File directory) throws IOException {
	for (File file : directory.listFiles()) {
	    if (file.isFile()) {
		FileUtils.deleteQuietly(file);
	    } else {
		FileUtils.deleteDirectory(file);
	    }
	}
    }

    public static int nbPagesPdf(File pdf) throws IOException {
	int nbPages = 0;
	if (pdf != null) {
	    PdfReader reader = new PdfReader(pdf.getAbsolutePath());
	    nbPages = reader.getNumberOfPages();
	    reader.close();
	}
	return nbPages;
    }

    public static String encodage(File file) {
	byte[] buf = new byte[4096];
	UniversalDetector detector = new UniversalDetector(null);
	try (FileInputStream fis = new FileInputStream(file);) {
	    int nread;
	    while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
		detector.handleData(buf, 0, nread);
	    }
	    detector.dataEnd();
	    String encoding = detector.getDetectedCharset();
	    if (encoding != null) {
		return encoding;
	    }
	    logger.debug("Encodage non trouvé pour le fichier " + file.getName());

	} catch (IOException e) {
	    logger.warn("Erreur sur l'encodage du fichier " + file.getName() + " : " + e.getMessage(), e);
	} finally {
	    detector.reset();
	}
	return null;
    }

    public static boolean estUtf8(File file) {
	String encodage = encodage(file);
	if (encodage == null || StringUtils.equalsIgnoreCase("UTF-8", encodage)) {
	    return true;
	}
	return false;
    }

    /**
     * Cette méthode permet de décompresser le fichier zip dans le répertoire où
     * il se trouve. Le fichier zip initial n'est pas supprimé. Différentes
     * exceptions peuvent être levées, si le fichier n'existe pas, si les droits
     * ne sont pas suffisant etc.
     *
     * @return Le dossier courant dans lequel l'archive a été décompressée.
     */
    public static File extraireZip(File zip, String fileName) throws Exception {
	if (zip == null) {
	    return null;
	}
	try (ZipFile zipFile = new ZipFile(zip, Charset.forName("ISO-8859-1"))) {
	    Enumeration<? extends ZipEntry> entries = zipFile.entries();
	    File extractionFolder = new File(zip.getParentFile(), fileName);
	    while (entries.hasMoreElements()) {
		ZipEntry entry = entries.nextElement();
		File entryDestination = new File(extractionFolder, entry.getName());
		entryDestination.getParentFile().mkdirs();
		if (entry.isDirectory()) {
		    entryDestination.mkdirs();
		} else {
		    InputStream in = zipFile.getInputStream(entry);
		    OutputStream out = new FileOutputStream(entryDestination);
		    IOUtils.copy(in, out);
		    IOUtils.closeQuietly(in);
		    IOUtils.closeQuietly(out);
		}
	    }
	    return extractionFolder;
	} catch (IOException e) {
	    logger.error(e.getMessage(), e);
	    throw new Exception(
		    "Erreur lors de la décompression du fichier [" + zip.getName() + "] : " + e.getMessage(), e);
	}
    }

    public static File extraireZip(File zip) throws Exception {
	return extraireZip(zip, Long.toString(System.currentTimeMillis()));
    }

    /**
     * Cette méthode crée un fichier zip contenant tous les fichiers qui sont
     * dans les figures des blocs ainsi qu'éventuellement le fichier de données
     * <code>donnees-produit</code>.
     */
    public static void creerFichierDonneesZip(File dossierProduit, List<File> listeFichiers) throws Exception {
	try {
	    FileOutputStream fos = new FileOutputStream(dossierProduit);
	    creerFichierDonneesZipCommun(fos, listeFichiers);
	} catch (FileNotFoundException e) {
	    logger.error(e.getMessage(), e);
	    throw new Exception(
		    "Erreur lors de la création du fichier zip contenant les données du produit : " + e.getMessage(),
		    e);
	}
    }

    /**
     * Cette méthode crée un fichier zip contenant tous les fichiers qui sont
     * dans les figures des blocs ainsi qu'éventuellement le fichier de données
     * <code>donnees-produit</code>.
     */
    public static InputStream creerFichierDonneesZipDynamique(List<File> listeFichiers) throws Exception {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	creerFichierDonneesZipCommun(out, listeFichiers);
	return new ByteArrayInputStream(out.toByteArray());
    }

    public static void creerFichierDonneesZipCommun(OutputStream out, List<File> listeFichiers) throws Exception {
	try {
	    ZipOutputStream zos = new ZipOutputStream(out);
	    byte[] buffer = new byte[128];
	    for (File fichier : listeFichiers) {
		if (!fichier.isDirectory()) {
		    ZipEntry entry = new ZipEntry(fichier.getName());
		    FileInputStream fis = new FileInputStream(fichier);
		    zos.putNextEntry(entry);
		    if (StringUtils.equalsIgnoreCase(XML, FilenameUtils.getExtension(entry.getName()))) {
			// On écrit le bom de l'UTF-8 pour éviter les problèmes
			// lors du rechargement du zip
			byte[] bom = { (byte) 239, (byte) 187, (byte) 191 };
			zos.write(bom);
		    }
		    int read = 0;
		    while ((read = fis.read(buffer)) != -1) {
			zos.write(buffer, 0, read);
		    }
		    zos.closeEntry();
		    fis.close();
		}
	    }
	    zos.close();
	    out.close();
	} catch (IOException e) {
	    logger.error(e.getMessage(), e);
	    throw new Exception(
		    "Erreur lors de la création du fichier zip contenant les données du produit : " + e.getMessage(),
		    e);
	}
    }
}
