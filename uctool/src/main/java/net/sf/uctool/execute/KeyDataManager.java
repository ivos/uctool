package net.sf.uctool.execute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.uctool.output.data.DataOut;

public class KeyDataManager {

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
			if (i++ > maxLength) {
				break;
			}
			if (item.getOrder() < maxRefs) {
				break;
			}
			executionContext.getKeyData().add(item.getDataOut());
		}
	}

	private int cutSameOrderCrossingMaxLength(List<KeyDataItem> items) {
		if (items.size() <= MAX_LENGTH) {
			return items.size();
		}
		int i = MAX_LENGTH - 1;
		while (items.get(i).getOrder() == items.get(MAX_LENGTH).getOrder()
				&& i > 0) {
			i--;
		}
		if (0 == i) {
			i = MAX_LENGTH;
		}
		return i;
	}

	private final static int MAX_LENGTH = 5;

	private static class KeyDataItem implements Comparable<KeyDataItem> {

		private final DataOut dataOut;
		private final int order;

		public KeyDataItem(DataOut dataOut) {
			this.dataOut = dataOut;
			this.order = dataOut.getReferencesData().size()
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
