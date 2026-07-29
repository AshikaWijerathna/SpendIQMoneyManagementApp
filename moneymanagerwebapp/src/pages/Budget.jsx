import { useEffect, useState } from "react";
import Dashboard from "../components/Dashboard.jsx";
import toast from "react-hot-toast";
import { useUser } from "../hooks/useUser.jsx";
import axiosConfig from "../util/axiosConfig.jsx";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import BudgetForm from "../components/BudgetForm.jsx";
import BudgetList from "../components/BudgetList.jsx";
const Budget = () => {
  useUser();

  const [budgets, setBudjets] = useState([]);
  const [categories, setCategories] = useState([]);

  const [month, setMonth] = useState(new Date().getMonth() + 1);
  const [year, setYear] = useState(new Date().getFullYear());

  const fetchBudgets = async () => {
    try {
      const response = await axiosConfig.get(
        `${API_ENDPOINTS.BUDGETS}?month=${month}&year=${year}`,
      );

      if (response.status === 200) {
        setBudjets(response.data);
      }
    } catch (error) {
      console.error("Failed to fetch budgets:", error);
      toast.error("Failed to fetch budgets");
    }
  };
  const fetchExpenseCategories = async () => {
    try {
      const response = await axiosConfig.get(
        API_ENDPOINTS.CATEGORY_BY_TYPE("expense"),
      );
      if (response.status === 200) {
        setCategories(response.data);
      }
    } catch (error) {
      console.error("Failed to fetch expense categories: ", error);
      toast.error("Failed to fetch expense categories");
    }
  };
  const handleAddBudget = async (budget) => {
    if (!budget.categoryId) {
      toast.error("Category is required");
      return;
    }
    if (!budget.amount || Number(budget.amount) <= 0) {
      toast.error("Budget amount must be greater than 0");
      return;
    }
    try {
      await axiosConfig.post(API_ENDPOINTS.ADD_BUDGET, {
        categoryId: Number(budget.categoryId),
        amount: Number(budget.amount),
        month: Number(month),
        year: Number(year),
      });
      toast.success("Budget added successfully");
      fetchBudgets();
    } catch (error) {
      console.error("Error adding budget: ", error);
      toast.error(error.response?.data?.message || "Failed to add budget");
    }
  };
  const handleDeleteBudget = async (id) => {
    try {
      await axiosConfig.delete(API_ENDPOINTS.DELETE_BUDGET(id));
      toast.success("Budget deleted successfully");
      fetchBudgets();
    } catch (error) {
      console.error("Error deleting budget: ", error);
      toast.error("Failed to delete budget");
    }
  };
  const handleUpdateBudget = async (id, amount) => {
    if (!amount || Number(amount) <= 0) {
      toast.error("Budget amount must be greater than 0");
      return;
    }
    try {
      await axiosConfig.put(API_ENDPOINTS.UPDATE_BUDGET(id), {
        amount: Number(amount),
      });
      toast.success("Budget updated successfully");
      fetchBudgets();
    } catch (error) {
      console.error("Error updating budget: ", error);
      toast.error("Failed to update budget");
    }
  };
  useEffect(() => {
    fetchExpenseCategories();
  }, []);
  useEffect(() => {
    fetchBudgets();
  }, [month, year]);
  return (
    <Dashboard activeMenu="Budget">
      <div className="my-5 mx-auto">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-semibold">Budget Management</h2>
        </div>

        <div className="card mb-6">
          <h5 className="text-lg font-semibold mb-4">Select Budget Month</h5>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <select
              className="w-full border rounded px-3 py-2"
              value={month}
              onChange={(e) => setMonth(Number(e.target.value))}
            >
              <option value={1}>January</option>
              <option value={2}>February</option>
              <option value={3}>March</option>
              <option value={4}>April</option>
              <option value={5}>May</option>
              <option value={6}>June</option>
              <option value={7}>July</option>
              <option value={8}>August</option>
              <option value={9}>September</option>
              <option value={10}>October</option>
              <option value={11}>November</option>
              <option value={12}>December</option>
            </select>

            <input
              type="number"
              className="w-full border rounded px-3 py-2"
              value={year}
              onChange={(e) => setYear(Number(e.target.value))}
            />
          </div>
        </div>

        <BudgetForm categories={categories} onAddBudget={handleAddBudget} />

        <BudgetList
          budgets={budgets}
          onDelete={handleDeleteBudget}
          onUpdate={handleUpdateBudget}
        />
      </div>
    </Dashboard>
  );
};

export default Budget;
