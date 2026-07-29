import { useEffect, useState } from "react";
import Dashboard from "../components/Dashboard.jsx";
import { useUser } from "../hooks/useUser.jsx";
import axiosConfig from "../util/axiosConfig.jsx";
import toast from "react-hot-toast";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import GoalForm from "../components/GoalForm.jsx";
import GoalList from "../components/GoalList.jsx";
const Goals = () => {
  useUser();
  const [goals, setGoals] = useState([]);
  const fetchGoals = async () => {
    try {
      const response = await axiosConfig.get(API_ENDPOINTS.GOALS);
      if (response.status === 200) {
        setGoals(response.data);
      }
    } catch (error) {
      console.error("Failed to fetch goals:", error);
      toast.error("Failed to fetch goals");
    }
  };
  const handleAddGoal = async (goal) => {
    if (!goal.goalName.trim()) {
      toast.error("Goal name is required");
      return;
    }
    if (!goal.targetAmount || Number(goal.targetAmount) <= 0) {
      toast.error("Target amount must be greater than 0");
      return;
    }
    if (!goal.deadline) {
      toast.error("Deadline is required");
      return;
    }
    try {
      await axiosConfig.post(API_ENDPOINTS.ADD_GOAL, {
        goalName: goal.goalName,
        targetAmount: Number(goal.targetAmount),
        currentAmount: Number(goal.currentAmount || 0),
        deadline: goal.deadline,
      });
      toast.success("Goal added successfully");
      fetchGoals();
    } catch (error) {
      console.error("Error adding goal: ", error);
      toast.error(error.response?.data?.message || "Failed to add goal");
    }
  };
  const handleDeleteGoal = async (id) => {
    try {
      await axiosConfig.delete(API_ENDPOINTS.DELETE_GOAL(id));
      toast.success("Goal deleted successfully");
      fetchGoals();
    } catch (error) {
      console.error("Error deleting goal: ", error);
      toast.error("Failed to delete goal");
    }
  };
  const handleAddSaving = async (id, amount) => {
    if (!amount || Number(amount) <= 0) {
      toast.error("Saving amount must be greater than 0");
      return;
    }
    try {
      await axiosConfig.patch(API_ENDPOINTS.ADD_GOAL_SAVING(id), {
        amount: Number(amount),
      });
      toast.success("Saving added successfully");
      fetchGoals();
    } catch (error) {
      console.error("Error adding saving: ", error);
      toast.error( error.response?.data?.message ||
      error.response?.data ||"Failed to add saving");
    }
  };
  useEffect(()=>{
    fetchGoals();
  },[]);
  return (
  <Dashboard activeMenu="Goals">
    <div className="my-5 mx-auto">
        <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-semibold">Financial Goals</h2>
        </div>
        <GoalForm onAddGoal={handleAddGoal}/>
        <GoalList goals={goals} onDelete={handleDeleteGoal} onAddSaving={handleAddSaving}/>
    </div>
  </Dashboard>
);
};
export default Goals;
