import { useState } from "react";
import { addThousandsSeparator } from "../util/util.js";
import { Pencil, Save, Trash2, X } from "lucide-react";

const BudgetList = ({ budgets, onDelete, onUpdate }) => {
  const [editingId, setEditingId] = useState(null);
  const [editAmount, setEditAmount] = useState("");

  const startEdit = (budget) => {
    setEditingId(budget.id);
    setEditAmount(budget.amount);
  };
  const cancelEdit = () => {
    onUpdate(id, editAmount);
    setEditingId(null);
    setEditAmount("");
  };
  const saveEdit = (id) => {
    onUpdate(id, editAmount);
    setEditingId(null);
    setEditAmount("");
  };
  return(
    <div className="card">
      <h5 className="text-lg font-semibold mb-4">
        Budget vs Actual Spending
      </h5>

      {budgets?.length === 0 && (
        <p className="text-sm text-gray-500">
          No budgets found for this month.
        </p>
      )}

      <div className="space-y-4">
        {budgets?.map((budget) => {
          const progressWidth =
            budget.usedPercentage > 100 ? 100 : budget.usedPercentage;

          return (
            <div
              key={budget.id}
              className={`border rounded-xl p-4 ${
                budget.exceeded
                  ? "border-red-300 bg-red-50"
                  : "border-gray-200 bg-white"
              }`}
            >
              <div className="flex items-center justify-between mb-3">
                <div>
                  <h6 className="font-semibold text-gray-800">
                    {budget.categoryName}
                  </h6>

                  {budget.exceeded && (
                    <p className="text-sm text-red-600 font-medium">
                      Budget exceeded!
                    </p>
                  )}
                </div>

                <div className="flex items-center gap-3">
                  {editingId === budget.id ? (
                    <>
                      <button
                        className="text-green-600 hover:text-green-800 cursor-pointer"
                        onClick={() => saveEdit(budget.id)}
                      >
                        <Save size={18} />
                      </button>

                      <button
                        className="text-gray-500 hover:text-gray-700 cursor-pointer"
                        onClick={cancelEdit}
                      >
                        <X size={18} />
                      </button>
                    </>
                  ) : (
                    <button
                      className="text-blue-500 hover:text-blue-700 cursor-pointer"
                      onClick={() => startEdit(budget)}
                    >
                      <Pencil size={18} />
                    </button>
                  )}

                  <button
                    className="text-red-500 hover:text-red-700 cursor-pointer"
                    onClick={() => onDelete(budget.id)}
                  >
                    <Trash2 size={18} />
                  </button>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-3 text-sm mb-3">
                <div>
                  <p className="text-gray-500">Budget</p>

                  {editingId === budget.id ? (
                    <input
                      type="number"
                      className="w-full border rounded px-3 py-2 mt-1"
                      value={editAmount}
                      onChange={(e) => setEditAmount(e.target.value)}
                    />
                  ) : (
                    <p className="font-semibold">
                      Rs. {addThousandsSeparator(budget.amount)}
                    </p>
                  )}
                </div>

                <div>
                  <p className="text-gray-500">Spent</p>
                  <p className="font-semibold text-red-600">
                    Rs. {addThousandsSeparator(budget.spentAmount)}
                  </p>
                </div>

                <div>
                  <p className="text-gray-500">Remaining</p>
                  <p
                    className={`font-semibold ${
                      budget.remainingAmount < 0
                        ? "text-red-600"
                        : "text-green-600"
                    }`}
                  >
                    Rs. {addThousandsSeparator(budget.remainingAmount)}
                  </p>
                </div>
              </div>

              <div className="w-full bg-gray-200 rounded-full h-3">
                <div
                  className={`h-3 rounded-full ${
                    budget.exceeded ? "bg-red-600" : "bg-green-600"
                  }`}
                  style={{ width: `${progressWidth}%` }}
                ></div>
              </div>

              <p className="text-xs text-gray-500 mt-2">
                {budget.usedPercentage}% used
              </p>
            </div>
          );
        })}
      </div>
    </div>
  )
};

export default BudgetList;
