import { useState } from "react";

const BudgetForm = ({ categories, onAddBudget }) => {
  const [budget, setBudget] = useState({
    categoryId: "",
    amount: "",
  });
  const handleChange = (key, value) => {
    setBudget({ ...budget, [key]: value });
  };
  const handleSubmit = () => {
    onAddBudget(budget);

    setBudget({
      categoryId: "",
      amount: "",
    });
  };
  return(
    <div className="card mb-6">
        <h5 className="text-lg font-semibold mb-4">Add Category Budget</h5>
         <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
                <label className="block text-sm font-medium mb-1">Category</label>
                <select className="w-full border rounded px-3 py-2" value={budget.categoryId} onChange={(e)=> handleChange("categoryId", e.target.value)}>
                    <option value="">Select category</option>
                    {categories?.map((category)=>(
                        <option key={category.id} value={category.id}>
                            {category.name}
                        </option>
                    ))}
                </select>
            </div>

            <div>
                <label className="block text-sm font-medium mb-1">
                    Budget Amount
                </label>
                <input type="number" className="w-full border rounded px-3 py-2" placeholder="e.g., 50000" value={budget.amount} onChange={(e)=> handleChange("amount",e.target.value)}/>
            </div>
            <div className="flex items-end">
                <button type="button" className="add-btn add-btn-fill w-full flex items-center justify-center" onClick={handleSubmit}>Add Budget</button>
            </div>
         </div>
    </div>
  )
};

export default BudgetForm;
