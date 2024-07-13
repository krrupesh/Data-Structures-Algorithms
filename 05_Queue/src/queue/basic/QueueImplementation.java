package queue.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class QueueImplementation {

	static int queue[];
	static int front = -1;
	static int rear = -1;
	static int size;
	static int capacity;
	
	public static void main(String[] args) throws NumberFormatException, IOException {

		System.out.println("Enter capacity of Queue");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		capacity = Integer.parseInt(br1.readLine());
		System.out.println("Capacity "+capacity);
		queue = new int[capacity];		
		
		while(true){
			System.out.println("Enter Your Choice 1 For Insert, 2 For Delete");
			BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
			int choice = Integer.parseInt(br3.readLine());
			
			switch(choice){
			
				case 1:{
					System.out.println("Enter Elements of Queue");
					BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
					int data = Integer.parseInt(br2.readLine());
					insertIntoQueue(data);
					break;
				}
				
				case 2:{
					deleteFromQueue();
					break;
				}
				
				default :{
					System.out.println("Please provide Correct Choice");
				}
			
			}	
		}
	}

	public static void insertIntoQueue(int data){
		
		if(!isFull()){
			rear = (rear +1)%capacity;
			queue[rear] = data;

			size++;
			displayQueue();
		}else{
			System.out.println("Queue is Full !");
		}		
	}
	
	public static int deleteFromQueue(){
		
		if(!isEmpty()){
			front = (front + 1)%capacity;
			int temp = queue[front];
			queue[front] = Integer.MIN_VALUE;
			size--;
			displayQueue();
			return temp;
		}else{
			System.out.println("Queue is Empty !!");
			return Integer.MIN_VALUE;
		}		
	}
	
	public static void displayQueue(){
		System.out.println("Elements in the Queue are");
		for(int i=0;i<capacity;i++){
			System.out.println("queue["+i+"]="+queue[i]);
		}
	}
	
	public static boolean isEmpty(){
		
		return (front == rear);
	}
	
	public static boolean isFull(){
		
		return (size == capacity);
	}
}
