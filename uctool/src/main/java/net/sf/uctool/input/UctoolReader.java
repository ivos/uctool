package net.sf.uctool.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.sf.uctool.exception.ReaderException;
import net.sf.uctool.xsd.Uct;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UctoolReader {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final StopWatch time;

	private JAXBContext jc;

	public UctoolReader() {
		time = new StopWatch();
	}

	public UctoolReader init() {
		logger.debug("Initializing JAXB.");
		time.start();
		try {
			jc = JAXBContext.newInstance(Uct.class);
		} catch (JAXBException e) {
			throw new ReaderException("Error setting up JAXB context.", e);
		}
		logger.debug("Initialized JAXB @ {}.", time.toString());
		time.stop();
		return this;
	}

	public Uct read(File file) {
		try {
			Reader reader = new FileReader(file);
			return read(reader, file.getName());
		} catch (FileNotFoundException e) {
			throw new ReaderException("Input file [" + file.getName()
					+ "] does not exist.", e);
		}
	}

	public Uct read(Reader reader, String name) {
		Unmarshaller unmarshaller;
		if (null == jc) {
			throw new ReaderException(
					"JAXB context is not set up. Did you call init()?");
		}
		try {
			unmarshaller = jc.createUnmarshaller();
		} catch (JAXBException e) {
			throw new ReaderException("Error setting up JAXB unmarshaller.", e);
		}
		try {
			Uct uct = (Uct) unmarshaller.unmarshal(reader);
			return uct;
		} catch (JAXBException e) {
			throw new ReaderException("Error parsing source [" + name + "].", e);
		}
	}

}
