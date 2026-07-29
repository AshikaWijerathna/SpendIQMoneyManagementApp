import { Coins, TrendingDown, TrendingUp } from "lucide-react";
import { addThousandsSeparator } from "../util/util.js";

const ReportSummaryCards = ({ title, data }) => {
  return (
  <div className="card mb-6">
      <h5 className="text-lg font-semibold mb-4">{title}</h5>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="p-4 rounded-xl bg-green-50 border border-green-100">
          <div className="flex items-center gap-3">
            <TrendingUp className="text-green-700" />
            <div>
              <p className="text-sm text-gray-500">Total Income</p>
              <h6 className="text-xl font-semibold text-green-700">
                $ {addThousandsSeparator(data?.totalIncome || 0)}
              </h6>
            </div>
          </div>
        </div>

        <div className="p-4 rounded-xl bg-red-50 border border-red-100">
          <div className="flex items-center gap-3">
            <TrendingDown className="text-red-700" />
            <div>
              <p className="text-sm text-gray-500">Total Expense</p>
              <h6 className="text-xl font-semibold text-red-700">
                $ {addThousandsSeparator(data?.totalExpense || 0)}
              </h6>
            </div>
          </div>
        </div>

        <div className="p-4 rounded-xl bg-purple-50 border border-purple-100">
          <div className="flex items-center gap-3">
            <Coins className="text-purple-700" />
            <div>
              <p className="text-sm text-gray-500">Total Savings</p>
              <h6
                className={`text-xl font-semibold ${
                  Number(data?.totalSavings || 0) < 0
                    ? "text-red-700"
                    : "text-purple-700"
                }`}
              >
                $ {addThousandsSeparator(data?.totalSavings || 0)}
              </h6>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReportSummaryCards;
