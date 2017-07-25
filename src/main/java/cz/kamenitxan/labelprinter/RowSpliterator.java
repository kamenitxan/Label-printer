package cz.kamenitxan.labelprinter;

import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;
import java.util.function.Consumer;

public class RowSpliterator extends FixedBatchSpliterator<Row> {

	private final Iterator<Row> cr;
	private final long size;

	RowSpliterator(Iterable<Row> cr, int batchSize, long size) {
		super(IMMUTABLE | ORDERED | NONNULL, batchSize);
		if (cr == null) throw new NullPointerException("CSVReader is null");
		this.cr = cr.iterator();
		this.size = size;
	}

	RowSpliterator(Iterable<Row> cr, long size) {
		this(cr, 100, size);
	}

	@Override
	public boolean tryAdvance(Consumer<? super Row> action) {
		if (action == null) throw new NullPointerException();
		try {
			if (!cr.hasNext()) {
				return false;
			}
			final Row row = cr.next();
			if (row == null) {
				return false;
			}
			action.accept(row);
			return true;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void forEachRemaining(Consumer<? super Row> action) {
		if (action == null) throw new NullPointerException();
		try {
			while (cr.hasNext()) {
				action.accept(cr.next());
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getExactSizeIfKnown() {
		return size;
	}
}
