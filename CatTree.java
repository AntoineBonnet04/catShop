import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException; 


public class CatTree implements Iterable<CatInfo>{
    public CatNode root;
    
    public CatTree(CatInfo c) {
        this.root = new CatNode(c);
    }
    
    private CatTree(CatNode c) {
        this.root = c;
    }
    
    
    public void addCat(CatInfo c)
    {	
        this.root = root.addCat(new CatNode(c));
    }
    
    public void removeCat(CatInfo c)
    {
        this.root = root.removeCat(c);
    }
    
    public int mostSenior()
    {
        return root.mostSenior();
    }
    
    public int fluffiest() {
        return root.fluffiest();
    }
    
    public CatInfo fluffiestFromMonth(int month) {
        return root.fluffiestFromMonth(month);
    }
    
    public int hiredFromMonths(int monthMin, int monthMax) {
        return root.hiredFromMonths(monthMin, monthMax);
    }
    
    public int[] costPlanning(int nbMonths) {
        return root.costPlanning(nbMonths);
    }
    
    public Iterator<CatInfo> iterator()
    {
        return new CatTreeIterator();
    }
    
    
    class CatNode {
        
        CatInfo data;
        CatNode senior;
        CatNode same;
        CatNode junior;
        
        public CatNode(CatInfo data) {
            this.data = data;
            this.senior = null;
            this.same = null;
            this.junior = null;
        }
        
        public String toString() {
            String result = this.data.toString() + "\n";
            if (this.senior != null) {
                result += "more senior " + this.data.toString() + " :\n";
                result += this.senior.toString();
            }
            if (this.same != null) {
                result += "same seniority " + this.data.toString() + " :\n";
                result += this.same.toString();
            }
            if (this.junior != null) {
                result += "more junior " + this.data.toString() + " :\n";
                result += this.junior.toString();
            }
            return result;
        }
        
        
        public CatNode addCat(CatNode c) {
        	if (c == null) {return this;}
        	
        	CatNode pointer = this; 		// root of subtree on which method is called
            while (true) {
            	if(c.data.monthHired < pointer.data.monthHired) { // c senior to pointer
            		
            		if (pointer.senior == null) {pointer.senior = c; return this;}
            		else {pointer = pointer.senior;}}
            	
            	else if (c.data.monthHired > pointer.data.monthHired){ // c junior to pointer
            		
            		if (pointer.junior == null) {pointer.junior = c; return this;}
            		else {pointer = pointer.junior;}}
            	
            	else { // same seniority as pointer
            		
            		if (c.data.furThickness > pointer.data.furThickness) { // c longer fur than pointer
            			
            			CatNode tmp = new CatNode(pointer.data);
            			pointer.data = c.data;
            			c.data = tmp.data;
            			if (pointer.same == null) {pointer.same = c; return this;}
            			else {pointer.same.addCat(tmp);} //add previous root to subtree
            			
            			return this;}
            		
            		else {	// c shorter fur than pointer
            			if (pointer.same == null) {pointer.same = c; return this;}
            			else {pointer = pointer.same;}
            		}}}}
       
        private void removeLeaf(CatInfo c) {
        	// takes as input a leaf CatInfo, finds its parent and removes the leaf
        	// We can assume c has no children and is not the root
        	if (this.junior != null) {
        	if (this.junior.data.equals(c)){ this.junior = null;}
        	else {this.junior.removeLeaf(c);}}
        	
        	if (this.same != null) {
        	if (this.same.data.equals(c)){ this.same = null;}
        	else {this.same.removeLeaf(c);}}
        	
        	if (this.senior != null) {
        	if (this.senior.data.equals(c)) {this.senior = null;}
        	else {this.senior.removeLeaf(c);}}
        		
        	}
        
        
        public CatNode removeCat(CatInfo c) {
        	CatNode temp = this;
        	
        	if (c.monthHired > this.data.monthHired) {
        		if (this.junior != null) {this.junior.removeCat(c);}} 
        	
        	else if (c.monthHired < this.data.monthHired) {
        		if (this.senior != null) {this.senior.removeCat(c);}}
        	
        	else { // same month as c
        		if (this.data.equals(c)) {
        			
        			if (this.same != null) {
        				this.data = this.same.data;
                		this.addCat(this.same.junior);
                		this.addCat(this.same.senior);
                		this.same = this.same.same;
                		}
        			
        			else if (this.senior != null) { // same == null
        				CatNode tmp = this.junior;
        				
        				this.data = this.senior.data;
        				this.same = this.senior.same;
        				this.junior = this.senior.junior;
        				this.senior = this.senior.senior;
        				
        				this.addCat(tmp);} 
        			
        			else if (this.junior != null) { // same == senior == null
        				
        				this.data = this.junior.data;
        				this.senior = this.junior.senior;
        				this.same = this.junior.same;
        				this.junior = this.junior.junior;}
        			
        			else { //same == senior == junior == null
        				if (this == root) {root = null;}
        				else {root.removeLeaf(this.data);}} 
        		}
        		else if (this.same != null) {this.same.removeCat(c);}
        	}
        	return temp;
    }
        
