package newhiresetB.print.elements.counter.sorted.order;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class FrequencySorter
{
private static int[] a;

public static void print()
{
for (int i : a)
{
System.out.print(i + " ");
}
}

public static int[] sort()
{
Arrays.sort(a);

Map<Integer,Integer> m = new TreeMap<Integer,Integer>();

int counter = 0;
int index = 0;
int previous = a[index];
for (int i : a)
{
if (i == previous)
{
counter++;
}
else
{
m.put(counter, previous);
counter = 0;
}
previous = i;
}

int[] b = new int[m.size()];

int n = m.size()-1;
for (Integer i : m.values())
{
	b[n] = i;
	n--;
}

return b;

}

public static void main(String[] args)
{
int[] b = {1,2,2,1,3,3,3,3,3,4,5};

a = b;

int[] s = sort();

print();

System.out.println();

for (int i : s)
{
System.out.print(i + " ");
}

}

}