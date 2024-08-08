package api.expense;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    @Query("{'userId': ?0}")
    List<Expense> findyByUserId(String userId);
}
