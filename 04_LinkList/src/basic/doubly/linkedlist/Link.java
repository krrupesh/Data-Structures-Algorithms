package basic.doubly.linkedlist;

class Link
{
public long dData;                 // data item
public Link next;                  // next link in list
public Link previous;              // previous link in list
// ------------------------------------------------------------
public Link(long d)                // constructor
{ 
	dData = d; 
}
// -------------------------------------------------------------
// display this link
public void displayLink()         
{ System.out.print(dData+" "); 
}
 
}
