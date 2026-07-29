import{ Cell, Legend, Pie, PieChart, ResponsiveContainer, Tooltip,} from "recharts";

const COLORS =["#7e22ce","#dc2626","#16a34a","#2563eb","#f97316","#0891b2","#be123c",];

const CategorySpendingChart = ({data}) =>{
    const chartData = data?.map((item)=>({
        name: item.categoryName,
        amount: Number(item.totalAmount || 0),
    }));

    return(
        <div className="card">
            <h5 className="text-lg font-semibold mb-4">
                Category-wise Spending Analysis
            </h5>

            {chartData?.length === 0 ? (
                <p className="text-sm text-gray-500">No category spending found for this month.</p>
            ):(
                <ResponsiveContainer width="100%" height={350}>
                    <PieChart>
                        <Pie data={chartData} dataKey="amount" nameKey="name" cx="50%" cy="50%" outerRadius={120} innerRadius={70} labelLine={false}>
                            {chartData?.map((entry, index)=> (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]}/>
                            ))}
                        </Pie>
                        <Tooltip />
                        <Legend />
                    </PieChart>
                </ResponsiveContainer>
            )}
        </div>
    )
}

export default CategorySpendingChart;