import { useState } from "react";

const GoalForm = ({ onAddGoal }) => {
  const [goal, setGoal] = useState({
    goalName: "",
    targetAmount: "",
    currentAmount: "",
    deadline: "",
  });

  const handleChange = (key, value) => {
    setGoal({ ...goal, [key]: value });
  };

  const handleSubmit = () => {
    onAddGoal(goal);

    setGoal({
      goalName: "",
      targetAmount: "",
      currentAmount: "",
      deadline: "",
    });
  };
  return (
    <div className="card mb-6">
      <h5 className="text-lg font-semibold mb-4">Create Financial Goal</h5>
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label className="block text-sm font-medium mb-1">Goal Name</label>
          <input
            type="text"
            className="w-full border rounded px-3 py-2"
            palceholder="Emergency Fund"
            value={goal.goalName}
            onChange={(e) => handleChange("goalName", e.target.value)}
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">
            Target Amount
          </label>
          <input
            type="number"
            className="w-full border rounded px-3 py-2"
            placeholder="50000"
            value={goal.targetAmount}
            onChange={(e) => handleChange("targetAmount", e.target.value)}
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">
            Current Saved
          </label>
          <input
            type="number"
            className="w-full border rounded px-3 py-2"
            placeholder="50000"
            value={goal.currentAmount}
            onChange={(e) => handleChange("currentAmount", e.target.value)}
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Deadline</label>
          <input
            type="date"
            className="w-full border rounded px-3 py-2"
            value={goal.deadline}
            onChange={(e) => handleChange("deadline", e.target.value)}
          />
        </div>

        <div className="md:col-span-4 flex justify-end">
          <button
            type="button"
            className="add-btn add-btn-fill px-6 py-2"
            onClick={handleSubmit}
          >
            Add Goal
          </button>
        </div>
      </div>
    </div>
  );
};
export default GoalForm;
