package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * This program takes input from the user in form of integers and puts all distinct integers in a sorted binary tree.
 * The class implements some methods for working with the tree, such as printing all the values within the tree
 * in a sorted manner (both ascending and descending), adding a new node, checking if an integer already
 * exists inside the tree and finding the size of the tree.
 * 
 * @author Ivan Skorupan
 */
public class UniqueNumbers {

	/**
	 * This static class is a simple model of a tree node. It contains references to nodes left and right of itself
	 * and the integer value it holds.
	 */
	static class TreeNode {
		TreeNode left;
		TreeNode right;
		int value;
	}

	/**
	 * This method is the starting point of the program.
	 * It takes user input using a Scanner object and puts all valid integers inside a sorted binary tree.
	 * Afterwards, the tree is printed out in a sorted manner both ascending and descending.
	 * 
	 * @param args command line arguments, not used in this task
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in); // a Scanner object to use for input
		TreeNode glava = null; // the root node is initially null
		
		/* Some test code
		
		System.out.println(treeSize(glava));
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);

		System.out.println(glava.value);
		System.out.println(glava.left.value);
		System.out.println(glava.left.right.value);
		System.out.println(glava.right.value);

		int velicina = treeSize(glava);
		System.out.println(velicina);

		if(containsValue(glava, 76)) {
			System.out.println("da");
		}
		
		*/
		
		while(true) {
			System.out.print("Unesite broj > ");
			String inputLine = scanner.next(); // read a line from user

			if(!inputLine.equals("kraj")) {
				int inputNumber = 0;
				try {
					inputNumber = Integer.parseInt(inputLine);
				} catch(NumberFormatException ex) {
					System.out.printf("'%s' nije cijeli broj.%n", inputLine);
					continue;
				}

				if(!containsValue(glava, inputNumber)) { // if the input value isn't already contained in the tree, we will add it
					glava = addNode(glava, inputNumber);
					System.out.println("Dodano.");
				} else {
					System.out.println("Broj već postoji. Preskačem.");
				}
			} else {
				System.out.print("Ispis od najmanjeg: ");
				printSortedTree(glava, true); // print ascending
				System.out.println();
				System.out.print("Ispis od najvećeg: ");
				printSortedTree(glava, false); // print descending
				System.out.println();
				break;
			}
		}
		scanner.close(); // close the Scanner object to correctly release resources
	}

	/**
	 * This method takes the root of a sorted binary tree and a sorting indicator as inputs and then prints out the tree
	 * in a sorted manner (ascending or descending).
	 * 
	 * @param root root of the tree
	 * @param printAscending indicator by which to sort the output (true for ascending, false for descending)
	 */
	public static void printSortedTree(TreeNode root, boolean printAscending) {
		if(root == null) {
			return;
		}
		
		/*
		 * If we print the tree in ascending order, we start from the leftmost node and work our way
		 * through the tree in a left --> center --> right node manner.
		 * 
		 * If we print the tree in descending order, we start from the rightmost node and work our way
		 * through the tree in a right --> center --> left node manner.
		 */
		if(printAscending) {
			printSortedTree(root.left, true);
		} else {
			printSortedTree(root.right, false);
		}
		
		System.out.printf("%d ", root.value);
		
		if(printAscending) {
			printSortedTree(root.right, true);
		} else {
			printSortedTree(root.left, false);
		}
		return;
	}

	/**
	 * This method takes the root of a sorted binary tree and an integer value as inputs
	 * and then checks if the integer value is already contained within the tree.
	 * 
	 * @param root root of the tree
	 * @param value integer to check for inside the tree
	 * @return true if value is inside the tree, false otherwise
	 */
	public static boolean containsValue(TreeNode root, int value) {
		if(root == null) {
			return false;
		}
		if(root.value == value) {
			return true;
		}

		boolean isLeft = containsValue(root.left, value); // check if the value is to the left of the root node
		boolean isRight = containsValue(root.right, value); // check if the value is to the right of the root node
		if(isLeft || isRight) {
			return true;
		}
		return false;
	}

	/**
	 * This method calculates the size of a sorted binary tree whose root node is given as an argument to the method.
	 * 
	 * @param root root of the tree
	 * @return size of the tree
	 */
	public static int treeSize(TreeNode root) {
		if(root == null) {
			return 0;
		}
		if(root.left == null && root.right == null) {
			return 1;
		}

		return 1 + treeSize(root.left) + treeSize(root.right); // recursively find the leaves and then work our way back up the tree 
	}

	/**
	 * This method attempts to add a given integer value inside a sorted binary tree whose root is given as an argument to the method.
	 * Upon success, a new node is being created inside the tree containing the given value. The value will not be added if it already
	 * exists within the tree (in that case, the method just returns the root node and does not change the tree).
	 * 
	 * @param root root of the tree
	 * @param value integer value to add to the tree
	 * @return the root node is returned
	 */
	public static TreeNode addNode(TreeNode root, int value) {
		if(root == null) {
			root = new TreeNode();
			root.value = value;
			return root;
		}

		if(value < root.value) {
			root.left = addNode(root.left, value); // if the value is smaller then current node's value, go left
		} else if(value > root.value) {
			root.right = addNode(root.right, value); // if the value is greater than current node's value, go right
		}

		return root;
	}

}
