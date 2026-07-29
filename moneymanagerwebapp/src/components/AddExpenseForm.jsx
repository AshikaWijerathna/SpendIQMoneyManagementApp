import { useEffect, useState } from "react";
import EmojiPickerPopup from "./EmojiPickerPopup.jsx";
import Input from "./Input.jsx";

const AddExpenseForm = ({ onAddExpense, categories }) => {
  //Add categories prop
  const [expense, setExpense] = useState({
    name: "",
    categoryId: "",
    amount: "",
    date: "",
    icon: "",
  });
  //Effect to set a default category if categories are loaded and none is selected
  useEffect(() => {
    if (categories && categories.length > 0 && !expense.categoryId) {
      //Automatically select teh first category as default if none is chosen
      setExpense((prev) => ({ ...prev, categoryId: categories[0].id })); //Use categories[0].id for MySQL
    }
  }, [categories, expense.categoryId]);

  const handleChange = (key, value) => setExpense({ ...expense, [key]: value }); //Changed setIcome to setExpense
  const categoryOptions = categories.map((cat) => ({
    value: cat.id, //Correct for Mysql 'id'
    label: `${cat.name}`, //Display icon and name in dropdown
  }));
  return (
    <div>
      <EmojiPickerPopup
        icon={expense.icon}
        onSelect={(selectedIcon) => handleChange("icon", selectedIcon)}
      />
      <Input
        value={expense.name}
        onChange={({ target }) => handleChange("name", target.value)}
        label="Expense Source"
        placeholder="e.g., Electricity, WIFI"
        type="text"
      />
      <Input
        value={expense.categoryId}
        onChange={({ target }) => handleChange("categoryId", target.value)}
        label="Category"
        isSelect={true}
        options={categoryOptions}
      />
      <Input
        value={expense.amount}
        onChange={({ target }) => handleChange("amount", target.value)}
        label="Amount"
        placeholder="e.g., 150.00"
        type="number"
      />
      <Input
        value={expense.date}
        onChange={({ target }) => handleChange("date", target.value)}
        label="Date"
        placeholder=""
        type="date"
      />
      <div className="flex justify-end mt-6">
        <button
          type="button"
          className="add-btn add-btn-fill"
          onClick={() => onAddExpense(expense)}
        >
          Add Expense
        </button>
      </div>
    </div>
  );
};

export default AddExpenseForm;
