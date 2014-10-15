package net.sf.uctool.convert;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.term.TermOut;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TermConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ConverterHelper converterHelper;

	public TermConverter(ExecutionContext executionContext) {
		this.converterHelper = new ConverterHelper(executionContext);
	}

	public TermOut convert(Term term) {
		logger.debug("Converting term {}.", term.getName());
		TermOut o = new TermOut();
		String name = term.getName();
		o.setName(name);
		o.setAbbreviation(term.getAbbreviation());

		DescriptionType description = term.getDescription();
		if (null != description) {
			StringBuilder sb = new StringBuilder();
			for (Object content : description.getContent()) {
				converterHelper.writeDescription(sb, content, "term", name,
						name);
			}
			o.setDescription(sb.toString().trim());
		}

		return o;
	}

}
