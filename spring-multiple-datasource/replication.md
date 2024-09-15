### source db 초기 설정

- `docker run -p 3303:3303 --name mysql-source -e MYSQL_ROOT_PASSWORD=1234 -d mysql:8.0`
- `docker exec -it mysql-source /bin/bash`
- `microdnf install vim`
- `vim /etc/my.cnf`
  ```
  log-bin=mysql-bin
  server-id=1 
  port=3303
  binlog_do_db=coupon
  sync_binlog=1
  expire-logs-days=7
  binlog_format=ROW
  max_binlog_size=500M
  max_allowed_packet=1000M
  ```
- `docker restart mysql-source`
- `mysql -u root -p`
- `show gloable variables;`
- `show master status;`
- replication 접근 계정 생성 : `create user 'replication'@'%' identified with mysql_native_password by '1234';`
- 소스 서버 접근 권한 : `grant replication slave on *.* to 'replication'@'%';`
- `flush privileges;`
- `select user, host from mysql.user;`
- (memo) 바이너리 로그 파일 위치 기반 복제
    - 바이너리 로그 파일명 + 파일 내에서의 위치(실제 파일의 바이트 수)로 이벤트를 식별한다.
    - 레플리카가 자신이 어떤 바이너리 로그 파일의 어떤 위치까지 복제했는지 관리
    - 따라서 레플리카가 레플리케이션을 일시 중단하고 재개 가능하다.
    - `server_id` : 바이너리 로그에서 각 이벤트가 발생한 서버를 식별하기 위해 사용, 고유 해야함
    - 레플리카 소스 모두 1이라고 가정했을때, 레플리카는 자신에서 발생한 이벤트로 판단하고 적용하지 않음
    - 적용시 참고 사항 : 바이너리 로그 비활성화 기간이 존재할 수 있으며, 용량 확보를 위해 일정 기간 지난 바이너리 로그가 삭제되어 있을 수도 있다.
    - 데이터를 구성하기 이전에 mysqldump와 같은 툴을 이용해 소스 서버의 데이터를 레플리카 서버에 적재해야 한다.
- 아래는 추가 옵션
    - log-bin : mysql data directory인 /var/lib/mysql/에 호스트명-bin..000001 형태로 생성
    - maxallowedpacket : 서버로 질의하거나 받게되는 패킷의 최대 길이
    - binlog_format : 바이너리 로그의 저장 형식을 지정(STATEMENT , ROW , MIXED)
    - maxbinlogsize : 바이너리 로그의 최대 크기
    - sync_binlog : N개의 트랜잭션 마다 바이너리 로그를 디스크에 동기화 시킬지 결정한다. 1은 가장 안정적이지만, 가장 느린 설정이다.
    - expire-logs-days : 바이너리 로그가 만료되는 기간
    - binlogdodb : 레플리케이션을 적용할 데이터베이스 이름 설정. 설정하지 않으면, 모든 데이터베이스 대상으로 레플리케이션이 진행됨

### source db 생성 및 dump 생성

- `create database coupon;`
- `use coupon;`
- ```
  create table coupon (     
      id bigint not null auto_increment,     
      issue_count bigint not null,     
      issue_limit bigint not null,    
      primary key(id) 
  ) engine=InnoDB;
  ```
- `mysqldump -u root -p coupon > dump.sql`
- `docker cp mysql-source:dump.sql .`
- `docker run -p 3306:3306 --name mysql-replica -e MYSQL_ROOT_PASSWORD=1234 --link mysql-source -d mysql:8.0`
- `docker exec -it mysql-replica /bin/bash`
- `microdnf install vim`
- `vim /etc/my.cnf` : server-id 유의
  ```
  server-id=2
  ```
- `docker restart mysql-replica`
- `docker cp dump.sql mysql-replica:.`
- `mysql -u root -p`
- `create database coupon;`
- `mysql -u root -p coupon < dump.sql`

### source-replica 연결

replica

```
change master to master_host='mysql-source', master_user='replication',
master_port=3303, master_password='1234', master_log_file='mysql-bin.000001',
master_log_pos=1698;
```

- `master_log_pos` : source 서버 현재 로그 위치
- `master_log_file` : source 서버 바이너리 로그 파일명
- `show slave status\G`
- `start slave;`

replica 재설정

- `stop slave;`
- `reset slave;`
- `change master to..`