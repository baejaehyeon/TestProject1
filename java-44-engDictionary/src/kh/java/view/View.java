package kh.java.view;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import kh.java.model.Word;

public class View {
	
	//---------메인 메뉴 및 재사용 가능 메소드------------------------------------------------------------------------------------
	Scanner sc = new Scanner(System.in);
	Random r = new Random();
	
	public int main() { //가장 처음 메뉴
		System.out.println("=====영어 단어 사전=====");
		System.out.println("1.단어 검색");
		System.out.println("2.영단어 등록");
		System.out.println("3.테스트 보기");
		System.out.println("4.오답 노트");
		System.out.println("5.단어 수정 및 삭제");
		System.out.println("0.프로그램 종료");
		System.out.print("선택 > ");
		int selMenu = sc.nextInt();
		
		return selMenu;
	}
	
	public String getName(String msg) { // 사용자에게 삭제, 수정 할 단어명을 입력받아 리턴
		System.out.print(msg + " 할 단어 입력 : ");
		String userInput = sc.next();
		sc.nextLine(); // enter buffer 비우기
		return userInput;
	}
	
	
	//---------검색_솔이-------------------------------------------------------------------------------------
	
	public String input() {
		System.out.print("입력 : "); //영단어 또는 뜻 입력
		String inputWord = sc.next();
		return inputWord;
	}
	
	public int searchDictionary() { // 검색_영한검색 or 한영검색
		System.out.println("1.영한검색");
		System.out.println("2.한영검색");
		System.out.println("0.뒤로 가기");
		System.out.print("선택 > ");
		int select = sc.nextInt();
		
		return select;
	}
	

	//---------수정 및 삭제_도형이형-------------------------------------------------------------------------------------
	public int updateMain() { // 사용자에게 보여줄 선택지 메뉴, 문자를 입력했을 경우 Exception throws
		System.out.println("사전 관리 메뉴입니다.");
		System.out.println("1. 단어 수정");
		System.out.println("2. 단어 삭제");
		System.out.println("0. 뒤로 가기");
		System.out.print("선택 > ");

		int select = sc.nextInt();
		sc.nextLine(); // enter buffer 비우기

		return select;
	}

	public String getMean(String msg) { // 사용자에게 수정할 단어 내용을 입력받아 리턴
		System.out.print(msg);
		String mean = sc.nextLine(); //new 뜻을 입력하고 리턴

		return mean;
	}

	public Boolean delWord(String userInput) { // 사용자에게 userInput 단어를 정말 삭제할지 확인하여 Boolean 리턴

		while (true) { // 삭제 여부를 y,n으로 사용자가 답변할때까지 루프

			System.out.print("[" + userInput + "] 단어를 정말 삭제하시겠습니까? [y/n] : ");

			char userAns = sc.next().charAt(0);

			if (userAns == 'y') { // 사용자가 y를 입력할 경우 -> true 리턴
				System.out.println("[" + userInput + "] 단어를 삭제합니다");
				return true;

			} else if (userAns == 'n') { // 사용자가 n을 입력할 경우 -> false 리턴
				System.out.println("단어 삭제를 중단합니다.");
				return false;

			} else { // 사용자가 y나 n 이외의 값을 입력했을 경우
				System.out.println("잘못된 입력입니다.");
			}
		}
	}


	//---------테스트_예진누나-------------------------------------------------------------------------------------
		
	
	public int vocaTest() {				//메뉴선택 받아서 숫자리턴.
		System.out.println("---- 단어 테스트 ----");	
		System.out.println("1.영한 테스트");
		System.out.println("2.한영 테스트");
		System.out.println("0.뒤로가기");
		System.out.print("선택 > ");
		int select = sc.nextInt();
		return select;
	}
	
