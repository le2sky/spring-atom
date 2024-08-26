insert into question(title, content, category)
values ('Spring MVC에 대한 동작 과정을 설명해주세요.', 'Spring MVC는 DispatcherServlet를 중심으로 이루어져 있습니다.', 'BACKEND'),
       ('JPA에 대해 설명해주세요.',
        'JPA는 자바 진영 ORM 표준 기술입니다.',
        'BACKEND'),
       ('REACT에 대해 설명해주세요.',
        '리액트는 자바스크립트 라이브러리의 하나로서 사용자 인터페이스를 만들기 위해 사용된다. 페이스북과 개별 개발자 및 기업들 공동체에 의해 유지보수된다. 리액트는 싱글 페이지 애플리케이션이나 모바일 애플리케이션 개발에 사용될 수 있다.',
        'FRONTEND'),
       ('Tanstack Query에 대해 설명해주세요.',
        '기존의 client state를 다루는 상태관리 라이브러리들과 다르게 sever state를 Fetching, Caching, 비동기적으로 업데이트 하는데 도움을 주는 라이브러리입니다.',
        'FRONTEND');

insert into subscribe(email, category)
values ('leehaneul990623@gmail.com', 'BACKEND'),
       ('brgndyy@gmail.com', 'FRONTEND');
