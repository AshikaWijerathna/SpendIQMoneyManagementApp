import { useEffect, useState } from "react";
import Dashboard from "../components/Dashboard.jsx";
import { useUser } from "../hooks/useUser.jsx";
import axiosConfig from "../util/axiosConfig.jsx";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import toast from "react-hot-toast";
import BillForm from "../components/BillForm.jsx";
import BillList from "../components/BillList.jsx";

const Bills = ()=>{
    useUser();

    const[bills,setBills] = useState([]);

    const fetchBills = async ()=>{
        try{
            const response= await axiosConfig.get(API_ENDPOINTS.GET_BILLS);
            setBills(response.data);
        }catch(error){
            toast.error("Failed to fetch bills");
        }
    }

    const handleAddBill = async (bill)=>{
        if(!bill.billName.trim()){
            toast.error("Bill name is required");
            return;
        }
        if(!bill.amount || Number(bill.amount) <= 0){
            toast.error("Amount must be greater than 0");
            return;
        }
        if(!bill.dueDate){
            toast.error("Due date is required");
            return;
        }
        try{
            await axiosConfig.post(API_ENDPOINTS.ADD_BILL, {
                billName:bill.billName,
                amount:Number(bill.amount),
                dueDate: bill.dueDate,
            });
            toast.success("Bill reminder added");
            fetchBills();
        }catch(error){
            toast.error("Failed to add bill");
        }
    }
    const handleMarkPaid = async(id)=>{
        try{
            await axiosConfig.put(API_ENDPOINTS.MARK_BILL_PAID(id));
            toast.success("Bill marked bill sa paid");
            fetchBills();
        }catch(error){
            toast.error("Failed to mark bill as Paid");
        }
    }
    const handleDeleteBill = async(id)=>{
        try{
            await axiosConfig.delete(API_ENDPOINTS.DELETE_BILL(id));
            toast.success("Bill deleted");
            fetchBills();
        }catch(error){
            toast.error("Failed to delete bill");
        }
    };
    useEffect(()=>{
        fetchBills();
    },[]);
    return (
    
    <Dashboard activeMenu="Bills">
        <div className="my-5 mx-auto">
            <h2 className="text-2xl font-semibold">Upcoming Bill Reminders</h2>
            <BillForm onAddBill={handleAddBill}/>
            <BillList bills={bills} onMarkPaid={handleMarkPaid} onDelete={handleDeleteBill}/>
        </div>
    </Dashboard>
)
}
export default Bills;