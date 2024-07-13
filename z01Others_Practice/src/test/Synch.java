package test;
public class Synch {
    public synchronized void m1() {
    }
    public synchronized void m2() {
        m1();
    }
    public static void main(String[] args) {
        Synch s = new Synch();
        s.m2();
        System.out.println("Done");
    }
}