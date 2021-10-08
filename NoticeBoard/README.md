# NoticeBoard

## SpringBoot를 활용한 게시판 만들기

⇒ 기존에 구현했던 CRUD기능이 있던 기본 게시판에 Security를 사용하여 로그인,회원가입 추가, OAuth2 카카오로그인 구현

⇒ 게시글 상세보기 페이지에 댓글기능 추가

⇒ JUnit을 이용한 테스트 기능 추가

⇒ 예외처리 

## 와이어프레임

회원가입,로그인,댓글기능만 추가되고 UI변화가 적고 UI적인 부분은 크게 중요하지 않은 프로젝트이므로 생략

## 구현 조건 리스트

- [x]  회원가입, 로그인페이지 추가
    - [x]  유저 필드는 id(자동증가), username(최소3자이상, 알파벳대소문자, 숫자로 구성,유니크), password(최소4자이상, username과 같은 값이 포함된 경우 실패) - 비밀번호는 암호화해서 저장, role(admin,user역할부여,Enum사용) - `validation 사용`
    - [x]  회원가입- username중복확인(사용자에게 "중복된 닉네임입니다." 메세지출력),password/password확인 일치여부확인 - `자바스크립트 사용하지 말고 전부 서버에서 처리해보기` 인데 중복확인은 자바스크립트 사용...
    - [x]  회원가입,로그인,게시글목록,게시글상세조회 페이지를 제외한 나머지는 로그인 한 경우만 볼 수 있도록 하기
    - [x]  로그인한 유저가 로그인,회원가입페이지에 접근할 경우 "이미 로그인이 되어있습니다." 메세지 출력 후 전체 게시글 목록 조회 페이지로 이동시키기
- [x]  OAuth2 카카오로그인 구현하기
- [x]  게시글 상세 페이지에 댓글 폼 추가 - 댓글 작성란(버튼추가), 목록 순서로 만들기
    - [x]  모든사람이 댓글을 볼 수는 있지만 작성은 로그인한 유저만 가능
    - [x]  로그인하지 않은 사람이 댓글작성버튼 누를시 "로그인이 필요한 기능입니다." 메세지 출력 후 로그인페이지로 이동 시키기
    - [x]  댓글 작성란이 비워져 있으면 "댓글 내용을 입력해주세요"라는 메세지 띄우기
    - [x]  댓글 작성은 비동기식으로 시도  - `jquery의 before,after,append,prepend 와 같은 동적 메서드 사용해서 작성시 가장 상단에 최신글로 보일 수 있도록 만들기, 작성한 댓글도 id와 함수에 임의의 값 부여해서 수정,삭제 시 비동기식으로 참조할 수 있도록 만들기`
    - [x]  해당 게시글에 등록된 댓글 목록형식으로 보여주기 - `순서는 DESC`
    - [x]  댓글은 게시글 상세페이지에 들어올때 같이 보여주는데 타임리프로 값을 반복적으로 찍으면서 id와 함수에 증가하는 숫자를 부여해서 모든 댓글을 구분할 수 있도록 만들기
    - [x]  댓글 작성자는 해당 댓글 수정,삭제 가능하게 만들기
    - [x]  수정버튼 누를 시 해당 댓글이 readonly가 해제 되고 삭제버튼과 수정버튼은 사라지고 수정완료, 취소 버튼 생성
    - [x]  댓글 수정내용이 비어있지 않아야 수정가능하며 수정완료 버튼 누를 시 비동기식으로 처리하여 서버에 수정 반영 - `로딩없이 바로 수정된값을 볼 수 있으며 댓글 수정 내용이 수정 전과 동일하다면 return`
    - [x]  댓글 삭제 버튼 누를 시 "정말로 삭제하시겠습니까?" 메세지 출력후 확인/취소 버튼 중 "확인"버튼 누를 시 댓글 삭제 비동기식으로 처리해서 해당 댓글 id 기억해서 DB에 삭제 반영하고 댓글 id 참조해서 로딩없이 삭제 진행, 취소버튼 클릭시 삭제없이 유지
- [x]  예외처리 - `RestControllerAdvice 사용해서 구현 해보기(비밀번호 포함, 확인검사만 적용`)
- [x]  테스트데이터만들어서 추가하기
- [x]  테스트 코드 만들기 - 테스트케이스는 최소2개이상
    - [x]  회원가입 구현 - username, password, 중복확인, 비밀번호일치확인
- [x]  RDS-EC2로 배포해보기
---

