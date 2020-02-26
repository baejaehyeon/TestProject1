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

	// ---------재사용 가능 메소드 및 전역 멤버-------------------------------------------------------------------------------------
	Word word = new Word(); // 테스트용 객체 생성, 삭제되어야함
	View view = new View(); // 테스트용 객체 생성, 삭제되어야함
	ArrayList<Word> allDB = new ArrayList<Word>(); // 테스트용 객체 생성, 삭제되어야함
	BufferedReader br = null; // 테스트용 객체 생성, 삭제되어야함
	BufferedWriter bw = null; // 테스트용 객체 생성, 삭제되어야함
	BufferedReader failbr = null; // 테스트용 객체 생성, 삭제되어야함
	BufferedReader failbr2 = null;
	Random r = new Random();
	Scanner sc = new Scanner(System.in);
	ArrayList<String> failName = new ArrayList<String>(); // 오답파일에 뜻 제외한 영단어를 저장할 배열
	ArrayList<Word> failDB = new ArrayList<Word>(); // 오답 영단어 + 뜻 두개 저장할 배열

	public void controlMain() { // 첫 메뉴

		while (true) {
			int select = view.main();
			switch (select) {
			case 1: // 단어 검색
				search();
				break;
			case 2:// 영단어 등록
				Insertdictionary();
				break;
			case 3:// 테스트
				testMenu();
				break;
			case 4:// 오답 노트
				failMenu();
				break;
			case 5:// 단어 수정 및 삭제
				updateOrDelete();
				break;
			case 0:
				return;
			}
		}
	}

	public SearchWord() { // 단어장 전체 읽어와 리스트에 추가
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
			
			fr2 = new FileReader("C:\\Users\\user1\\Desktop\\failDB.txt"); // 오답파일 불러옴
			failbr2 = new BufferedReader(fr2);

			while (true) {
				
				String toke = failbr.readLine();
				String str1 = failbr2.readLine();
				
				if (toke == null || str1 == null) { // 더이상 불러올 텍스트 없을 때
					break;
				}
				
				
				// 단어+뜻 저장
				StringTokenizer st2 = new StringTokenizer(toke, "/");				
				failDB.add(new Word(st2.nextToken(), st2.nextToken(), st2.nextToken()));
						
				StringTokenizer sT = new StringTokenizer(str1, "/"); // 오답 한줄씩 토큰으로 나눠주기
				String st = sT.nextToken(); // 첫번째 토큰만 추출 - 뜻 제외한 단어만
				failName.add(st); // 단어만 failName 배열에 삽입

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

	public int searchIndex(String userInput) { // 유저가 입력한 단어 단어장에 있는지 검사 및 결과 리턴 !!!!!!!재사용 가능!!!!!!!!!

		for (int i = 0; i < allDB.size(); i++) {
			if (allDB.get(i).getName().equals(userInput)) { // i번째 객체의 getName값이 userInput과 동일할때
				return i; // i값 리턴
			}
		}
		return -1; // 해당되는 단어를 찾지 못할경우 -1 리턴
	}

	public void saveDB() { // 수정, 삭제가 끝날시 진행할 DB 저장 메소드 !!!!!!!재사용 가능!!!!!!!!!

		try {
			bw = new BufferedWriter(new FileWriter("C:\\Users\\user1\\Desktop\\allDB.txt")); // allDB.txt에 저장하는 BufferWriter 생성
			int count = 0;
			for (Word w : allDB) { // allDB 내의 모든 Word 객체 검색
				if(count!=0) {
					bw.newLine();
					
				}
				// 검색된 Word 객체의 이름, mean1, mean2를 구분자 / 를 기준으로 FileWrite
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

	// ---------수정 및 삭제_도형이형-------------------------------------------------------------------------------------

	public void updateOrDelete() { // 유저에게 메뉴화면을 출력하고 입력값을 리턴받아 진행

		while (true) {
			int slt = view.updateMain();

			switch (slt) {
			case 1: // 단어 수정
				modifyWord();
				break;
			case 2: // 단어 삭제
				delWord();
				break;
			case 0:
				System.out.println("이전 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("0~2 사이의 숫자를 입력하세요.");
			}
		}
	}

	public void modifyWord() { // 단어 수정 후 리스트에 저장
		String userInput = view.getName("수정");

		int index = searchIndex(userInput);

		if (index == -1) {
			System.out.println("찾으시는 단어가 없습니다.");

		} else {
			String currentMean1 = allDB.get(index).getMean1(); // 전체 단어가 있는 리스트의 뜻1과 뜻 2
			String currentMean2 = allDB.get(index).getMean2();

			System.out.println("[" + userInput + "] 단어를 수정합니다.");
			String mean1 = view.getMean("현재 내용 : " + currentMean1 + "\t수정  : "); // 단어의 첫번째 뜻

			System.out.println("만약 입력을 중단하시려면 x 를 입력하세요 ↓");
			String mean2 = view.getMean("현재 내용 : " + currentMean2 + "\t수정  : ");

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

	public void delWord() { // 단어 삭제 메소드 !!!!!!!재사용 가능!!!!!!!!!
		String userInput = view.getName("삭제"); // 삭제할 단어

		int index = searchIndex(userInput);

		if (index == -1) { // 입력한 단어가 DB에 존재하지 않을때
			System.out.println("찾으시는 단어가 없습니다.");
			return; // 메소드 종료

		} else if (view.delWord(userInput)) { // 유저가 y를 입력해 단어삭제를 진행하는 경우 = true가 리턴될 때
			allDB.remove(index);
			saveDB();

		} else { // 유저가 n을 입력하여 단어삭제를 취소한 경우
			return;
		}
	}

	// ---------검색_솔이-------------------------------------------------------------------------------------
	public void search() {

		while (true) {
			int select = view.searchDictionary(); // view에서 영한 검색할지 한영 검색할지
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

	public void searchEng() { // 영단어로 뜻 검색
		// 입력받는 화면인 View클래스의 객체 만들기
		String search = view.input(); // 검색할 단어 입력받기

		for (int i = 0; i < search.length(); i++) {// 영단어 이외의 문자 입력 시
			if (search.charAt(i) < 'A' || search.charAt(i) > 'z') {
				System.out.println("영어만 입력할 수 있습니다.");
				return;
			}
		}

		search = search.toLowerCase();
		boolean find = false; // 단어장에 없는 단어를 검색했을 때 없는단어라고 말해주기위함
		for (int i = 0; i < allDB.size(); i++) {
			if (allDB.get(i).getName().equals(search)) {// 단어장의 단어들 중 내가 입력한것과 일치하는것이 있으면
				System.out.println("단어 : " + allDB.get(i).getName() + "\n첫번째 뜻 : " + allDB.get(i).getMean1()
						+ "\n두번째 뜻 : " + allDB.get(i).getMean2());
				find = true;
			}
		}
		if (find == false) {
			System.out.println("단어장에 없는 단어입니다!");
		}
	}

	public void searchKor() { // 뜻으로 영단어 검색

		String search = view.input(); // 검색할 단어 입력받기
		boolean find = false; // 단어장에 없는 단어를 검색했을 때 없는단어라고 말해주기위함
		for (int i = 0; i < allDB.size(); i++) {
			// 내가 검색한 뜻이 모든 단어의 mean1 또는 mean2 중에 일치하는게 있으면
			if (allDB.get(i).getMean1().equals(search) || allDB.get(i).getMean2().equals(search)) {
				// 같은 뜻을 가진 단어 모두 출력
				System.out.println("단어 : " + allDB.get(i).getName() + "\n첫번째 뜻 : " + allDB.get(i).getMean1()
						+ "\n두번째 뜻 : " + allDB.get(i).getMean2());
				find = true;
			}
		}
		if (find == false) {
			System.out.println("[" + search + "]의 뜻을 가진 단어가 없습니다!");
		}
	}

	// ---------테스트_예진누나-------------------------------------------------------------------------------------

	public void testMenu() {

		try {
			FileReader fr2 = new FileReader("C:\\Users\\user1\\Desktop\\failDB.txt"); // 오답노트 읽어옴
			failbr = new BufferedReader(fr2);

			// failDB데이터 읽어옴 (기존에 있던 오답도 항상 오답노트에 들어있어야하니까)
			while (true) {
				String str = failbr.readLine();
				if (str == null)
					break; // 단어 저장될거 없으면 break;
				StringTokenizer sT = new StringTokenizer(str, "/");
				Word word = new Word(sT.nextToken(), sT.nextToken(), sT.nextToken());
				failDB.add(word); // 오답단어 리스트에 저장.
			}

			// 게임선택
			while (true) {
				int select = view.vocaTest(); // view에서 메뉴선택 입력받음.
				switch (select) {
				case 1: // 영한테스트라면
					failDB = view.engTest(allDB); // 모든리스트를 주고 틀린리스트를 받는다.
					System.out.println(failDB.size());
//					if (!failList.isEmpty()) { // 받은리스트에 정보가 있다면
//						for (Word w : failList) { // 받은리스트에 있는 Word객체를 failDB 에저장.
//							System.out.println("test");
//							failDB.add(w);
//						}
//					}
					break;
				case 2: // 한영테스트라면
					ArrayList<Word> failList2 = view.korTest(allDB);// 모든리스트를 주고 틀린리스트를 받는다.
					if (!failList2.isEmpty()) { // 받은리스트에 정보가 있다면
						for (Word w : failList2) { // 받은리스트에 있는 Word객체를 failDB 에저장.
							failDB.add(w);
						}
					
					}
					break;
				case 0:
					output(); // 뒤로가기를 누르면 failDB가 출력됨.(오답노트)
//					failDB.clear();
					return;
				}
			} // 테스트 첫화면 반복 while
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
			FileWriter fw = new FileWriter("C:\\Users\\user1\\Desktop\\failDB.txt"); // 오답노트 생성
			bw = new BufferedWriter(fw);
			for (Word w : failDB) { // failDB리스트에 있는 Word객체를
				bw.write(w.toString()); // 한줄씩 출력
				bw.newLine(); // 개행
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

	// ---------오답_수민누나-------------------------------------------------------------------------------------

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
	// ---------오답_수민누나-------------------------------------------------------------------------------------
	public void reTest() { // 재시험 보고 정답 시 오답노트에서 삭제시키기
		
		view.reTest();
		char agree = sc.next().toLowerCase().charAt(0); // 재시험 여부 입력
		sc.nextLine();
		switch (agree) {
		case 'n': // 재시험 원치 않을 시 종료
			System.out.println("이전 메뉴로 돌아갑니다.\n");
			return;
		case 'y': // 재시험 원할 시
			if(failDB.size()==0) {
				System.out.println("오답노트에 단어가 없습니다!");
				return;
			}
			System.out.println("테스트를 시작합니다. 다음 영단어를 보고 뜻을 입력하세요.");			
			int size = failName.size(); //3
			for (int i = 0; i < size; i++) {//0.1.2
				
				System.out.println(failName.get(i)); // 오답(뜻 제외한) 영단어에 인덱스 붙여서 출력
				System.out.print("뜻 : ");
				String ans = sc.nextLine(); // 뜻 입력받음.
		
				if (failDB.get(i).getMean1().equals(ans) || failDB.get(i).getMean2().equals(ans)) {
					System.out.println("정답!"); // '입력받은 답 '이 '문제의 뜻'과 하나라도 같으면 "정답"
					failName.remove(i); // 단어 들어있는 배열에서 제거해주기
					failDB.remove(i); // 위와 상동
					size--;
					
				} else {
					System.out.println("틀렸습니다!");
				}

			}
		}
	}

	public void clearNote() {
		BufferedReader br = null;		
		BufferedWriter bw = null;
		
		// 오답노트 비우기 위해 텍스트파일을 한 줄씩 배열에 넣어주기
		try {
			FileReader fr = new FileReader("C:\\Users\\user1\\Desktop\\failDB.txt"); // 오답전체파일 불러오기
			br = new BufferedReader(fr);
			while (true) {
				String str = br.readLine(); // 텍스트파일 1줄씩 읽어옴
				if (str == null) { // 더이상 불러올 텍스트 없을 시 while문 탈출
					break;
				}
				StringTokenizer st = new StringTokenizer(str, "/");
				String name = st.nextToken();
				String mean1 = st.nextToken();
				String mean2 = st.nextToken();
				failDB.add(new Word(name, mean1, mean2)); // 오답의 name+mean1+mean2 failDB에 넣어주기
			}
			// 오답 불러올 데이터 clear처리해서 비우기
			failName.clear();
			failDB.clear();
			
			FileWriter fw = new FileWriter("C:\\Users\\user1\\Desktop\\failDB.txt"); // 오답노트 생성
			bw = new BufferedWriter(fw);
			for (Word w : failDB) { // failDB리스트에 있는 Word객체를
				bw.write(w.toString()); // 한줄씩 출력
				bw.newLine(); // 개행
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

	public void readNote() { // 오답노트 넘버링 보고 선택해서 뜻까지 조회	
		System.out.println(failName.size());
		if (failName.size() == 0) { // 보여줄 오답이 없을 시
			System.out.println("오답노트에 담긴 단어가 없습니다.\n");
		} else { // failName(뜻 제외한 단어만 들어있는) 배열에 넣은 오답name 일련화해서 출력
			for (int i = 0; i < failName.size(); i++) {
				System.out.println((i + 1) + ". " + (failName.get(i)));
			}

			view.readNote(); // view클래스의 readNote 메소드 실행
			int select = sc.nextInt(); // 조회하고 싶은 오답 번호 선택
			System.out.println("단어 : " + failDB.get(select - 1).getName() + "\n첫번째 뜻 : "
					+ failDB.get(select - 1).getMean1() + "\n두번째 뜻 : " + failDB.get(select - 1).getMean2()); // 뜻 포함한 영단어
																											// 들어있는 배열에서
																											// 선택한 오답 출력
			System.out.println();
		}

	}

	// ---------영단어 등록_지현-------------------------------------------------------------------------------------
	// ---------단어 등록_지현-------------------------------------------------------------------------------------
	
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
			FileWriter fw=new FileWriter("C:\\Users\\user1\\Desktop\\allDB.txt"); //파일 쓰기(등록한 단어 사용하려고)
			bw=new BufferedWriter(fw);
			
			for(int i=0; i<allDB.size();i++) {
				Word w = allDB.get(i);
				String st = w.getName()+"/"+w.getMean1()+"/"+w.getMean2(); //등록할 거 받기
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
	public void inputWord() { //영단어 등록
		String nWord;
		boolean bool;
		while (true) {
			bool = true;
			System.out.print("등록할 단어를 입력하세요 : ");
			nWord = sc.next().toLowerCase(); 			
			for (int i = 0; i < allDB.size(); i++) {
				if (nWord.equals(allDB.get(i).getName())) {
					System.out.println("단어가 중복됩니다.다시 입력해주세요");
					bool = false;
					break;
				}

			}
			if(bool) {
				break;
			}
		}
		
		String name = nWord;
		System.out.print("첫번째 뜻을 입력해주세요 : ");
		String nMean1 = sc.next();

		
		System.out.print("두번째 뜻을 입력해주세요 : ");
		String nMean2 = sc.next();
		
		System.out.println(name+"/"+nMean1+"/"+nMean2+"가 맞습니까?");
		System.out.print("이대로 등록하시겠습니까?[y/n] : ");
		char ch = sc.next().charAt(0);
		
		switch(ch) {
		case 'y':
			Word w = new Word(name, nMean1, nMean2);
			allDB.add(w);
			store();
			System.out.println("====단어가 등록되었습니다====");
			break;
		case 'n':			
			break;
		default:
			System.out.println("잘못된 입력입니다. 형식에 맞춰 입력해주세요! ");
		}
		
		
		}

}
