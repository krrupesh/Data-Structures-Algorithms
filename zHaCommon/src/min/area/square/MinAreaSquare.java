package min.area.square;

public class MinAreaSquare {

	public static void main(String[] args) {

	}

	
	static void initBounds(int[] x, int[] y)
    {
        minX = x[0];
        maxX = x[0];
        minY = y[0];
        maxY = y[0];
        for(int i = 1; i < x.length; i++){
            if(x[i] > maxX)
                maxX = x[i];
            if(x[i] < minX)
                minX = x[i];
            if(y[i] > maxY)
                maxY = y[i];
            if(y[i] < minY)
                minY = y[i];
        }
    }

    static int countEnclosingPoints(int[] x, int[] y, int sx1, int sy1, int sx2, int sy2)
    {
        int count = 0;
        for(int i = 0; i < x.length; i++)
        {
            if(x[i] > sx1 && x[i] < sx2 && y[i] > sy1 && y[i] < sy2)
                count++;
        }
        return count;
    }

    static int minX;
    static int minY;
    static int maxX;
    static int maxY;

    static long minArea(int[] x, int[] y, int k) {
        long area = 0;
        initBounds(x, y);
        int xDiff = maxX - minX;
        int yDiff = maxY - minY;

        int sx1 = minX - 1;
        int sy1 = minY - 1;

        int sideDiff = Math.abs(xDiff - yDiff) + 1;

        int sx2; 
        int sy2;

        if(xDiff > yDiff){
            sx2 = maxX + 1;
            sy2 = maxY + sideDiff;
        } else {
            sx2 = maxX + sideDiff;
            sy2 = maxY + 1;
        }
        area = (sx2 - sx1) * (sx2 - sx1);

        int p, q, r, s;
        int minSize = (int) Math.sqrt(k) + 1;
        for(p = sx1; p < sx2 - minSize; p++) {
            for(q = sy1; q < sy2 - minSize; q++) {
                int xd = sx2 - p; int yd = sy2 - q;
                int sd = (xd < yd)? xd : yd;
                for(int i = sd; i >= minSize; i--){
                    int count = countEnclosingPoints(x, y, p, q, p + i, q + i);
                    if(count >= k) {
                        long currArea = i * i;
                        if(currArea < area)
                            area = currArea;
                    }
                    else
                        break;
                }
            }
        }
        return area;
    }
	
	
}
