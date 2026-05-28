package cz.mendelu.devtrendsexplorer.domain.userwatchlist;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserWatchlistRepository extends CrudRepository<UserWatchlist, UserWatchlistId> {

    List<UserWatchlist> findAllByUserId(String userId);

    @Query("select uw from UserWatchlist uw join fetch uw.repository where uw.userId = :userId")
    List<UserWatchlist> findAllByUserIdWithRepository(@Param("userId") String userId);

    @Query("select uw from UserWatchlist uw join fetch uw.repository where uw.userId = :userId and uw.repository.id = :repoId")
    Optional<UserWatchlist> findByUserIdAndRepositoryIdWithRepository(@Param("userId") String userId,
                                                                      @Param("repoId") Long repoId);

    void deleteByUserIdAndRepositoryId(String userId, Long repoId);

    @Query("select count(uw) > 0 from UserWatchlist uw where uw.userId = :userId and uw.repository.id = :repoId")
    boolean existsByUserIdAndRepositoryId(@Param("userId") String userId,
                                          @Param("repoId") Long repoId);
}
