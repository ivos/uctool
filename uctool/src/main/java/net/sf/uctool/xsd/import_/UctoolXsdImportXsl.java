package net.sf.uctool.xsd.import_;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;
import net.sf.uctool.common.ExecutorBase;
import net.sf.uctool.exception.ReaderException;
import net.sf.uctool.exception.WriterException;
import net.sf.uctool.execute.Project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UctoolXsdImportXsl extends ExecutorBase {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// private final String encoding;
	private final Transformer transformer;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public UctoolXsdImportXsl(String encoding) {
		super(new Project(null, null, null, encoding, "en"));
		// this.encoding = encoding;
		transformer = getTransformer();
	}

	private Transformer getTransformer() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(
					"template/ImportXsd.xsl");
			TransformerFactory factory = new TransformerFactoryImpl();
			Transformer transformer = factory.newTransformer(new StreamSource(
					is));
			return transformer;
		} catch (TransformerConfigurationException e) {
			throw new ReaderException("Cannot load XSL template.", e);
		}
	}

	public void execute(File inputPath, File outputDir) {
		timeAll.start();

		outputDir.mkdirs();
		List<File> inputFiles = findInputFiles(inputPath, "*.xsd");
		for (File file : inputFiles) {
			processFile(outputDir, file);
		}

		timeAll.stop();
		logger.info("Executed UCTool data XSD import into {} in {}.",
				outputDir, timeAll.toString());
	}

	private void processFile(File outputDir, File file) {
		try {
			StreamSource sourceDocument = new StreamSource(file);
			String outputFilename = file.getName().split("\\.")[0] + ".xml";
			StreamResult resultDocument = new StreamResult(new File(outputDir,
					outputFilename));
			transformer.transform(sourceDocument, resultDocument);
			// resultDocument.getOutputStream().flush();
			// resultDocument.getOutputStream().close();
		} catch (Exception e) {
			throw new WriterException("Cannot process input XSD file: " + file,
					e);
		}
	}

}
