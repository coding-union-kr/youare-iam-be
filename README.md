# 💌 프로젝트 소개

### 프로젝트 명

**너는, 나는**

### 기획 의도

서로 좋아하거나 사랑하는 사이라도 **직접적으로 말하기 어려웠던 점**은 없나요?

혹은 **상대에 대해 더 알아가고 싶었던 적**이 있었나요?

가장 가까운 사이라도 이야기하지 못했던, 혹은 궁금했던 점들이 있었나요?

어떻게 하면 재밌고 더 편하게 질문할 수 있을지 고민하다가 `너는, 나는` 을 만들게 되었습니다.

<br/>

### 프로젝트 설명

_너는 어떻게 생각해? 나는…_

질문을 공유해서 공유된 질문에 대한 답변으로 서로에 대해 더 알아갈 수 있는 서비스입니다.

주요 사용자 타겟은 연인입니다.

- 사용자가 `너는, 나는` 서비스에 접속하여 로그인 후 나누고 싶은 질문을 선택 후 초대 링크를 통해 해당 질문을 연인에게 보냅니다.
- 초대 링크를 받은 상대가 해당 질문에 답변을 작성하고 로그인을 하면 초대가 수락되며, 질문에 대한 서로의 답변을 확인할 수 있습니다.
- 질문은 하루에 한 번만 선택할 수 있고, 질문에 대한 답변은 양쪽 모두가 답변을 작성해야만 확인이 가능합니다.
- 질문 리스트는 DB에 저장해 둔 프리셋 질문들을 사용합니다.

<br/>

### 나중에 추가하고 싶은 기능

작업을 하면서 이런 기능들이 있으면 사용자들이 더 편하게 서비스를 이용할 수 있겠다고 생각되었던 것들을 정리해 보았습니다.

1. 질문 현황 페이지

2. 답변 수정

3. 커스텀 질문 등록

4. 알림 기능

<br/>

# 프로젝트 주요 기능 소개

<table align="center">
  <tr align="center">
    <td>로그인</td>
    <td>동작</td>
  </tr>
  <tr>
    <td align="center">
      <b>메인 페이지에서 ‘카카오 로그인’ 버튼을 누르면 카카오 소셜 로그인 페이지로 </b>
      <br/>
      <b>이동하여 카카오 로그인을 할 수 있습니다.</b>
    </td>
    <td align="center">
      <img src='https://github.com/coding-union-kr/youare-iam-fe/blob/dev/docs/login.gif?raw=true' width='40%'>
    </td>
  </tr>
  <tr align="center">
    <td>사용자 초대</td>
    <td>동작</td>
  </tr>
  <tr>
    <td align="center">
     <b>상대방에게 묻고 싶은 질문을 선택하고, 카카오톡 친구에게 공유하여 초대할 수 있습니다.</b>
    </td>
    <td align="center">   
      <img src='https://github.com/coding-union-kr/youare-iam-fe/blob/dev/docs/send-invitation.gif' width='40%'>
    </td>
    </td>
  </tr>
  <tr align="center">
    <td>초대 수락</td>
    <td>동작</td>
  </tr>
  <tr>
    <td align="center">
     <b>- 초대받은 사용자는 카카오톡으로 공유된 링크에 접속하여 질문에 대한 답변을 작성할 수 있습니다.</b>
      <br/>
      <br/>
     <b>- 초대받은 사용자가 답변을 등록하려면 로그인을 해야 하며, 답변 작성 후 등록시 카카오 로그인 페이지로 리다이렉트됩니다.</b>
      <br/>
      <br/>
     <b>- 로그인에 성공하면 로그인 전에 작성한 답변을 등록할 수 있으며, 커플이 맺어짐과 동시에 서로의 답변을 확인할 수 있습니다.</b>
    </td>
    <td align="center">   
      <img src='https://github.com/coding-union-kr/youare-iam-be/assets/86812098/9acbca85-058b-43c6-b203-236b169a3df2' width='40%'>
    </td>
    </td>
  </tr>
  <tr align="center">
    <td>질문 선택</td>
    <td>동작</td>
  </tr>
  <tr align="center">
    <td>
     <b>커플이 답변할 질문을 질문 선택 페이지에서 매일 한개씩 선택할 수 있습니다.</b>
      <br/>
      <br/>
     <b>질문을 선택하면 대화 상세 페이지로 이동하며, 선택한 질문이 하단에 추가된 것을</b>
      <br/>
      <b>확인할 수 있습니다.</b>
    </td>
    <td align="center">
      <img src='https://github.com/coding-union-kr/youare-iam-fe/blob/dev/docs/question.gif?raw=true' width='40%'>
    </td>
  </tr>
  <tr align="center">
    <td>답변 등록</td>
    <td>동작</td>
  </tr>
  <tr align="center">
    <td>
     <b>대화 상세 페이지에서 선택한 질문을 클릭하여 답변을 작성할 수 있습니다.</b>
      <br/>
      <br/>
     <b>두 사람 모두 작성한 후 답변을 확인할 수 있으며, 둘 중 한 명만 답변했을 경우</b>
      <br/>
      <b>안내 메시지가 오버레이되며 답변을 확인할 수 없습니다.</b>
    </td>
    <td align="center">
      <img src='https://github.com/coding-union-kr/youare-iam-fe/blob/dev/docs/answer.gif?raw=true' width='40%'>
    </td>
  </tr>
  <tr align="center">
    <td>대화 상세 페이지</td>
    <td>동작</td>
  </tr>
  <tr align="center">
    <td>
     <b>선택한 질문과 이에 대한 답변은 채팅 형식으로 대화 상세 페이지에 누적됩니다.</b>
      <br/>
      <br/>
     <b>로그인한 사용자의 답변은 오른편에, 상대방의 답변은 왼편에 배치됩니다.</b>
      <br/>
      <br/>
      <b>최신 질문일수록 하단에서 확인할 수 있으며,</b>
      <br/>
      <b>오래된 질문은 스크롤을 위로 올려 확인할 수 있습니다.</b>
    </td>
    <td align="center">
      <img src='https://github.com/coding-union-kr/youare-iam-be/assets/86812098/ab0d1888-40f2-4843-a3fa-62e09857b75a' width='40%'>
    </td>
  </tr>
