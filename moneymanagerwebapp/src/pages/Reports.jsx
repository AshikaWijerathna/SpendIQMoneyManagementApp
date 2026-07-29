import { useEffect, useState } from "react";
import Dashboard from "../components/Dashboard.jsx";
import { useUser } from "../hooks/useUser.jsx";
import axiosConfig from "../util/axiosConfig.jsx";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import toast from "react-hot-toast";
import ReportSummaryCards from "../components/ReportSummaryCards.jsx";
import MonthlyYearlyChart from "../components/MonthlyYearlyChart.jsx";
import CategorySpendingChart from "../components/CategorySpendingChart.jsx";

const Reports = () => {
  useUser();

  const [month, setMonth] = useState(new Date().getMonth() + 1);
  const [year, setYear] = useState(new Date().getFullYear());

  const [monthlySummary, setMonthlySummary] = useState(null);
  const [yearlySummary, setYearlySummary] = useState(null);
  const [monthlyData, setMonthlyData] = useState([]);
  const [categoryData, setCategoryData] = useState([]);

  const fetchReports = async () => {
    try {
      const monthlySummaryres = await axiosConfig.get(
        `${API_ENDPOINTS.MONTHLY_REPORT}?month=${month}&year=${year}`,
      );
      const yearlySummaryRes = await axiosConfig.get(
        `${API_ENDPOINTS.YEARLY_REPORT}?year=${year}`,
      );
      const yearlyMonthlyRes = await axiosConfig.get(
        `${API_ENDPOINTS.YEARLY_MONTHLY_REPORT}?year=${year}`,
      );
      const categoryRes = await axiosConfig.get(
        `${API_ENDPOINTS.CATEGORY_WISE_REPORT}?month=${month}&year=${year}`,
      );

      setMonthlySummary(monthlySummaryres.data);
      setYearlySummary(yearlySummaryRes.data);
      setMonthlyData(yearlyMonthlyRes.data);
      setCategoryData(categoryRes.data);
    } catch (error) {
      console.error("Failed to fetch reports: ", error);
      toast.error("Failed to fetch reports");
    }
  };
  useEffect(()=>{
    fetchReports();
  },[month, year]);
  return (
    <Dashboard activeMenu="Reports">
        <div className="my-5 mx-auto">
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-semibold">
                    Advanced Reports & Analytics
                </h2>
            </div>

            <div className="card mb-6">
                <h5 className="text-lg font-semibold mb-4">Select Report Period</h5>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <select className="w-full border rounded px-3 py-2" value={month} onChange={(e)=>setMonth(Number(e.target.value))}>
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
                    <input type="number" className="w-full border rounded px-3 py-2" value={year} onChange={(e)=> setYear(Number(e.target.value))}/>
                </div>
            </div>
            <ReportSummaryCards title="Monthly Financial Report" data={monthlySummary}/>
            <ReportSummaryCards title="Yearly Financial Report" data={yearlySummary}/>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
                <MonthlyYearlyChart data={monthlyData}/>
                <CategorySpendingChart data={categoryData}/>
            </div>
        </div>
    </Dashboard>
  );
};
export default Reports;
