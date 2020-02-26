package kh.java.controller;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import kh.java.view.View;
import kh.java.model.*;
import kh.java.view.*;;

public class SearchWord {

	// ---------���� ���� �޼ҵ� �� ���� ���-------------------------------------------------------------------------------------
	Word word = new Word(); // �׽�Ʈ�� ��ü ����, �����Ǿ����
	View view = new View(); // �׽�Ʈ�� ��ü ����, �����Ǿ����
	ArrayList<Word> allDB = new ArrayList<Word>(); // �׽�Ʈ�� ��ü ����, �����Ǿ����
	BufferedReader br = null; // �׽�Ʈ�� ��ü ����, �����Ǿ����
	BufferedWriter bw = null; // �׽�Ʈ�� ��ü ����, �����Ǿ����
	BufferedReader failbr = null; // �׽�Ʈ�� ��ü ����, �����Ǿ����
	BufferedReader failbr2 = null;
	Random r = new Random();
	Scanner sc = new Scanner(System.in);
	ArrayList<String> failName = new ArrayList<String>(); // �������Ͽ� �� ������ ���ܾ ������ �迭
	ArrayList<Word> failDB = new ArrayList<Word>(); // ���� ���ܾ� + �� �ΰ� ������ �迭

	public void controlMain() { // ù �޴�

		while (true) {
			int select = view.main();
			switch (select) {
			case 1: // �ܾ� �˻�
				search();
				break;
			case 2:// ���ܾ� ���
				Insertdictionary();
				break;
			case 3:// �׽�Ʈ
				testMenu();
				break;
			case 4:// ���� ��Ʈ
				failMenu();
				break;
			case 5:// �ܾ� ���� �� ����
				updateOrDelete();
				break;
			case 0:
				return;
			}
		}
	}

