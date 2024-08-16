package api.expense;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExpenseController {
    @Autowired
    ExpenseRepository expenseRepository;

    @GetMapping("/")
    public String getAllExpenses(Model model, @AuthenticationPrincipal OAuth2User principal) {
        String userId = principal.getAttribute("id").toString();
        String username = principal.getAttribute("name");
        List<Expense> expenses = expenseRepository.findyByUserId(userId);
        
        // Calculate total expenses by category
        Map<String, Double> expensesByCategory = expenses.stream()
            .collect(Collectors.groupingBy(Expense::getCategory,
                Collectors.summingDouble(Expense::getAmount)));
        
        model.addAttribute("expenses", expenses);
        model.addAttribute("username", username);
        model.addAttribute("expensesByCategory", expensesByCategory);
        
        return "expenses";
    }

    @PostMapping("/expenses")
    public String addExpense(@ModelAttribute Expense expense, @AuthenticationPrincipal OAuth2User principal) {
        String userId = principal.getAttribute("id").toString();
        String username = principal.getAttribute("name");
        expense.setUserId(userId);
        expense.setUserName(username);
        expenseRepository.save(expense);
        return "redirect:/";
    }
}