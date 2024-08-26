package maeilmail;

import org.springframework.data.jpa.repository.JpaRepository;

interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
}