</table>

<br/>

# 🚀 프로젝트 실행 방법

### 요구사항

- Java 17
- Docker
- MySQL 8.0

### 프로젝트 클론

```bash
git clone https://github.com/coding-union-kr/youare-iam-be.git
```

### 프로젝트 루트 폴더에 `.env` 파일 작성

```jsx
DB_HOSTNAME=
DB_NAME=
DB_PASSWORD=
DB_USERNAME=
ENCRYPTION_IV=
ENCRYPTION_SECRET_KEY=
JWT_SECRET=
OAUTH_CLIENT_ID=
OAUTH_CLIENT_SECRET=
OAUTH_REDIRECT_URI=
```

### 프로젝트 실행

- 작은 프로젝트라서 `prod`와 `local`환경으로만 구성

```bash
./gradlew build && java -jar -Dspring.profiles.active=local ./common/build/libs/common-0.0.1-SNAPSHOT.jar
```

---

## 5. 아키텍처

<img src="https://github.com/coding-union-kr/youare-iam-be/assets/86812098/675b74bd-7d21-4948-8003-9bc38edbe5c4" width="700px"/>


# 🛠️ 백엔드 기술 스택
### 기술 스택💙
- JAVA 17                   
- Spring boot 3.2.0         
- MySQL 8.0.33              
- Querydsl 5.0.0            
- JPA                      
- JWT 0.11.2
- Oauth 2.0

### Dev Ops💚
- Elastic Beanstalk
- Amazon RDS
- Amazon EC2
- AWS
- Github Actions

### 사용한 Tools🧡
- Github
- Swagger 3.0
- IntelliJ
- DataGrip


<br/>
<hr/>
<br/>