        public int mostSenior() {
        	if (this.senior != null) { 
        		return this.senior.mostSenior();}
        	else {return this.data.monthHired;}
        }
        
        public int fluffiest() {
        	int maximum = this.data.furThickness; 
        		
        	// Find maximum in junior subtree
        	if (this.junior != null) {
        		int juniorMax = this.junior.fluffiest();
        		if (juniorMax > maximum) {maximum = juniorMax;}}
        	
        	// Find maximum in senior subtree
        	if (this.senior != null) {
        		int seniorMax = this.senior.fluffiest();
        		if (seniorMax > maximum) {maximum = seniorMax;}}
        	return maximum;
        	}
       
        public int hiredFromMonths(int monthMin, int monthMax) {
        	
        	if (root == null || monthMin > monthMax) {return 0;}
        	
        	int number = 0;
        	if (monthMin <= this.data.monthHired && this.data.monthHired <= monthMax) {
        		number ++;}
        	
        	if (this.junior != null) {number += this.junior.hiredFromMonths(monthMin, monthMax);}
        	if (this.same != null) {number += this.same.hiredFromMonths(monthMin, monthMax);}
        	if (this.senior != null) {number += this.senior.hiredFromMonths(monthMin, monthMax);}
        	
            return number;
        }
        
        public CatInfo fluffiestFromMonth(int month) {
        	
        	CatInfo fluff = null;
        	if (this.data.monthHired == month) {fluff = this.data;}
        	
        	else if (this.data.monthHired > month) { // too junior
        		if (this.senior == null) {return null;}
        		else {fluff = this.senior.fluffiestFromMonth(month);}}
        		
        	else { // too senior
            	if (this.junior == null) {return null;}
            	else {fluff = this.junior.fluffiestFromMonth(month);}}
            	
            return fluff;
        }
        
        private int costTraversal(int month) {
        	int cost = 0; // total cost for given month
        
        	if (this.data.nextGroomingAppointment == month) {
        		cost += this.data.expectedGroomingCost;
        	}
        	if (this.junior != null) {
        		cost += this.junior.costTraversal(month);}
        	if (this.same != null) {
        		cost += this.same.costTraversal(month);}
        	if (this.senior != null) {
        		cost += this.senior.costTraversal(month);}
        	return cost;
        }
        
        public int[] costPlanning(int nbMonths) {
        	
            int[] intArray = new int[nbMonths];
            for (int i = 0; i<nbMonths; i++) {
            	int month = 243 + i; // starting in march 2020 = month 243
            	intArray[i] += this.costTraversal(month);	
            }
            return intArray; 
        }
    }
    
    private class CatTreeIterator implements Iterator<CatInfo> {
    	private CatNode[] arr;
    	private int counter;
    	
        public CatTreeIterator() {//returns an iterator object pointing to the head of the list
        	counter = 0; // pointing at most senior 
            int length = root.hiredFromMonths(0,250); // total number of nodes in tree
            arr = new CatNode[length]; 
            copyTree(root); // Unordered array of all catNodes in tree
            sortCatTree(0, length-1);
            counter = 0;
        }
        
        private void copyTree(CatNode pointer) {
        		arr[counter] = pointer; counter ++;
	        	if (pointer.junior != null) {copyTree(pointer.junior);}
	        	if (pointer.same != null) {copyTree(pointer.same);}
	        	if (pointer.senior != null) {copyTree(pointer.senior);}
        }
        
        private void sortCatTree(int start, int end) { // QUICKSORT
        	int p = partition(start, end);
        	if (p-1 > start) {sortCatTree(start, p - 1);}
        	if (p+1 < end) {sortCatTree(p + 1, end);}
        }
        
        private int partition(int start, int end) {
        	CatNode pivot = arr[end];
        
        	for (int i = start; i<end; i++) {
        		int month = arr[i].data.monthHired;
        		int fur = arr[i].data.furThickness;
        		int pivotMonth = pivot.data.monthHired;
        		int pivotFur = pivot.data.furThickness;
        		
        		if ( (month < pivotMonth) || ( month == pivotMonth && fur < pivotFur)){
        			// Swap arr[start] and arr[i]
        			CatNode temp = arr[start];
        			arr[start] = arr[i];
        			arr[i] = temp;
        			start++;
        		}
        	}
        	// swap arr[start] and arr[end]
        	CatNode temp = arr[start];
        	arr[start] = pivot;		arr[end] = temp;
        	return start;
        }
        
        public CatInfo next(){
        	//returns the element of the list that the Iterator is currently referencing 
            // and then moves to the next node
        	if (!hasNext()){throw new NoSuchElementException();}
        	else {
        	CatInfo tmp = arr[counter].data;
        	counter ++;
        	return tmp;}
        }
        
        public boolean hasNext() {
            return (counter < root.hiredFromMonths(0,250));
        }
    }
    
}
