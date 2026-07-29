import { useState } from "react";

const BillForm = ({onAddBill})=>{
    const [bill, setBill] = useState({
    billName: "",
    amount: "",
    dueDate: "",
  });

  const handleChange = (key, value) => {
    setBill({ ...bill, [key]: value });
  };

  const handleSubmit = () => {
    onAddBill(bill);

    setBill({
      billName: "",
      amount: "",
      dueDate: "",
    });
  };
    return (
    <div className="card mb-6">
        <h5 className="text-lg font-semibold mb-4">Add Bill Reminder</h5>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">

            <div>
                <label className="block text-sm font-medium mb-1">Bill Name</label>
                <input type="text" className="w-full border rounded px-3 py-2" placeholder="Electricity Bill" value={bill.billName} onChange={(e)=> handleChange("billName", e.target.value)}/>
            </div>

            <div>
                <label className="block text-sm font-medium mb-1">Amount</label>
                <input type="number" className="w-full border rounded px-3 py-2" placeholder="8500" value={bill.amount} onChange={(e)=> handleChange("amount", e.target.value)}/>
            </div>

            <div>
                <label className="block text-sm font-meidum mb-1">Due Date</label>
                <input type="date" className="w-full border rounded px-3 py-2" value={bill.dueDate} onChange={(e)=>handleChange("dueDate", e.target.value)}/>
            </div>

            <div className="flex items-end">
                <button type="button" className="add-btn add-btn-fill px-6 py-2" onClick={handleSubmit}>Add Bill</button>
            </div>
        </div>
    </div>
    );
}

export default BillForm;