	public SearchWord() { // �ܾ��� ��ü �о�� ����Ʈ�� �߰�
		FileReader fr;
		FileReader fr2;
		try {
			br = new BufferedReader(new FileReader("C:\\Users\\user1\\Desktop\\allDB.txt"));

			String str;

			while (true) {
				str = br.readLine();
				if (str == null) {
					break;

				} else {
					StringTokenizer sT = new StringTokenizer(str, "/");

					String name = sT.nextToken();
					String mean1 = sT.nextToken();
					String mean2 = sT.nextToken();

					allDB.add(new Word(name, mean1, mean2));
				}
			}
			
			fr = new FileReader("C:\\Users\\user1\\Desktop\\failDB.txt");
			failbr = new BufferedReader(fr);
			
			fr2 = new FileReader("C:\\Users\\user1\\Desktop\\failDB.txt"); // �������� �ҷ���
			failbr2 = new BufferedReader(fr2);

			while (true) {
				
				String toke = failbr.readLine();
				String str1 = failbr2.readLine();
				
				if (toke == null || str1 == null) { // ���̻� �ҷ��� �ؽ�Ʈ ���� ��
					break;
				}
				
				
				// �ܾ�+�� ����
				StringTokenizer st2 = new StringTokenizer(toke, "/");				
				failDB.add(new Word(st2.nextToken(), st2.nextToken(), st2.nextToken()));
						
				StringTokenizer sT = new StringTokenizer(str1, "/"); // ���� ���پ� ��ū���� �����ֱ�
				String st = sT.nextToken(); // ù��° ��ū�� ���� - �� ������ �ܾ
				failName.add(st); // �ܾ failName �迭�� ����

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				failbr.close();
				failbr2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int searchIndex(String userInput) { // ������ �Է��� �ܾ� �ܾ��忡 �ִ��� �˻� �� ��� ���� !!!!!!!���� ����!!!!!!!!!

		for (int i = 0; i < allDB.size(); i++) {
			if (allDB.get(i).getName().equals(userInput)) { // i��° ��ü�� getName���� userInput�� �����Ҷ�
				return i; // i�� ����
			}
		}
		return -1; // �ش�Ǵ� �ܾ ã�� ���Ұ�� -1 ����
	}

	public void saveDB() { // ����, ������ ������ ������ DB ���� �޼ҵ� !!!!!!!���� ����!!!!!!!!!

		try {
			bw = new BufferedWriter(new FileWriter("C:\\Users\\user1\\Desktop\\allDB.txt")); // allDB.txt�� �����ϴ� BufferWriter ����
			int count = 0;
			for (Word w : allDB) { // allDB ���� ��� Word ��ü �˻�
				if(count!=0) {
					bw.newLine();
					
				}
				// �˻��� Word ��ü�� �̸�, mean1, mean2�� ������ / �� �������� FileWrite
				bw.write(w.getName() + "/" + w.getMean1() + "/" + w.getMean2());
				count++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ---------���� �� ����_��������-------------------------------------------------------------------------------------

	public void updateOrDelete() { // �������� �޴�ȭ���� ����ϰ� �Է°��� ���Ϲ޾� ����

		while (true) {
			int slt = view.updateMain();

			switch (slt) {
			case 1: // �ܾ� ����
				modifyWord();
				break;
			case 2: // �ܾ� ����
				delWord();
				break;
			case 0:
				System.out.println("���� �޴��� ���ư��ϴ�.");
				return;
			default:
				System.out.println("0~2 ������ ���ڸ� �Է��ϼ���.");
			}
		}
	}

	public void modifyWord() { // �ܾ� ���� �� ����Ʈ�� ����
		String userInput = view.getName("����");

		int index = searchIndex(userInput);

		if (index == -1) {
			System.out.println("ã���ô� �ܾ �����ϴ�.");

		} else {
			String currentMean1 = allDB.get(index).getMean1(); // ��ü �ܾ �ִ� ����Ʈ�� ��1�� �� 2
			String currentMean2 = allDB.get(index).getMean2();

			System.out.println("[" + userInput + "] �ܾ �����մϴ�.");
			String mean1 = view.getMean("���� ���� : " + currentMean1 + "\t����  : "); // �ܾ��� ù��° ��

			System.out.println("���� �Է��� �ߴ��Ͻ÷��� x �� �Է��ϼ��� ��");
			String mean2 = view.getMean("���� ���� : " + currentMean2 + "\t����  : ");

			if (mean2.charAt(0) == 'x') {
				allDB.get(index).setMean1(mean1);
				saveDB();

			} else {
				allDB.get(index).setMean1(mean1);
				allDB.get(index).setMean2(mean2);
				saveDB();
			}
		}
	}

	public void delWord() { // �ܾ� ���� �޼ҵ� !!!!!!!���� ����!!!!!!!!!
		String userInput = view.getName("����"); // ������ �ܾ�

		int index = searchIndex(userInput);

		if (index == -1) { // �Է��� �ܾ DB�� �������� ������
			System.out.println("ã���ô� �ܾ �����ϴ�.");
			return; // �޼ҵ� ����

		} else if (view.delWord(userInput)) { // ������ y�� �Է��� �ܾ������ �����ϴ� ��� = true�� ���ϵ� ��
			allDB.remove(index);
			saveDB();

		} else { // ������ n�� �Է��Ͽ� �ܾ������ ����� ���
			return;
		}
	}

	// ---------�˻�_����-------------------------------------------------------------------------------------
	public void search() {

		while (true) {
			int select = view.searchDictionary(); // view���� ���� �˻����� �ѿ� �˻�����
			switch (select) {
			case 1:
				searchEng();
				break;
			case 2:
				searchKor();
				break;
			case 0:
				return;
			}
		}
	}

	public void searchEng() { // ���ܾ�� �� �˻�
		// �Է¹޴� ȭ���� ViewŬ������ ��ü �����
		String search = view.input(); // �˻��� �ܾ� �Է¹ޱ�

		for (int i = 0; i < search.length(); i++) {// ���ܾ� �̿��� ���� �Է� ��
			if (search.charAt(i) < 'A' || search.charAt(i) > 'z') {
				System.out.println("��� �Է��� �� �ֽ��ϴ�.");
				return;
			}
		}

		search = search.toLowerCase();
		boolean find = false; // �ܾ��忡 ���� �ܾ �˻����� �� ���´ܾ��� �����ֱ�����
		for (int i = 0; i < allDB.size(); i++) {
			if (allDB.get(i).getName().equals(search)) {// �ܾ����� �ܾ�� �� ���� �Է��ѰͰ� ��ġ�ϴ°��� ������
				System.out.println("�ܾ� : " + allDB.get(i).getName() + "\nù��° �� : " + allDB.get(i).getMean1()
						+ "\n�ι�° �� : " + allDB.get(i).getMean2());
				find = true;
			}
		}
		if (find == false) {
			System.out.println("�ܾ��忡 ���� �ܾ��Դϴ�!");
		}
	}

	public void searchKor() { // ������ ���ܾ� �˻�

		String search = view.input(); // �˻��� �ܾ� �Է¹ޱ�
		boolean find = false; // �ܾ��忡 ���� �ܾ �˻����� �� ���´ܾ��� �����ֱ�����
		for (int i = 0; i < allDB.size(); i++) {
			// ���� �˻��� ���� ��� �ܾ��� mean1 �Ǵ� mean2 �߿� ��ġ�ϴ°� ������
			if (allDB.get(i).getMean1().equals(search) || allDB.get(i).getMean2().equals(search)) {
				// ���� ���� ���� �ܾ� ��� ���
				System.out.println("�ܾ� : " + allDB.get(i).getName() + "\nù��° �� : " + allDB.get(i).getMean1()
						+ "\n�ι�° �� : " + allDB.get(i).getMean2());
				find = true;
			}
		}
		if (find == false) {
			System.out.println("[" + search + "]�� ���� ���� �ܾ �����ϴ�!");
		}
	}

	// ---------�׽�Ʈ_��������-------------------------------------------------------------------------------------

	public void testMenu() {

		try {
			FileReader fr2 = new FileReader("C:\\Users\\user1\\Desktop\\failDB.txt"); // �����Ʈ �о��
			failbr = new BufferedReader(fr2);

			// failDB������ �о�� (������ �ִ� ���䵵 �׻� �����Ʈ�� ����־���ϴϱ�)
			while (true) {
				String str = failbr.readLine();
				if (str == null)
					break; // �ܾ� ����ɰ� ������ break;
				StringTokenizer sT = new StringTokenizer(str, "/");
				Word word = new Word(sT.nextToken(), sT.nextToken(), sT.nextToken());
				failDB.add(word); // ����ܾ� ����Ʈ�� ����.
			}

			// ���Ӽ���
			while (true) {
				int select = view.vocaTest(); // view���� �޴����� �Է¹���.
				switch (select) {
				case 1: // �����׽�Ʈ���
					failDB = view.engTest(allDB); // ��縮��Ʈ�� �ְ� Ʋ������Ʈ�� �޴´�.
					System.out.println(failDB.size());
//					if (!failList.isEmpty()) { // ��������Ʈ�� ������ �ִٸ�
//						for (Word w : failList) { // ��������Ʈ�� �ִ� Word��ü�� failDB ������.
//							System.out.println("test");
//							failDB.add(w);
//						}
//					}
					break;
				case 2: // �ѿ��׽�Ʈ���
					ArrayList<Word> failList2 = view.korTest(allDB);// ��縮��Ʈ�� �ְ� Ʋ������Ʈ�� �޴´�.
					if (!failList2.isEmpty()) { // ��������Ʈ�� ������ �ִٸ�
						for (Word w : failList2) { // ��������Ʈ�� �ִ� Word��ü�� failDB ������.
							failDB.add(w);
						}
					
					}
					break;
				case 0:
					output(); // �ڷΰ��⸦ ������ failDB�� ��µ�.(�����Ʈ)
//					failDB.clear();
					return;
				}
			} // �׽�Ʈ ùȭ�� �ݺ� while
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				failbr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} // main

	public void output() {
		BufferedWriter bw = null;

		try {
			FileWriter fw = new FileWriter("C:\\Users\\user1\\Desktop\\failDB.txt"); // �����Ʈ ����
			bw = new BufferedWriter(fw);
			for (Word w : failDB) { // failDB����Ʈ�� �ִ� Word��ü��
				bw.write(w.toString()); // ���پ� ���
				bw.newLine(); // ����
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// ---------����_���δ���-------------------------------------------------------------------------------------

	public void failMenu() {
		System.out.println(failDB.size());
		while (true) {
			int select = view.failMenu();
			switch (select) {
			case 1:
				readNote();
				break;
			case 2:
				reTest();
				break;
			case 3:
				clearNote();
				break;
			case 0:
				return;
			}
		}
	}
	// ---------����_���δ���-------------------------------------------------------------------------------------
	public void reTest() { // ����� ���� ���� �� �����Ʈ���� ������Ű��
		
		view.reTest();
		char agree = sc.next().toLowerCase().charAt(0); // ����� ���� �Է�
		sc.nextLine();
		switch (agree) {
		case 'n': // ����� ��ġ ���� �� ����
			System.out.println("���� �޴��� ���ư��ϴ�.\n");
			return;
		case 'y': // ����� ���� ��
			if(failDB.size()==0) {
				System.out.println("�����Ʈ�� �ܾ �����ϴ�!");
				return;
			}
			System.out.println("�׽�Ʈ�� �����մϴ�. ���� ���ܾ ���� ���� �Է��ϼ���.");			
			int size = failName.size(); //3
			for (int i = 0; i < size; i++) {//0.1.2
				
				System.out.println(failName.get(i)); // ����(�� ������) ���ܾ �ε��� �ٿ��� ���
				System.out.print("�� : ");
				String ans = sc.nextLine(); // �� �Է¹���.
		
				if (failDB.get(i).getMean1().equals(ans) || failDB.get(i).getMean2().equals(ans)) {
					System.out.println("����!"); // '�Է¹��� �� '�� '������ ��'�� �ϳ��� ������ "����"
					failName.remove(i); // �ܾ� ����ִ� �迭���� �������ֱ�
					failDB.remove(i); // ���� ��
					size--;
					
				} else {
					System.out.println("Ʋ�Ƚ��ϴ�!");
				}

			}
		}
	}

	public void clearNote() {
		BufferedReader br = null;		
		BufferedWriter bw = null;
		
		// �����Ʈ ���� ���� �ؽ�Ʈ������ �� �پ� �迭�� �־��ֱ�
		try {
			FileReader fr = new FileReader("C:\\Users\\user1\\Desktop\\failDB.txt"); // ������ü���� �ҷ�����
			br = new BufferedReader(fr);
			while (true) {
				String str = br.readLine(); // �ؽ�Ʈ���� 1�پ� �о��
				if (str == null) { // ���̻� �ҷ��� �ؽ�Ʈ ���� �� while�� Ż��
					break;
				}
				StringTokenizer st = new StringTokenizer(str, "/");
				String name = st.nextToken();
				String mean1 = st.nextToken();
				String mean2 = st.nextToken();
				failDB.add(new Word(name, mean1, mean2)); // ������ name+mean1+mean2 failDB�� �־��ֱ�
			}
			// ���� �ҷ��� ������ clearó���ؼ� ����
			failName.clear();
			failDB.clear();
			
			FileWriter fw = new FileWriter("C:\\Users\\user1\\Desktop\\failDB.txt"); // �����Ʈ ����
			bw = new BufferedWriter(fw);
			for (Word w : failDB) { // failDB����Ʈ�� �ִ� Word��ü��
				bw.write(w.toString()); // ���پ� ���
				bw.newLine(); // ����
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void readNote() { // �����Ʈ �ѹ��� ���� �����ؼ� ����� ��ȸ	
		System.out.println(failName.size());
		if (failName.size() == 0) { // ������ ������ ���� ��
			System.out.println("�����Ʈ�� ��� �ܾ �����ϴ�.\n");
		} else { // failName(�� ������ �ܾ ����ִ�) �迭�� ���� ����name �Ϸ�ȭ�ؼ� ���
			for (int i = 0; i < failName.size(); i++) {
				System.out.println((i + 1) + ". " + (failName.get(i)));
			}

			view.readNote(); // viewŬ������ readNote �޼ҵ� ����
			int select = sc.nextInt(); // ��ȸ�ϰ� ���� ���� ��ȣ ����
			System.out.println("�ܾ� : " + failDB.get(select - 1).getName() + "\nù��° �� : "
					+ failDB.get(select - 1).getMean1() + "\n�ι�° �� : " + failDB.get(select - 1).getMean2()); // �� ������ ���ܾ�
																											// ����ִ� �迭����
																											// ������ ���� ���
			System.out.println();
		}

	}

	// ---------���ܾ� ���_����-------------------------------------------------------------------------------------
	// ---------�ܾ� ���_����-------------------------------------------------------------------------------------
	
	public void Insertdictionary() {
		while (true) {
			int sel = view.Insertdictionary();
			switch (sel) {
			case 1:
				inputWord();
				break;
			case 0:
				return;
			}
		}
	}
	
	public void store() {
		BufferedWriter bw=null; 
		try {
			FileWriter fw=new FileWriter("C:\\Users\\user1\\Desktop\\allDB.txt"); //���� ����(����� �ܾ� ����Ϸ���)
			bw=new BufferedWriter(fw);
			
			for(int i=0; i<allDB.size();i++) {
				Word w = allDB.get(i);
				String st = w.getName()+"/"+w.getMean1()+"/"+w.getMean2(); //����� �� �ޱ�
				bw.write(st);         
				bw.newLine();          
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void inputWord() { //���ܾ� ���
		String nWord;
		boolean bool;
		while (true) {
			bool = true;
			System.out.print("����� �ܾ �Է��ϼ��� : ");
			nWord = sc.next().toLowerCase(); 			
			for (int i = 0; i < allDB.size(); i++) {
				if (nWord.equals(allDB.get(i).getName())) {
					System.out.println("�ܾ �ߺ��˴ϴ�.�ٽ� �Է����ּ���");
					bool = false;
					break;
				}

			}
			if(bool) {
				break;
			}
		}
		
		String name = nWord;
		System.out.print("ù��° ���� �Է����ּ��� : ");
		String nMean1 = sc.next();

		
		System.out.print("�ι�° ���� �Է����ּ��� : ");
		String nMean2 = sc.next();
		
		System.out.println(name+"/"+nMean1+"/"+nMean2+"�� �½��ϱ�?");
		System.out.print("�̴�� ����Ͻðڽ��ϱ�?[y/n] : ");
		char ch = sc.next().charAt(0);
		
		switch(ch) {
		case 'y':
			Word w = new Word(name, nMean1, nMean2);
			allDB.add(w);
			store();
			System.out.println("====�ܾ ��ϵǾ����ϴ�====");
			break;
		case 'n':			
			break;
		default:
			System.out.println("�߸��� �Է��Դϴ�. ���Ŀ� ���� �Է����ּ���! ");
		}
		
		
		}

}
