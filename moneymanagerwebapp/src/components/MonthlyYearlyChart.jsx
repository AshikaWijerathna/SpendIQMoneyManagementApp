import {Bar,BarChart,CartesianGrid,Legend,ResponsiveContainer,Tooltip,XAxis,YAxis} from "recharts";

const monthNames = ["","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

const MonthlyYearlyChart = ({data}) =>{
    const chartData = data?.map((item)=>({
        month: monthNames[item.month],
        income: Number(item.income || 0),
        expense:Number(item.expense || 0),
        savings: Number(item.savings || 0),
    }));
    return(
        <div className="card">
            <h5 className="text-lg font-semibold mb-4">
                Monthly Income vs Expense
            </h5>
            <ResponsiveContainer width="100%" height={350}>
                <BarChart data={chartData}>
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis />
                    <Tooltip />
                    <Legend />

                    <Bar dataKey="income" name="Income" fill="#16a34a" radius={[6,6,0,0]}/>
                    <Bar dataKey="expense" name="Expense" fill="#dc2626" radius={[6,6,0,0]}/>
                    <Bar dataKey="savings" name="Savings" fill="#7e22ce" radius={[6,6,0,0]}/>
                </BarChart>
            </ResponsiveContainer>
        </div>
    )
}

export default MonthlyYearlyChart;