package net.sf.uctool.execute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.uctool.output.data.DataOut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyDataManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutionContext executionContext;

	public KeyDataManager(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public void setupKeyData() {
		List<DataOut> dataOuts = new ArrayList<DataOut>(executionContext
				.getDataOuts().values());
		if (dataOuts.isEmpty()) {
			return;
		}
		List<KeyDataItem> items = new ArrayList<KeyDataManager.KeyDataItem>();
		for (DataOut dataOut : dataOuts) {
			items.add(new KeyDataItem(dataOut));
		}

		Collections.sort(items);

		int i = 0;
		int maxLength = cutSameOrderCrossingMaxLength(items);
		int maxRefs = items.get(0).getOrder() / 2;
		for (KeyDataItem item : items) {
			if (i++ >= maxLength) {
				break;
			}
			if (item.getOrder() < maxRefs) {
				logger.debug(
						"Data {} has too few references {}, skipping all following.",
						item.getDataOut().getRefcode(), item.getOrder());
				break;
			}
			logger.debug("Adding key data {} with number of references {}.",
					item.getDataOut().getRefcode(), item.getOrder());
			executionContext.getKeyData().add(item.getDataOut());
		}
	}

	private int cutSameOrderCrossingMaxLength(List<KeyDataItem> items) {
		if (items.size() <= MAX_LENGTH) {
			logger.debug("Too few data, including all of them.");
			return items.size();
		}
		if (items.get(0).getOrder() == items.get(MAX_LENGTH).getOrder()) {
			logger.debug(
					"All data within the max limit have the same number of references {}, keeping all of them.",
					items.get(0).getOrder());
			return MAX_LENGTH;
		}
		int i = MAX_LENGTH - 1;
		while (items.get(i).getOrder() == items.get(MAX_LENGTH).getOrder()
				&& i > 0) {
			logger.debug(
					"Removing data {} that has the same number of references {} as the first after-limit data {}.",
					items.get(i).getDataOut().getRefcode(), items.get(i)
							.getOrder(), items.get(MAX_LENGTH).getDataOut()
							.getRefcode());
			i--;
		}
		return i + 1;
	}

	private final static int MAX_LENGTH = 5;

	private static class KeyDataItem implements Comparable<KeyDataItem> {

		private final DataOut dataOut;
		private final int order;

		public KeyDataItem(DataOut dataOut) {
			this.dataOut = dataOut;
			this.order = dataOut.getInstances().size()
					+ dataOut.getReferencesData().size()
					+ dataOut.getReferencesInstances().size()
					+ dataOut.getReferencesUcs().size();
		}

		@Override
		public int compareTo(KeyDataItem o) {
			return new Integer(o.getOrder()).compareTo(order);
		}

		public DataOut getDataOut() {
			return dataOut;
		}

		public int getOrder() {
			return order;
		}

	}

}
