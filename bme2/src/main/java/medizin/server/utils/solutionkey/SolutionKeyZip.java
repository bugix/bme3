package medizin.server.utils.solutionkey;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import medizin.server.domain.Person;
import medizin.server.utils.docx.PaperUtils;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

public class SolutionKeyZip {
	
	private static Logger log = Logger.getLogger(SolutionKeyZip.class);
	
	private final ByteArrayOutputStream os;
	private String zipfileName = "solutionKey.zip";
	private static final String EXTENSION_ZIP = ".zip";

	private final BaselGenerator baselGenerator;
	private final FragentypGenerator fragentypGenerator;
	private final SubscoresGenerator subscoresGenerator;
	
	public SolutionKeyZip(ByteArrayOutputStream os, Integer assessmentId, Person loggedPerson) {
		this.os = os;
		zipfileName = "solution_" + PaperUtils.getDocumentName(assessmentId.longValue(), null,loggedPerson.getShidId(), EXTENSION_ZIP);
		log.info("fileName : " + zipfileName);
		baselGenerator = new BaselGenerator(assessmentId.longValue());
		fragentypGenerator = new FragentypGenerator(assessmentId.longValue());
		subscoresGenerator = new SubscoresGenerator(assessmentId.longValue());
	}
	
	public void generate() {
		fragentypGenerator.generate();
		subscoresGenerator.generate();
		baselGenerator.generate();
		createZipFile();
	}
	
	private void createZipFile() {

		try {
			ZipOutputStream zipOut = new ZipOutputStream(os);
			
			zipOut.putNextEntry(new ZipEntry(fragentypGenerator.getFileName()));
			zipOut.write(fragentypGenerator.getBytes());
			zipOut.closeEntry();
			
			zipOut.putNextEntry(new ZipEntry(subscoresGenerator.getFileName()));
			zipOut.write(subscoresGenerator.getBytes());
			zipOut.closeEntry();
			
			zipOut.putNextEntry(new ZipEntry(baselGenerator.getFileName()));
			zipOut.write(baselGenerator.getBytes());
			zipOut.closeEntry();
			
			zipOut.close();

			log.info("Done...");

		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	public String getFileName() {
		return zipfileName;
	}
}
