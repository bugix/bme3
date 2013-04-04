package medizin.client.util;

import java.util.Iterator;
import java.util.RandomAccess;
import java.util.Set;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;

public class Matrix<E>  implements Iterable<E>, RandomAccess, Cloneable, java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private TreeBasedTable <Integer,Integer,E> table = TreeBasedTable .create();
	
	private final Predicate<E> NOT_NULL = new Predicate<E>() {

		@Override
		public boolean apply(E input) {
			return input != null;
		}
	};

	public Matrix() {
		clear();
	}
	
	public boolean isEmpty() {
		return table.isEmpty();
	}

	public boolean contains(E o) {
		return table.containsValue(o);
	}

	@Override
	public Iterator<E> iterator() {
		return FluentIterable.from(table.values()).filter(NOT_NULL).iterator();
	}


	public boolean set(int row,int column, E e) {
		
		if(row < 0 || column < 0) {
			Log.info("row or column negative : row " + row + " column " + column);
			return false;
		}
		
		table.put(row, column, e);
		return true;
	}

	public E remove(int row,int column) {
		return table.remove(row, column);
	}
	
	public void clear() {
		table.clear();
	}

	public E get(int row, int column) {
		return table.get(row, column);
	}

	public  boolean rowExists(int row) {
		return table.containsRow(row);
	}
	
	public boolean exists(int row, int column) {
		return table.contains(row, column); 
	}

	public int getRows() {
		if(table.rowKeySet().isEmpty()){
			return 0;
		}else {
			return table.rowKeySet().last() + 1;
		}
	}

	public int getColumns(int row) {
		if(table.row(row).isEmpty()) {
			return 0;
		}
		else {
			return table.row(row).lastKey()+1;
		}
	}

	public boolean addColumn(int row,int startFromColumn, E e) {
		int column = getColumns(row) < startFromColumn ? startFromColumn: getColumns(row);
		return set(row,column,e);	
	}

	public boolean addRow(int startFrom, int currentColumn, E e) {	
		int row = getNextRow(startFrom);
		Log.info("addRow: Element (row*column): (" +row +"*" + currentColumn + ") "+ e.toString());
		return set(row, currentColumn, e);
	}

	public boolean add(int startFromRow,int startFromColumn, E e) {
		int row = getNextRow(startFromRow); 
		Log.info("addRow: Element (row*column): (" +row +"* unknow(startFromColumn:"+startFromColumn+") ) "+ e.toString());
		return addColumn(row, startFromColumn, e);
	}

	private int getNextRow(int startFrom) {
		return getRows() < startFrom ? startFrom : getRows(); 
	}

	public int getColumnForObject(final E e) {
		Optional<Cell<Integer, Integer, E>> optional = getCellForValue(e);
		
		if(optional.isPresent()) {
			return optional.get().getColumnKey();
		}else {
			return -1;
		}
	}

	public int getRowForObject(E e) {
		Optional<Cell<Integer, Integer, E>> optional = getCellForValue(e);
		
		if(optional.isPresent()) {
			return optional.get().getRowKey();
		}else {
			return -1;
		}		
	}
	
	private Optional<Cell<Integer, Integer, E>> getCellForValue(final E e) { 
		return FluentIterable.from(table.cellSet()).filter(new Predicate<Cell<Integer, Integer, E>> () {

			@Override
			public boolean apply(Cell<Integer, Integer, E> input) {
				
				return e.equals(input.getValue());
			}
		}).first();
	}

	public boolean removeRow(int currentRow) {	
		table.row(currentRow).clear();
		return true;
	}
	
	public boolean removeColumn(int currentColumn) {
		table.column(currentColumn).clear();
		return true;
	}

	public boolean removeCell(int row, int column) {
		return table.remove(row, column) != null;
	}

	public Set<Cell<Integer, Integer, E>> filter(final Predicate<E> predicate) {
		Set<Cell<Integer, Integer, E>> filterSeted = null;

		if(predicate != null) {
			Iterator<Cell<Integer, Integer, E>>  iterator = FluentIterable.from(table.cellSet()).filter(new Predicate<Cell<Integer,Integer,E>>() {

				@Override
				public boolean apply(Cell<Integer, Integer, E> input) {
					return predicate.apply(input.getValue());
				}
			}).iterator();
			
			filterSeted = Sets.newHashSet(iterator);
		}else {
			filterSeted = Sets.newHashSet();
		}
		
		return filterSeted;
	}
}
