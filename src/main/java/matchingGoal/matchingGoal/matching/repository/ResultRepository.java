package matchingGoal.matchingGoal.matching.repository;

import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

  boolean existsByGame(Game game);
  Optional<Result> findByGameId(long gameId);
}