# 😊프로젝트 진행을 위한 노력😊
저희는 3주간의 기획 기간과 3주간의 개발 기간을 합쳐 총 6주차에 MVP를 출시하겠다는 목표를 세웠습니다. 
<br/>
그러나 6주차에 프론트엔드는 MVP 기능 중 초대하기와 무한스크롤 기능, 백엔드는 인증과 배포 구현이 완료되지 않은 상태였습니다. 
<br/>
또한 기존의 MVP 목표에 리프레시 토큰 구현이 추가되었고 테스트를 하며 서비스를 더 보완하는 시간을 가졌습니다. 5주 간의 추가 개발 기간을 가진 후 11주차에 릴리즈를 하게 되었습니다.

1. 그라운드 룰📃
  - 팀 일정 관리 도구🛠
      - 프로젝트 기간 동안 일정 관리를 위해서 노션(Notion) 캘린더, 슬랙(Slack)을 이용했습니다.
      - 회의 일정, 작업 진행 상황 및 마일스톤을 공유해서 팀원 간 진행 상황을 투명하게 공유할 수 있도록 했습니다.
  - 주간 스크럼 및 코어 타임⏰
      - 매주 4회, 오후 1시부터 6시까지 스크럼 회의와 코어타임을 진행했습니다.
      - 스크럼 회의에서는 각 팀원이 현재 진행 중인 작업과 그 진행 상황, 앞으로 해결해야할 계획을 공유했습니다.
      - 코어타임에는 프론트엔드와 백엔드 팀이 각각 나뉘어 작업 목표를 설정하고, 이를 달성하기 위해서 집중적으로 협업했습니다.
      - 또한, 코어타임 중에는 화면 공유를 통해 서로의 작업 과정을 실시간으로 확인하고, 필요한 경우 페어 프로그래밍을 진행했습니다.
  - 정기 회고 회의📝
      - 매주 일요일에 주간 회고 회의를 진행했습니다. 이 회의에서는 팀원들의 회고를 공유하면서 한 주간의 팀 운영에 대한 피드백을 진행했습니다.
      - 회의 결과를 차주의 목표에 반영했습니다.
  - 마일스톤 일정 조정✅
      - 프로젝트 진행 중 일부 기능 개발에 예상보다 더 많은 시간이 소요된 경우에는, 회의를 통해서 마일스톤 일정을 조정하고, 추가적인 리소스를 해당 작업에 할당해서 프로젝트를 완료할 수 있었습니다.
<br/>

2. 회고를 통한 팀원들의 성장(작성 예정)
   
<br/>

3. 기능 우선순위💈
  - 첫 기획 완료 후 필수 기능과 추가 기능이 무엇인지 정의하고 나눠서 MVP 목표를 설정했습니다.
  - MVP 목표 중에서도 중요도로 정리를 해서 소셜 로그인, 질문 선택 후 등록, 답변 등록, 질문과 답변 상세 조회 를 핵심 기능으로 설정했습니다.
    - 핵심 기능으로 설정한 이유🧐
      - 소셜 로그인 : 커플을 맺는 플로우가 확립되지 않았기 때문에 핵심 기능 바로 다음 우선순위인 초대 관련 기능을 구현하기 위해선 소셜 로그인이 구현되어야 했기 때문입니다.
      - 질문 선택 후 등록, 답변 등록, 질문과 답변 상세 조회 : 이 프로젝트의 핵심이 대화의 포문을 열 수 있는 질문을 선택해서 서로 답변을 하고 확인하는 것이기 때문에 해당 기능들을 핵심 기능으로 설정했습니다.
  - MVP 목표 중에서는 핵심 기능을 1순위로 두고 먼저 작업하였으며 핵심 기능 구현 후 작업이 완료된 기능을 보완하거나 그 기능과 연관된 기능을 다음 우선순위로 결정하였습니다.
  - 이후에는 프론트엔드의 흐름에 따라 필요한 기능을 먼저 작업하거나 팀 회의를 통해서 서비스 흐름의 방향이 수정되거나 추가 됐을 때 필요한 기능을 0순위로 잡아서 작업했습니다.
    
<br/>