	public ArrayList<Word> engTest(ArrayList<Word> al) { //테스트 후 failal 또는 null 리턴
		int point=0; //점수 셀 point
		ArrayList<Word> failList = new ArrayList<Word>();  //틀린거 받아서 넘길 임시 어레이리스트 생성.
		System.out.println("---- 영한 테스트 ----");
		System.out.print("문제 갯수를 입력하세요 : ");
		int qNum = sc.nextInt();					//문제갯수
		int question ;								//랜덤으로 인덱스 추출받을 변수.
		System.out.println("테스트를 시작합니다.");
		System.out.println("다음 영단어를 보고 뜻을 입력하세요.");
		sc.nextLine();
		
		//문제 갯수 만큼 만복하는 for문
		for(int i=0; i<qNum; i++) {
			question=r.nextInt(al.size()); 					//랜덤으로 인덱스추출
			System.out.println(al.get(question).getName()); //추출한 인덱스로 모든단어에서 영단어추출해서 보여줌.
			System.out.print("뜻 : ");
			String ans = sc.nextLine();						//뜻 입력받음.
			if(ans.equals(al.get(question).getMean1())||ans.equals(al.get(question).getMean2())){
				System.out.println("정답!");					//"입력받은 답 "이 "문제의 뜻"과 하나라도 같으면
				point++;									//정답 출력하고 점수+1
			} else {
				System.out.println("틀렸습니다ㅠ");
				failList.add(al.get(question));					//맞지않다면 임시리스트에 저장.
			}			
		}//문제 갯수 만큼 만복하는 for문	
		
		System.out.println(((double)point/qNum)*100 + "점 입니다.");
		System.out.print("틀린문제를 오답노트에 저장 하시겠습니까? [y/n] :");
		char ans = sc.next().charAt(0);
		if(ans=='y') {
			return failList;
		}else {
			return null;
		}
		
	}
	
	public ArrayList<Word> korTest(ArrayList<Word> al) {	//모든단어받아서 틀린단어 리턴
		int point=0;
		ArrayList<Word> failList2 = new ArrayList<Word>();  //틀린거 받아서 넘길 임시 어레이리스트 생성.
		
		System.out.println("---- 한영 테스트 ----");
		System.out.print("문제 갯수를 입력하세요 : ");
		int qNum = sc.nextInt();					//문제갯수
		int question ;								//랜덤으로 인덱스 추출받을 변수.
		
		System.out.println("테스트를 시작합니다.");
		System.out.println("다음 뜻을 보고 영어단어를 입력하세요.");
		sc.nextLine();
		
		//문제 갯수 만큼 만복하는 for문
		for(int i=0; i<qNum; i++) {
			question=r.nextInt(al.size()); 					//랜덤으로 인덱스추출
			int rNum=r.nextInt(2);							//뜻 두개중에 뭐꺼낼지 랜덤
			if(rNum==0) {									
				System.out.println(al.get(question).getMean1());	
			}else {
				System.out.println(al.get(question).getMean2());
			}
			
			System.out.print("영어단어 : ");
			String ans = sc.next().toLowerCase();			//영단어 입력			
			
			if(ans.equals(al.get(question).getName())){		
				System.out.println("정답!");					//입력받은값이 이름과 맞으면 정답
				point++;
			} else {
				System.out.println("틀렸습니다ㅠ");
				failList2.add(al.get(question));					//맞지않다면 임시리스트에 저장.			
			}			
		}//문제 갯수 만큼 만복하는 for문	
		
		System.out.println(((double)point/qNum)*100 + "점 입니다.");//점수출력
		System.out.print("틀린문제를 오답노트에 저장 하시겠습니까? [y/n] :");
		
		char ans = sc.next().charAt(0);
		if(ans=='y') {								//y 하면 임시리스트 리턴.
			return failList2;
		}else {
			return null;
		}
	}	

	//---------오답_수민누나-------------------------------------------------------------------------------------
	
	public int failMenu() {
		System.out.println("===== 오답노트 =====");
		System.out.println("1. 오답 보기");
		System.out.println("2. 재시험");
		System.out.println("3. 오답노트 비우기");
		System.out.println("0. 뒤로");
		System.out.print("선택 > ");
		int select = sc.nextInt();
		return select;
	}

	public void readNote() {			//1. 오답보기;  오답 출력 후 출력될 출력문
		System.out.print("조회할 오답 선택 : ");
	
	}

	public void reTest() {
		System.out.print("재시험하시겠습니까[y/n]? : ");
	}

	// ---------단어 등록_지현-------------------------------------------------------------------------------------

	public int  Insertdictionary() {
		System.out.println("1. 단어입력");
		System.out.println("0. 뒤로가기");
		System.out.print("선택 > ");
		int sel=sc.nextInt();
		return sel;
		
		}
	
	public char inputWord() {
		String nWord;
		System.out.println("등록할 단어를 입력하세요");
		nWord = sc.next();
		nWord=nWord.toLowerCase();
		String name = nWord;
		System.out.println("첫번째 뜻을 입력해주세요 ");
		String nMean1 = sc.next();

		String mean1 = nMean1;
		System.out.println("두번째 뜻을 입력해주세요");
		String nMean2 = sc.next();
		String mean2 = nMean2;
		System.out.println(name+"/"+mean1+"/"+mean2+"가 맞습니까?");
		System.out.println("이대로 등록하시겠습니까?[y/n]");
		char ch=sc.next().charAt(0);
		return ch;
		}
}