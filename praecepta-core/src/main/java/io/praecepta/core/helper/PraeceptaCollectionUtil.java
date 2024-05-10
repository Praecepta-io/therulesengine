package io.praecepta.core.helper;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PraeceptaCollectionUtil {

	public static <T> boolean isEmpty(Collection<T> items){
		if(items != null && items.size() > 0){
			return false;
		}
		return true;
	}
	
	public static <T> boolean isNotEmpty(Collection<T> items){
		if(isEmpty(items)){
			return false;
		}
		return true;
	}
	
	public static <T> List<List<T>> chunkListItems(List<T> items, int chunkSize){
		List<List<T>> itemsToReturn = new ArrayList<List<T>>();
		if(  isNotEmpty(items)){
			if(items.size() > chunkSize){
				List<T> itemToReturn = new ArrayList<T>(); 
				for(int i = 0; i < items.size(); i++){
					if(i % chunkSize == 0){
						itemsToReturn.add(itemToReturn);
						itemToReturn = new ArrayList<T>(); 
					}
					itemToReturn.add(items.get(i));
				}
				
			}
		}
		return itemsToReturn;
	}
	
	public static <T> Set<Set<T>> chunkSetItems(Set<T> items, int chunkSize){
		Set<Set<T>> itemsToReturn = new HashSet<Set<T>>();
		if(isNotEmpty(items)){
//			if(items.size() > chunkSize){
				Set<T> itemToReturn = new HashSet<T>(); 
				int i = 0;
				Iterator<T> iterator = items.iterator();
				while(iterator.hasNext()){
					if(i != 0 && i % chunkSize == 0){
						itemsToReturn.add(itemToReturn);
						itemToReturn = new HashSet<T>(); 
					}
					i++;
					itemToReturn.add(iterator.next());
				}
				
				if(itemToReturn.size() > 0){
					itemsToReturn.add(itemToReturn); // For Last Chunk
				}
//			}
		}
		return itemsToReturn;
	}
}