4. 백엔드 업무 분담 기준🎈
- 백엔드 업무를 크게 세 가지로 나눌 수 있습니다. => '로그인', '배포', '기능 API'
- 업무를 나눌 때, 저희는 각자의 도전 과제와 가장 하고 싶은 일을 중심으로 고려했어요. => 쪙이: sns 로그인, 감티: 배포
- 그래서 '로그인'과 '배포' 업무를 먼저 분담하고, '기능 API'는 각자가 나눠서 맡았습니다.
- 그리고 추후에 로그아웃 및 리프레쉬 토큰 기능을 구현해야 했는데, 각자 해당 업무에 투자할 수 있는 시간이 달라서 해당 업무에 더 많은 시간을 투자할 수 있는 사람이 맡게 되었습니다.

<br/>

# 🤝 팀 소개

### ❣️팀 소개

**멋**진 **개**발자, 멋개!

저희 팀 이름은 ‘멋개’ 입니다.

선후배 개발자 분들에게 선한 영향력을 줄 수 있는 멋진 개발자가 되자는 의미로 ‘멋개’ 라고 지었습니다.

### ❣️팀원 소개

<table align="center" style="width: 100%;">
    <tr align="center">
        <td style="width: 50%;">
            <b>🧡김미정(쪙이)🧡</b>
            <br/>
            <b style='font-size:12px'>백엔드</b>
            <br/>
            <img src='https://github.com/coding-union-kr/youare-iam-fe/blob/dev/docs/jjung.jpg?raw=true' height='120px'>
            <br/>
            <a href="https://github.com/meezzui">https://github.com/meezzui</a>    
        </td>
        <td style="width: 50%;">
            <b>💛전다빈(감티)💛</b>
            <br/>
            <b style='font-size:12px'>백엔드</b>
            <br/>
            <img src='https://github.com/coding-union-kr/youare-iam-fe/blob/dev/docs/gamti.jpg?raw=true' width='120px'>
            <br/>
            <a href="https://github.com/samjan29">https://github.com/samjan29</a>
        </td>
        <td style="width: 50%;">
            <b>🤎이슬(밤밤)🤎</b>
            <br/>
            <b style='font-size:12px'>프론트</b>
            <br/>
            <img src='https://github.com/coding-union-kr/youare-iam-fe/blob/dev/docs/bambam.png?raw=true' width='120px'>
            <br/>
            <a href="https://github.com/seul-dev">https://github.com/seul-dev</a>
        </td>
        <td style="width: 50%;">
            <b>💙이지선(비얌)💙</b>
            <br/>
            <b style='font-size:12px'>프론트</b>
            <br/>
            <img src='https://github.com/coding-union-kr/youare-iam-fe/blob/dev/docs/biyam.jpg?raw=true' width='120px'>
            <br/>
            <a href="https://github.com/biyamn">https://github.com/biyamn</a>
        </td>
    </tr>
    <tr align="center">
        <td style="width: 50%;">
            한 번 시작한 일은 절대 포기하지 않아요😊
            <br/>
            <b>Springboot</b>, <b>Mybatis</b>, <b>Mysql</b>, <b>JPA</b> 경험이 있으며 현재 JPA를 공부 중 입니다!!
            <br/>
            <br/>
            <b>[담당기능]</b>
            <br/>
            - sns 로그인
            <br/>
            - 사용자 초대 관련 API
        </td>
        <td style="width: 50%;">
            개발을 좋아하고 문제 해결이 재밌어요!
            <br />
            <br/>
            <b>[담당기능]</b>
            <br/>
            - Refresh token
            <br/>
            - 로그아웃
            <br/>
            - 질문, 답변 관련 API
            <br/>
            - 배포
        </td>
        <td style="width: 50%;">
            컴포넌트화, 모듈화, 클래스화를 좋아합니다.
            <br/>
            <br/>
            <b>[담당기능]</b>
            <br/>
            - 온보딩 ui 구현
            <br/>
            - 카카오 소셜 로그인 
            <br/>
            - 레이아웃 구현
        </td>
        <td style="width: 50%; word-break: keep-all;">
            새롭게 배우고 코드에 적용해보는 것을 좋아해요.
            <br/>
            <br/>
            <b>[담당기능]</b>
            <br/>
            - 무한 스크롤
            <br/>
            - 페이지 라우팅
            <br/>
            - 공통 컴포넌트 구현
            <br/>
            - 배포 자동화
        </td>
    </tr>
</table>
