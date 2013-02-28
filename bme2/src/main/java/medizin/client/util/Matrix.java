package medizin.client.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.RandomAccess;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Matrix<E>  implements Iterable<E>, RandomAccess, Cloneable, java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<ArrayList<E>> matrix  = Lists.newArrayList();
	private int[] size = new int[20];
	private int rows = 0;
	
	private final Predicate<E> NOT_NULL = new Predicate<E>() {

		@Override
		public boolean apply(E input) {
			return input != null;
		}
	};

	public Matrix() {
		init();
	}
	
	private void init() {
		for(int rowIndex=0;rowIndex<20;rowIndex++) {
			ArrayList<E> row = Lists.newArrayList();
			for(int columnIndex=0;columnIndex<20;columnIndex++) {
				row.add(null);
			}
			matrix.add(row);
			size[rowIndex] = 0;
			rows = 0;
		}
		
		Log.info("Matrix size : " +  matrix.size());
	}
	public int size() {
		int total = 0;
		
		for (int rowSize : size) {
			total += rowSize;
		}
		return total;
	}

	public boolean isEmpty() {
		return matrix.isEmpty();
	}

	public boolean contains(E o) {
		for (ArrayList<E> row: matrix) {
			if(row.contains(0)) {
				return true;
			}
		}	
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		final ArrayList<E> list = Lists.newArrayList();
		for (ArrayList<E> row : matrix) {
			if(row != null) {
				list.addAll(Lists.newArrayList(Iterables.filter(row,NOT_NULL)));	
			}
		}
		return list.iterator();
	}


	public boolean set(int row,int column, E e) {
		
		if(row < 0 || column < 0) {
			Log.info("row or column negative : row " + row + " column " + column);
			return false;
		}
		if(rowExists(row) && column < matrix.get(row).size()) {
			if(row < size.length && size[row] <= column) {
				size[row] = column + 1;
			}
			
			if(rows < row) {
				rows = row + 1;
			}
			//Log.info("set method : Element (row*column): (" +row +"*" + column+ ") "+ e!=null?e.toString():"null");
			matrix.get(row).set(column,e);
		}else {
			return false;
		}
		
		return true;
	}

	public E remove(int row,int column, E e) {
		
		if(exists(row,column)) {
			if(set(row,column,null)) {
				
				for(int i=0;i<size[row];i++) {
					if(get(row,i) != null) {
						return e;		
					}
				}
				
				rows--;
				return e;
			}
		}
		return null;
	}
	
	public void clear() {
		init();
	}

	public E get(int row, int column) {
		if(exists(row, column)) {
			Log.info("get method :Element (row*column): (" +row +"*" + column+ ") "+ matrix.get(row).get(column));
			return matrix.get(row).get(column);
		}else {
			return null;
		}
	}

	public  boolean rowExists(int row) {
		if(row < matrix.size() && matrix.get(row) !=null) {
			return true;
		}
		return false;
	}
	
	public boolean exists(int row, int column) {
		 
		if(rowExists(row) && column < matrix.get(row).size() && matrix.get(row).get(column) != null) {
			return true;
		}
			
		return false;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns(int row) {
		if(row < size.length) {
			return size[row];
		}
		return 0;
	}

	public boolean addColumn(int row,int startFromColumn, E e) {
		if(rowExists(row) && size[row] < matrix.get(row).size()) {
			
			if(size[row] < startFromColumn) {
				size[row] = startFromColumn;
			}
			
			Log.info("addColumn: Element (row*column): (" +row +"*" + size[row] + ") "+ e.toString());
			return set(row, size[row], e);
		}
		
		return false;
		
	}

	public boolean addRow(int startFrom, int currentColumn, E e) {
		int row = getNextRow(startFrom);
		Log.info("addRow: Element (row*column): (" +row +"*" + currentColumn + ") "+ e.toString());
		return set(row, currentColumn, e);
	}

	

	public boolean add(int startFromRow,int startFromColumn, E e) {
		int row = getNextRow(startFromRow); 
		Log.info("addRow: Element (row*column): (" +row +"* unknow ) "+ e.toString());
		return addColumn(row, startFromColumn, e);
	}

	private int getNextRow(int startFrom) {
		
		for (int row = startFrom; row <size.length; row++) {
			if(size[row] == 0) {
				return row;
			}
		}
		return 20;
	}

	public int getColumnForObject(E e) {
		
		for (ArrayList<E> rows : matrix) {
			
			if(rows.contains(e)) {
				int currentColumn = 0;
				for(E obj : rows) {
					
					if(e.equals(obj)) {
						return currentColumn;
					}
					currentColumn++;
				}
				 
			}
		}
		
		return -1;
	}

	public int getRowForObject(E e) {
		int currentRow = 0;
		for (ArrayList<E> rows : matrix) {
			
			if(rows.contains(e)) {
				return currentRow;
			}
			currentRow++;
		}
		return -1;
	}

	public boolean removeRow(int currentRow) {
		
		if(rowExists(currentRow)) {
			ArrayList<E> newColumn = Lists.newArrayList();
			for(int columnIndex=0 ; columnIndex < 20 ; columnIndex++) {
				newColumn.add(null);
			}
			matrix.set(currentRow, newColumn);
			size[currentRow] = 0;
			return true;
		}
		
		return false;
	}

	public boolean removeCell(int row, int column) {
		
		if(exists(row, column)) {
			return set(row, column, null);
		}
		return false;
	}
}
