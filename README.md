# 토닥토닥

<img width="60%" alt="토닥토닥" src="https://user-images.githubusercontent.com/44156173/146647068-d7854d7d-80b7-4dc2-a266-7cf3ab5ef9ce.png">

- ***Skills***   
  HTML, JAVA, Javascript, Oracle, Spring
  
- ***개인/팀***    
  팀 프로젝트    
  
- ***진행 기간***   
  2020년 1월 10일 → 2020년 3월 30일
  
- ***한 줄 소개***   
  비영리 단체를 위한 인사관리, 일정관리, 전자결재 중심의 그룹웨어 시스템        


## 🔗 Link

---

***Source***   

[GitHub - NayoungBae/todakTodak](https://github.com/NayoungBae/todakTodak)

***Video***   

[[비트캠프] 자바 142기 자바기반 웹 오픈소스 개발자 과정 / 팀명 : 토닥토닥 / 프로젝트명 : 비영리단체를 위한 그룹웨어](https://www.youtube.com/watch?v=9d4Os6aTCT0)

<br>

## 🖊️ 요약

---

- 기안서 작성   
<img width="60%" alt="기안서작성" src="https://user-images.githubusercontent.com/44156173/146646652-00a39e43-edf9-457a-9b91-6b94b99a61c0.png">

<br>

- 결재라인 선택
<img width="40%" alt="결재라인 선택" src="https://user-images.githubusercontent.com/44156173/146646677-7eb1ed28-b781-4ffd-8c65-dbf5c5ef8a26.png">

<br>

- 전자문서 목록 조회
<img width="60%" alt="전자문서 목록 조회" src="https://user-images.githubusercontent.com/44156173/146646713-1d54f1ca-a3fe-4e63-add9-b1be4423e1d1.png">

<br>

- 전자문서 상세 보기
<img width="60%" alt="전자문서 상세 보기" src="https://user-images.githubusercontent.com/44156173/146646729-f28606c6-8028-4a26-836a-00de70b9bbc5.png">

<br>

- 사용 대상
    
    업무의 전산화가 필요한 기업
    
- 문제 의식
    
    원활한 업무 수행을 위해 전사 업무 전산화에 대한 필요성을 발견
    
    환경 보호를 위해 업무 중 발생하는 종이를 최소화하는 목적
    
- 제공 서비스
    - 결재 문서 작성 및 기안
    - 결재 라인 선택
    - 결재 문서 목록 조회
    - 결재 문서 상세 조회
    - 문서의 결재 진행(승인, 반려, 대결)

<br>


## 🛠️ 사용 기술 및 라이브러리

- Spring
- HTML, CSS, Javascript, Bootstrap
- JSP, JAVA, Oracle, Mybatis
- Tomcat
- Github

<br>

## 🔨 맡은 역할

- 요구사항 정의서, 테이블 정의서, 요건 정의서 작성
- 전자결재 시스템 개발
- 전자결재 시스템에 대한 페이지 디자인

<br>

## 🤔 어려웠던 점

- 데이터베이스 테이블 구성
    
    어떤 테이블이 필요하고 테이블 안의 어떤 컬럼이 포함되는지 생각하는 것이 어려웠습니다.    
    블로그에 있는 ERD를 참고하여 테이블을 구성하고 필요에 맞게 컬럼을 수정하였습니다.

<br>

- 기능에 대한 실행될 쿼리 작성   
    승인이나 반려, 대결의 기능을 실행할 때 실행되는 쿼리가 많았습니다.   
    테이블에 데이터를 INSERT하거나 UPDATE할 순서를 구성하는 것이 어려웠습니다.    
    전자결재 시스템의 프로세스에 대해 자료를 조사하여 해결하였습니다.

<br>

## 💡 깨달은 점

- JAVA에서 함수를 정의할 때 규칙이 없어 어떤 기능을 수행하는 함수인지 명확히 판단할 수 없었습니다.    
  그래서 명명 규칙을 정한 뒤 함수를 다시 정의하였습니다. 이로 인해 규칙을 정하는 것에 대해 필요성을 느꼈습니다.   

<br>

- 코드를 작성할 때, 주석으로 코드의 설명이 필요하다는 것을 깨달았습니다.    
  다른 사람이 개발한 코드를 볼 때 작성한 의도를 쉽게 파악할 수 있다는 것을 깨달았습니다.   

<br><br><br>

## ❗ 자세한 기능 설명   

  - 결재 문서 작성 및 기안   
      - 기안서, 품의서, 휴가 신청서 작성 시 문서 번호, 결재 번호, 사원 번호, 작성자, 직급 정보를 자동으로 채워줍니다.   
      - 기안서와 휴가 신청서는 HTML 태그를 사용하여 내용을 입력하고, 품의서는 파일 업로드·다운로드 방식을 사용하여 문서 편집 프로그램에서 작성한 파일을 첨부합니다.   

<br>

  - 결재 라인 선택   
      - 조회하고 싶은 부서를 선택하면 해당 부서에 소속되어있는 직원을 조회하여 보여줍니다.   
      - 결재 문서 유형에 따라 지정된 직급을 선택하지 않으면 경고 창을 띄워 알립니다.   

<br>

  - 결재 문서 목록   
      - 작성자, 제목, 결재 기간, 분류, 결재 상태에 따라 문서를 검색합니다.   
      - 검색 시 Full Calendar API를 사용하여 날짜를 지정합니다.   
      - 제목을 클릭하면 문서의 내용과 결재 진행 현황을 확인할 수 있습니다.   

<br>

  - 결재 문서 상세 조회    
      - 내용을 확인한 뒤 결재 문서 상세 조회 페이지에서 승인, 반려, 대결에 대한 결재를 진행합니다.   
          1. 승인   
              결재 문서 작성 시 설정해준 결재 라인의 다음 사람에게 결재 권한을 전달합니다. 결재 문서 목록에서 진행 상태는 '미결'에서 '진행중'으로 변경됩니다.   
                
          2. 반려     
              결재 진행을 더 이상 할 수 없으며, 결재 상태는 '반려'로 변경됩니다.   
                
          3. 대결   
              현재 결재자와 같은 본부에 존재하고 현재 결재자의 직급보다 한 단계 낮은 임직원에게 결재 권한을 전달합니다.   
