package com.onmobile.apps.ivm.daemons.missedcalls;

public class DigitalTrie
{
	class TrieNodeItem
    {
      TrieNodeItem()
      {
      }
      Object value;
      TrieNodeItem nextRow[];
    }

	protected TrieNodeItem Root[];
	public static DigitalTrie _obj = null;
	
    private DigitalTrie()
    {
        Root = new TrieNodeItem[256];
        createNode(Root);
    }
    
    public static synchronized DigitalTrie getInstance(){
    	if(_obj == null){
    		_obj = new DigitalTrie();
    	}
    	return _obj;
    }

    protected void createNode(TrieNodeItem atrienodeitem[])
    {
        for(int i = 0; i < 256; i++){
            atrienodeitem[i] = new TrieNodeItem();
            atrienodeitem[i].value = null;
            atrienodeitem[i].nextRow = null;
        }

    }

    protected String trimValue(String s)
    {
        String s1 = s;
        s1.trim();
        for(; s1.startsWith("0"); s1 = s1.substring(1));
        return s1;
    }

    public void add(String s, Object obj)
    {
        String s1 = trimValue(s);
        if(s1.length() == 0){
            return;
        }
        TrieNodeItem atrienodeitem[] = Root;
        int i = 0;
        int k = 0;
        k = extract(s1, i);
        for(int j = 1; j < s1.length(); j++){
            if(atrienodeitem[k].nextRow == null){
                synchronized(this){
                	atrienodeitem[k].nextRow = new TrieNodeItem[256];
                    atrienodeitem = atrienodeitem[k].nextRow;
                    createNode(atrienodeitem);
                }
            }else{
                atrienodeitem = atrienodeitem[k].nextRow;
            }
            k = extract(s1, j);
        }

        atrienodeitem[k].value = null;
        atrienodeitem[k].value = obj;
    }

    public int extract(String s, int i)
    {
    	return s.charAt(i);
    }

    public Object getFirstMatch(String s)
    {
        TrieNodeItem atrienodeitem[] = Root;
        Object obj = null;
        boolean flag1 = false;
        for(int i = 0; i < s.length() && atrienodeitem != null && !flag1; i++){
            int j = extract(s, i);
            if(atrienodeitem != null && atrienodeitem[j].value != null && !flag1){
                obj = atrienodeitem[j].value;
                flag1 = true;
            }
            atrienodeitem = atrienodeitem[j].nextRow;
        }

        return obj;
    }

    public Object getBestMatch(String s)
    {
        TrieNodeItem atrienodeitem[] = Root;
        TrieNodeItem atrienodeitem1[] = null;
        int j = 0;
        Object obj = null;
        for(int i = 0; i < s.length() && atrienodeitem != null; i++){
            j = extract(s, i);
            atrienodeitem1 = atrienodeitem;
            if(atrienodeitem != null && atrienodeitem[j].value != null){
                obj = atrienodeitem[j].value;
            }
            atrienodeitem = atrienodeitem[j].nextRow;
        }

        if(atrienodeitem1 != null && atrienodeitem1[j].value != null){
            obj = atrienodeitem1[j].value;
        }
        return obj;
    }

    public Object getMatch(String s)
    {
        int i = extract(s, s.length() - 1);
        TrieNodeItem atrienodeitem[] = getMatchObj(s);
        if(atrienodeitem == null){
            return null;
        }else{
            return atrienodeitem[i].value;
        }
    }

    private TrieNodeItem[] getMatchObj(String s)
    {
        TrieNodeItem atrienodeitem[] = Root;
        TrieNodeItem atrienodeitem1[] = null;
        int j = 0;
        int i;
        for(i = 0; i < s.length() && atrienodeitem != null; i++){
            j = extract(s, i);
            atrienodeitem1 = atrienodeitem;
            atrienodeitem = atrienodeitem[j].nextRow;
        }

        if(i < s.length() - 1 || atrienodeitem != null){
            return null;
        }
        if(atrienodeitem1 != null && atrienodeitem1[j].value != null){
            return atrienodeitem1;
        }
        else{
            return null;
        }
    }

    public void markDelete(String s)
    {
        TrieNodeItem atrienodeitem[] = getMatchObj(s);
        if(atrienodeitem == null)
            return;
        int i = extract(s, s.length() - 1);
        synchronized(this){
            atrienodeitem[i].value = null;
        }
    }

    private void deleteNode(TrieNodeItem atrienodeitem[], TrieNodeItem atrienodeitem1[], int i, String s)
    {
        if(i < s.length()){
            int j = extract(s, i);
            deleteNode(atrienodeitem1, atrienodeitem1[j].nextRow, ++i, s);
        }
        if(atrienodeitem1 == null){
            if(atrienodeitem == null)
                return;
            int k = extract(s, i - 1);
            if(atrienodeitem[k].value != null)
                System.err.println("value=" + atrienodeitem[k].value);
            atrienodeitem[k].value = null;
            boolean flag = true;
            for(int l = 0; l < 9 && flag; l++){
                if(atrienodeitem[l].nextRow != null){
                    flag = false;
                }
            }

            if(flag)
                atrienodeitem = null;
        }
    }

    public void delete(String s)
    {
        synchronized(this){
            deleteNode(null, Root, 0, s);
        }
    }

    private void delete(TrieNodeItem trienodeitem)
    {
        if(trienodeitem.nextRow == null){
            trienodeitem = null;
        }else{
            for(int i = 0; i < 10; i++){
                delete(trienodeitem.nextRow[i]);
            }

        }
    }

    public void destroy()
    {
        Runtime runtime = Runtime.getRuntime();
        long l = runtime.freeMemory();
        for(int i = 0; i < 10; i++){
            delete(Root[i]);
        }

        Root = null;
        runtime.gc();
        long l1;
        do{
            l1 = l;
            runtime.gc();
            l = runtime.freeMemory();
            System.gc();
        }while(l > l1);
    }
    
    public static void main(String[] args){
    	DigitalTrie.getInstance().add("9845", "airtel-bangalore");
    	DigitalTrie.getInstance().add("9900", "airtel-bangalore");
    	DigitalTrie.getInstance().add("9901", "airtel-bangalore");
    	DigitalTrie.getInstance().add("9886", "voda-bangalore");
    	System.out.println(DigitalTrie.getInstance().getBestMatch("9845999999"));
    }
}
