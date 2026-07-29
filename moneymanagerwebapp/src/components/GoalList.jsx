import { Trash2, Trophy } from "lucide-react";
import { useState } from "react";
import { addThousandsSeparator } from "../util/util.js";
import moment from "moment";

const GoalList = ({ goals, onDelete, onAddSaving }) => {
  const [savingInputs, setSavingInputs] = useState({});

  const handleInputChange = (goalId, value) => {
    setSavingInputs({ ...savingInputs, [goalId]: value });
  };
  const handleAddSaving = (goalId) => {
    onAddSaving(goalId, savingInputs[goalId]);

    setSavingInputs({
      ...savingInputs,
      [goalId]: "",
    });
  };
  return (
    <div className="card">
      <h5 className="text-lg font-semibold mb-4">Goal Progress</h5>
      {goals?.length === 0 && (
        <p className="text-sm text-gray-500">No financial goals found.</p>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {goals?.map((goal)=>{
            const progressWidth=goal.progressPercentage > 100 ? 100 : goal.progressPercentage;

            return(
                <div key={goal.id} className={`border rounded-xl p-4 ${
                    goal.completed ? "border-green-300 bg-green-50" : "border-gray-200 bg-white"
                }`}>
                    <div className="flex items-start justify-between mb-3">
                        <div>
                            <h6 className="font-semibold text-gray-800 flex items-center gap-2">
                                {goal.completed && (
                                    <Trophy size={18} className="text-yellow-500"/>
                                )}
                                {goal.goalName}
                            </h6>
                            <p className="text-xs text-gray-500 mt-1">
                                Deadline: {moment(goal.deadline).format("Do MMM YYYY")}
                            </p>
                            {!goal.completed && goal.daysRemaining <= 7 && (
                                <p className="text-xs text-red-600 font-medium mt-1">
                                    Deadline approaching: {goal.daysRemaining} days left
                                </p>
                            )}
                            {goal.completed &&(
                                <p className="text-xs text-gren-600 font-medium mt-1">
                                    Goal completed!
                                </p>
                            )}
                        </div>
                        <button className="text-red-500 hover:text-red-700" onClick={()=> onDelete(goal.id)}>
                            <Trash2 size={18}/>
                        </button>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-3 text-sm mb-3">
                        <div>
                            <p className="text-gray-500">Target</p>
                            <p className="font-semibold">
                                $ {addThousandsSeparator(goal.targetAmount)}
                            </p>
                        </div>

                        <div>
                            <p className="text-gray-500">Saved</p>
                            <p className="font-semibold text-green-600">
                                $ {addThousandsSeparator(goal.currentAmount)}
                            </p>
                        </div>

                        <div>
                            <p className="text-gray-500">Remaining</p>
                            <p className="font-semibold text-purple-700">
                                $ {addThousandsSeparator(goal.remainingAmount)}
                            </p>
                        </div>
                    </div>

                    <div className="w-full bg-gray-200 rounded-full h-3">
                        <div className={`h-3 rounded-full ${goal.completed ? "bg-green-600" : "bg-purple-700"}`} style={{width: `${progressWidth}%`}}></div>
                    </div>

                    <p className="text-xs text-gray-500 mt-2">
                        {goal.progressPercentage}% completed
                    </p>
                    {!goal.completed && (
                        <div className="flex gap-2 mt-4">
                            <input type="number" className="w-full border rounded px-3 py-2 text-sm" placeholder="Add saving amount" value={savingInputs[goal.id] || ""} onChange={(e)=> handleInputChange(goal.id,e.target.value)}/>
                            <button type="button" className="add-btn add-btn-fill px-4 py-2" onClick={()=>handleAddSaving(goal.id)}>Add</button>
                        </div>
                    )}
                </div>
            );
        })}
      </div>
    </div>
  );
};
export default GoalList;
