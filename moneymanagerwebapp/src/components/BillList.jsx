import { CheckCircle, Trash2 } from "lucide-react";
import moment from "moment";
import { addThousandsSeparator } from "../util/util.js";

const BillList = ({ bills, onMarkPaid, onDelete }) => {
  return (
    <div className="card">
      <h5 className="text-lg font-semibold mb-4">Bills</h5>
      {bills?.length === 0 && (
        <p className="text-sm text-gray-500">No bills reminders found.</p>
      )}
      <div className="space-y-4">
        {bills?.map((bill) => {
          const isDueSoon =
            moment(bill.dueDate).diff(moment(), "days") <= 3 && !bill.paid;

          return (
            <div
              key={bill.id}
              className={`border rounded-xl p-4 ${bill.paid ? "bg-green-50 border-green-200" : isDueSoon ? "bg-red-50 border-red-200" : "bg-white border-gray-200"}`}
            >
              <div className="flex items-center justify-between">
                <div>
                  <h6 className="font-semibold text-gray-800">
                    {bill.billName}
                  </h6>
                  <p className="text-sm text-gray-500">
                    Amount: $ {addThousandsSeparator(bill.amount)}
                  </p>
                  <p className="text-sm text-gray-500">
                    Due Date:{moment(bill.dueDate).format("Do MMM YYYY")}
                  </p>
                  {isDueSoon && (
                    <p className="text-sm text-red-600 font-medium">
                      Due soon!
                    </p>
                  )}
                  {bill.paid && (
                    <p className="text-sm text-green-600 font-medium">Paid</p>
                  )}
                </div>

                <div className="flex items-center gap-3">
                  {!bill.paid && (
                    <button
                      className="text-green-600 hover:text-green-800"
                      onClick={() => onMarkPaid(bill.id)}
                    >
                      <CheckCircle size={20} />
                    </button>
                  )}

                  <button
                    className="text-red-500 hover:text-red-700"
                    onClick={() => onDelete(bill.id)}
                  >
                    <Trash2 size={20} />
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default BillList;
