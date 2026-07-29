package com.example.moneymanager.service;

import com.example.moneymanager.dto.BudgetDTO;
import com.example.moneymanager.dto.ExpenseDTO;
import com.example.moneymanager.dto.GoalDTO;
import com.example.moneymanager.dto.ReportSummaryDTO;
import com.example.moneymanager.entity.BillReminderEntity;
import com.example.moneymanager.entity.ProfileEntity;
import com.example.moneymanager.repository.BillReminderRepository;
import com.example.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    private final BudgetService budgetService;
    private final GoalService goalService;
    private final ReportService reportService;
    private final BillReminderRepository billReminderRepository;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *", zone = "Asia/Colombo")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile: profiles){
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in Money Manager.<br><br>"
                    + "<a href="+frontendUrl+" style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Money Manager</a>"
                    + "<br><br>Best regards,<br>Money Manager Team";
            emailService.sendEmail(profile.getEmail(), "Daily reminder: Add your income and expense", body);
        }
        log.info("Job Completed: sendDailyIncomeExpenseReminder()");
    }
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Colombo")
    public void sendDailyExpenseSummary(){
        log.info("Job started: sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile: profiles){
            List<ExpenseDTO> todayExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
            if(!todayExpenses.isEmpty()){
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;'>S.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;'>Amount</th><th style='border:1px solid #ddd;padding:8px;'>Category</th></tr>");
                int i = 1;
                for(ExpenseDTO expense : todayExpenses){
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A").append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hi "+profile.getFullName()+",<br/><br/> Here is a summary of your expenses for today:<br/><br/>"+table+"<br/><br/>Best regards,<br/>Money Manager Team";
                emailService.sendEmail(profile.getEmail(), "Your daily Expense summary", body);
            }
        }
        log.info("Job Completed: sendDailyExpenseSummary()");
    }
    //@Scheduled(cron = "0 * * * * *", zone = "Asia/Colombo")
    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Colombo")
    public void sendUpcomingBillReminders(){
        log.info("Job started: sendUpcomingBillReminders()");
        LocalDate reminderDate = LocalDate.now().plusDays(3);
        List<BillReminderEntity> bills = billReminderRepository.findDueBillsWithProfile(reminderDate);
        for(BillReminderEntity bill : bills){
            ProfileEntity profile = bill.getProfile();

            String body = "Hi " + profile.getFullName()
                    + ",<br/><br/>Your bill is due soon.<br/><br/>"
                    + "<b>Bill:</b> " + bill.getBillName() + "<br/>"
                    + "<b>Amount:</b> Rs. " + bill.getAmount() + "<br/>"
                    + "<b>Due Date:</b> " + bill.getDueDate() + "<br/><br/>"
                    + "Please make the payment before the due date.";

            emailService.sendEmail(profile.getEmail(), "Upcoming Bill Reminder", body);

            bill.setReminderSent(true);
            billReminderRepository.save(bill);
        }
        log.info("Job completed: sendUpcomingBillReminders()");
    }
    //@Scheduled(cron = "0 30 8 * * *",zone = "Asia/Colombo")
    //@Scheduled(cron = "0 * * * * *",zone = "Asia/Colombo")
    public void sendBudgetWarnings(){
        log.info("Job started: sendBudgetWarnings()");

        LocalDate today = LocalDate.now();
        List<ProfileEntity> profiles = profileRepository.findAll();

        for(ProfileEntity profile : profiles){
            List<BudgetDTO> budgets = budgetService.getBudgetsForProfile(
                    profile,
                    today.getMonthValue(),
                    today.getYear()
            );
            for (BudgetDTO budget : budgets) {
                if (Boolean.TRUE.equals(budget.getExceeded())) {
                    String body = "Hi " + profile.getFullName()
                            + ",<br/><br/>Your <b>" + budget.getCategoryName() + "</b> budget has been exceeded.<br/><br/>"
                            + "Budget Amount: Rs. " + budget.getAmount() + "<br/>"
                            + "Spent Amount: Rs. " + budget.getSpentAmount() + "<br/>"
                            + "Remaining Amount: Rs. " + budget.getRemainingAmount();

                    emailService.sendEmail(profile.getEmail(), "Budget Exceeded Alert", body);
                } else if (budget.getUsedPercentage() != null && budget.getUsedPercentage() >= 90) {
                    String body = "Hi " + profile.getFullName()
                            + ",<br/><br/>You have used <b>" + budget.getUsedPercentage() + "%</b> of your "
                            + "<b>" + budget.getCategoryName() + "</b> budget.<br/><br/>"
                            + "Budget Amount: Rs. " + budget.getAmount() + "<br/>"
                            + "Spent Amount: Rs. " + budget.getSpentAmount() + "<br/>"
                            + "Remaining Amount: Rs. " + budget.getRemainingAmount();

                    emailService.sendEmail(profile.getEmail(), "Budget Limit Warning", body);
                }
        }

        }
        log.info("Job completed: sendBudgetWarnings()");
    }

    //@Scheduled(cron = "0 * * * * *",zone = "Asia/Colombo")
    @Scheduled(cron = "0 0 9 * * MON", zone = "Asia/Colombo")
    public void sendSavingsGoalReminders() {
        log.info("Job started: sendSavingsGoalReminders()");

        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {
            List<GoalDTO> goals = goalService.getGoalsForProfile(profile);

            for (GoalDTO goal : goals) {
                if (Boolean.TRUE.equals(goal.getCompleted())) {
                    continue;
                }

                String body = "Hi " + profile.getFullName()
                        + ",<br/><br/>This is a reminder to keep saving for your goal.<br/><br/>"
                        + "<b>Goal:</b> " + goal.getGoalName() + "<br/>"
                        + "<b>Target:</b> Rs. " + goal.getTargetAmount() + "<br/>"
                        + "<b>Saved:</b> Rs. " + goal.getCurrentAmount() + "<br/>"
                        + "<b>Remaining:</b> Rs. " + goal.getRemainingAmount() + "<br/>"
                        + "<b>Progress:</b> " + goal.getProgressPercentage() + "%<br/>"
                        + "<b>Deadline:</b> " + goal.getDeadline();

                emailService.sendEmail(profile.getEmail(), "Savings Goal Reminder", body);
            }
        }

        log.info("Job completed: sendSavingsGoalReminders()");
    }

    //@Scheduled(cron = "0 * * * * *",zone = "Asia/Colombo")
    @Scheduled(cron = "0 0 7 1 * *", zone="Asia/Colombo")
    public void sendMonthlyFinancialSummaryEmails(){
        log.info("Job started: sendMonthlyFinancialSummaryEmails()");

        LocalDate previousMonth = LocalDate.now().minusMonths(1);
        int month = previousMonth.getMonthValue();
        int year = previousMonth.getYear();

        List<ProfileEntity> profiles = profileRepository.findAll();

        for(ProfileEntity profile : profiles){
            ReportSummaryDTO report = reportService.getMonthlySummaryForProfile(profile,month,year);

            String body = "Hi " + profile.getFullName()
                    + ",<br/><br/>"
                    + "<h3>Monthly Financial Summary</h3>"
                    + "<b>Month:</b> " + month + "/" + year + "<br/><br/>"
                    + "<b>Total Income:</b> Rs. " + report.getTotalIncome() + "<br/>"
                    + "<b>Total Expense:</b> Rs. " + report.getTotalExpense() + "<br/>"
                    + "<b>Total Savings:</b> Rs. " + report.getTotalSavings() + "<br/><br/>"
                    + "<a href=" + frontendUrl + " style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>View Full Report</a>"
                    + "<br/><br/>Best regards,<br/>Money Manager Team";

            emailService.sendEmail(profile.getEmail(), "Monthly Financial Summary", body);
        }
        log.info("Job completed: sendMonthlyFinancialSummaryEmails()");
    }

}