![API](https://github.com/Sollertia/SpringProject/blob/main/images/noticeboard_API.png)

---

## 💻 기술스택

    SpringBoot, JPA, Gradle, Junit, Thymeleaf, SpringSecurity, H2-Base, MySql
---


# Trouble Shooting

## Junit Controller MVC Test 시 발생한 JPA meta model 오류

오류메세지
```
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jpaAuditingHandler': Cannot resolve reference to bean 'jpaMappingContext' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jpaMappingContext': Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: JPA metamodel must not be empty!
```
- 원인은 테스트용 어노테이션들은 JPA 관련 Bean 객체를 로딩하지 않기 때문에 @EnableJpaAuditing의 설정과 맞지 않아 오류가 발생
- 해결방법은 @EnableJpaAuditing을 직접 클래스를 만들어 Configuration하여 등록해서 사용하면 된다.
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfiguration {}
```
[참고사이트](https://stackoverflow.com/questions/51467132/spring-webmvctest-with-enablejpa-annotation)



## JPA id Auto_increment 문제 
- 처음 h2-Base에서 게시글을 작성하고 댓글을 작성했을 때 id 번호가 하나로 통일되서 증가하는 것을 확인한 후 mySQl사용시에도 같은 문제가 발생하는 것을 확인하고 문제를 인식하게 되었다.

- 원인은 JPA 1.5에서는 Hibernate의 Id 생성 규칙을 결정하는 설정값이 false가 기본값이여서 Hibernate5 부터 생긴 MySQL에서의 GenerationType.AUTO가 IDENTITY가 아닌 모든 TABLE id를 기본 시퀀스 전략으로 하는 설정을 사용하지 않았지만 2.0으로 바뀐 후 설정값이 true가 기본값이 되어서 GenerationType.AUTO를 사용하게 되었을 때 모든 테이블의 id시퀀스를 하나의 테이블로 관리한다.

- 해결방법은 application.properties에서 하이버네이트의 use-new-id-generator-mappings을 false로 설정하거나 @GeneratedValue의 전략을 GenerationType.IDENTITY로 지정하면 해결된다.



## Hidden 태그의 보안문제
- 사실 항상 Hidden 태그를 사용하면서 사용자가 데브툴스를 사용하여 확인하면 보안상 문제가 있지 않을까?라는 생각을 하기도 했고 토비의 스프링 책에서도 사용하지 말라고 되어있어서 이번 프로젝트를 하면서 확실하게 사용해도 되는건지에 대해서 결정을 하고 싶었다.

- 결론은 상황에 따라 사용할지 말지 정하면 되지 않을까? 였다. 많은 커뮤티에서 현업 개발자분들의 글도 읽어보고 직접 쿠팡, 11번가 등의 대기업 공지사항 사이트도 확인해 봤을 때 사용하는 곳도 있고 사용하지 않는 곳도 있었지만 하나 확실한 점은 데이터베이스의 값을 수정, 변경, 삭제할 수 있는 입력값 정보를 Hidden으로 사용하는 곳은 한군데도 없었다. 대부분은 이전페이지 정보등과 같은 페이지관련 정보에 사용하고 있었다.

- 나는 이번 프로젝트에 유저의 id를 Hidden태그에 사용해서 구현을 했다. 확실히 좋지 않은 방법이라고 생각한다. 그래서 대안으로 생각한 것은 Session이나 쿠키에 저장해서 사용하는 방법이다.

- 결론은 페이지 정보와 같은 데이터베이스나 개인프라이버시에 크게 영향을 끼치지 않는 정보들은 Hidden 태그를 사용해도 되고 그이외에 꼭 사용해야 한다면 Session이나 쿠키를 사용하면 좋을 것 같다.



## 보완해야 할 점
- SpringSecurity에서  WebSecurityConfigurerAdapter의 configure을 이용하여 http.authorizeRequests() 설정을 했는데 경로문제로 많은 시간을 소모했다. 이제 어느정도 구조와 기능에 대해서 파악은 했지만 깊게 들어가면 아직도 이해가 되지않는 부분이 많다. 앞으로 Security를 사용하게 될 수도 있으니 구조와 원리에 대해서 확실하게 학습할 필요가 있다.

- 예외처리에서 RestControllerAdvice을 사용하여 예외들을 처리했지만 아직 뭔가 매끄러운 느낌이 없는 것 같다. Enum으로 에러메세지,코드등을 관리하고 RestControllerAdvice에서 에러들을 구분하여 처리할 수 있게 구조를 짰지만 이게 진짜 현업에서 사용하는 방법인지?에 대한 의구심이 들었다. 예외처리는 매우 중요한 부분이라고 생각하기 때문에 다시 한번 여러가지 방법과 구조로 학습할 필요가 있다.

- Junit 테스트 솔직히 쉬운 것 같으면서도 굉장히 까다롭다. 일단 정확한 동작원리를 이해하지 못하면 코드작성이 매우 어렵게 느껴진다. 
이번프로젝트에서 발생한 문제는 유저이름 중복검사를 테스트하기 위해 코드를 작성했는데 나는 유저이름을 받아와 컨트롤러에서 서비스를 호출하여 서비스에서 레포지토리를 가져와 비교를 하는 구조인데 
controller클래스 테스트에서 입력값과 비교하기 위해 만든 가상의 유저인 Principal 객체를 입력값과 같이 보냈는데 이게 내가 만든 구조는 컨트롤러에서 서비스로 유저의 정보를 보내지 않기 때문에 
내가 보낸 Principal객체가 컨트롤러에서 서비스로 넘어가지 못했다. 처음에는 이걸 깨닫지 못하고 왜 값이 널로 나오지?라며 수 많은 BBulzit을 했는데 결국에는 깨닫고 컨트롤러에서 바로 레포지토리를 불러서 해보니까 해결됐다... 솔직히 이런경우는 테스트코드를 짜는 의미가 없는 것 같다. 그렇게 중요한 핵심 API도 아니고 간단하기 때문에....라고 일단 위로를 해야겠다.












